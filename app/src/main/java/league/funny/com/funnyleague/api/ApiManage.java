package league.funny.com.funnyleague.api;

import java.io.File;
import java.io.IOException;

import league.funny.com.funnyleague.FunnyLeagueApplication;
import league.funny.com.funnyleague.util.NetworkUtil;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by inno-y on 2017/3/7.
 */

public class ApiManage {

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtil.isNetWorkAvailable(FunnyLeagueApplication.getContext())) {
                int maxAge = 1; // 在线缓存在1分钟内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    public static ApiManage apiManage;
    private static File httpCacheDirectory = new File(FunnyLeagueApplication.getContext().getCacheDir(), "zhihuCache");
    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(cache)
            .build();

    private Object monitor = new Object();
    public ZuiMeiApi zuiMeiApi;
    public NeiHanApi neiHanApi;

    public static ApiManage getInstence() {
        if (apiManage == null) {
            synchronized (ApiManage.class) {
                if (apiManage == null) {
                    apiManage = new ApiManage();
                }
            }
        }
        return apiManage;
    }

    public ZuiMeiApi getZuiMeiApiService() {
        if (zuiMeiApi == null) {
            synchronized (monitor) {
                if (zuiMeiApi == null) {
                    zuiMeiApi = new Retrofit.Builder()
                            .baseUrl("http://lab.zuimeia.com")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ZuiMeiApi.class);
                }
            }
        }

        return zuiMeiApi;
    }

    public NeiHanApi getNeiHanApiService() {
        if (neiHanApi == null) {
            synchronized (monitor) {
                if (neiHanApi == null) {
                    neiHanApi = new Retrofit.Builder()
                            .baseUrl("http://neihanshequ.com")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(new OkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(NeiHanApi.class);
                }
            }
        }

        return neiHanApi;
    }

}
