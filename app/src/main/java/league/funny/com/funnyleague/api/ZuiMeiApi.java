package league.funny.com.funnyleague.api;

import league.funny.com.funnyleague.bean.image.ImageResponse;
import retrofit2.http.GET;
import rx.Observable;

public interface ZuiMeiApi {

//    @GET("/api/4/news/latest")
//    Observable<ZhihuDaily> getLastDaily();
//
//    @GET("/api/4/news/before/{date}")
//    Observable<ZhihuDaily> getTheDaily(@Path("date") String date);
//
//    @GET("/api/4/news/{id}")
//    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);

    @GET("/wallpaper/category/1/")
    Observable<ImageResponse> getImage();

}
