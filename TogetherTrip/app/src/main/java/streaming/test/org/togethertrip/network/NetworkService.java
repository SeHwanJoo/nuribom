package streaming.test.org.togethertrip.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import streaming.test.org.togethertrip.datas.CourseWriteDatas;
import streaming.test.org.togethertrip.datas.CourseWriteResult;
import retrofit2.http.Part;
import retrofit2.http.Path;
import streaming.test.org.togethertrip.datas.CourseResult;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResult;
import streaming.test.org.togethertrip.datas.DetailSpotListDatas;
import streaming.test.org.togethertrip.datas.MessageResult;
import streaming.test.org.togethertrip.datas.RegisterDatas;
import streaming.test.org.togethertrip.datas.ReviewResult;
import streaming.test.org.togethertrip.datas.SearchData;
import streaming.test.org.togethertrip.datas.TouristSpotReviewResult;
import streaming.test.org.togethertrip.datas.TouristSpotSearchResult;
import streaming.test.org.togethertrip.datas.like.AddLikeInfo;
import streaming.test.org.togethertrip.datas.like.AddLikeResult;
import streaming.test.org.togethertrip.datas.like.RemoveLikeInfo;
import streaming.test.org.togethertrip.datas.like.RemoveLikeResult;

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

    //코스작성
    @POST("edit")
    Call<CourseWriteResult> writeCourse(@Body CourseWriteDatas courseWriteDatas);

    // 후기 작성
    @Multipart
    @POST("/trips/review")
    Call<ReviewResult> reviewRegister(@Part("userid") RequestBody userid,
                                      @Part("contentid") RequestBody contentid,
                                      @Part("stars") RequestBody stars,
                                      @Part("content") RequestBody content,
                                      @Part("date") RequestBody date,
                                      @Part MultipartBody.Part[] image);

    //후기 리스트뷰 띄우기
    @GET("/trips/reviews/{contentid}")
    Call<TouristSpotReviewResult> getMainResult(@Path("contentid") String contentid);

    //관광코스 그리드뷰 띄우기
    @GET("/course/list/{keyword}/{userid}")
    Call<CourseResult> getCourseResult(@Path("keyword") String keyword,
                                       @Path("userid") String userid);

    //코스 좋아요
    @POST("/course/like")
    Call<AddLikeResult> addLikeResult (@Body AddLikeInfo addLikeInfo);

    //코스 좋아요 취소
    @POST("/course/like")
    Call<RemoveLikeResult> removeLikeResult (@Body RemoveLikeInfo removeLikeInfo);
}
