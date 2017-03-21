package league.funny.com.funnyleague.api;

import league.funny.com.funnyleague.bean.neihan.NeiHanResponse;
import retrofit2.http.GET;
import rx.Observable;

public interface NeiHanApi {

    @GET("/joke/?is_json=1")
    Observable<NeiHanResponse> getText();

    @GET("/pic/?is_json=1")
    Observable<NeiHanResponse> getImage();

}
