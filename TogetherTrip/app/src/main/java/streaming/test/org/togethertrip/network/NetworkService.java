package streaming.test.org.togethertrip.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResult;
import streaming.test.org.togethertrip.datas.DetailSpotListDatas;
import streaming.test.org.togethertrip.datas.MessageResult;
import streaming.test.org.togethertrip.datas.RegisterDatas;
import streaming.test.org.togethertrip.datas.SearchData;
import streaming.test.org.togethertrip.datas.TouristSpotSearchResult;

/**
 * Created by taehyung on 2017-09-04.
 */

public interface NetworkService {
    //관광지 검색
    @POST("trips/list")
    Call<TouristSpotSearchResult> searchTouristSpot(@Body SearchData searchData);

    //관광지 상세보기
    @POST("trips/")
    Call<DetailSpotListClickResult> clickDetailSpotList(@Body DetailSpotListDatas detailSpotListDatas);

    //회원가입
    @POST("users")
    Call<MessageResult> requestSignup(@Body RegisterDatas registerDatas);

}
