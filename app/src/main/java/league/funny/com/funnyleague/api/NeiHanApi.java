package league.funny.com.funnyleague.api;

import league.funny.com.funnyleague.bean.neihan.NeiHanResponse;
import retrofit2.http.GET;
import rx.Observable;

public interface NeiHanApi {

    @GET("/joke/?is_json=1&app_name=neihanshequ_web&max_time={time}")
    Observable<NeiHanResponse> getText();

    @GET("/pic/?is_json=1&app_name=neihanshequ_web&max_time={time}")
    Observable<NeiHanResponse> getImage();

}
