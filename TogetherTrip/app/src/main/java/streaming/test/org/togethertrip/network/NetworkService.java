package streaming.test.org.togethertrip.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import streaming.test.org.togethertrip.datas.TouristSpotSearchList;

/**
 * Created by taehyung on 2017-09-04.
 */

public interface NetworkService {
    //관광지 검색
    @GET("trips/list/{keyword}")
    Call<TouristSpotSearchList> searchTouristSpot(@Path("keyword") String keyword);

}
