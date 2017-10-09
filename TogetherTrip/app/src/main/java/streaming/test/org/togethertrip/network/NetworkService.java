package streaming.test.org.togethertrip.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import streaming.test.org.togethertrip.datas.CourseWriteDatas;
import streaming.test.org.togethertrip.datas.CourseWriteResult;
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

    //회원가입
    @POST("users")
    Call<MessageResult> requestSignup(@Body RegisterDatas registerDatas);

    //코스작성
    @POST("edit")
    Call<CourseWriteResult> writeCourse(@Body CourseWriteDatas courseWriteDatas);
}
