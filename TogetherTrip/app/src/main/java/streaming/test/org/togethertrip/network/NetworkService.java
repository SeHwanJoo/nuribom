package streaming.test.org.togethertrip.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import streaming.test.org.togethertrip.datas.AlarmDatas;
import streaming.test.org.togethertrip.datas.CourseInfo;
import streaming.test.org.togethertrip.datas.CourseResult;
import streaming.test.org.togethertrip.datas.CourseWriteDatas;
import streaming.test.org.togethertrip.datas.CourseWriteResult;
import streaming.test.org.togethertrip.datas.DetailCourseDatas;
import streaming.test.org.togethertrip.datas.DetailCourseInfo;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResult;
import streaming.test.org.togethertrip.datas.DetailSpotListDatas;
import streaming.test.org.togethertrip.datas.MessageResult;
import streaming.test.org.togethertrip.datas.RegisterDatas;
import streaming.test.org.togethertrip.datas.ResultMessage;
import streaming.test.org.togethertrip.datas.ReviewResult;
import streaming.test.org.togethertrip.datas.SearchData;
import streaming.test.org.togethertrip.datas.TouristSpotReviewResult;
import streaming.test.org.togethertrip.datas.TouristSpotSearchResult;
import streaming.test.org.togethertrip.datas.UserInfoResult;
import streaming.test.org.togethertrip.datas.comment.CommentInfo;
import streaming.test.org.togethertrip.datas.comment.CommentResult;
import streaming.test.org.togethertrip.datas.comment.CommentWriteDatas;
import streaming.test.org.togethertrip.datas.comment.CommentWriteResult;
import streaming.test.org.togethertrip.datas.duplicationcheck.EmailCheckResult;
import streaming.test.org.togethertrip.datas.duplicationcheck.NicknameCheckResult;
import streaming.test.org.togethertrip.datas.like.AddLikeInfo;
import streaming.test.org.togethertrip.datas.like.AddLikeResult;
import streaming.test.org.togethertrip.datas.like.AddTripsLikeInfo;
import streaming.test.org.togethertrip.datas.login.LoginDatas;
import streaming.test.org.togethertrip.datas.login.LoginResult;

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

    //로그인
    @POST("users/login")
    Call<LoginResult> requestSignin(@Body LoginDatas loginDatas);

    //이메일 중복체크
    @GET("users/emailtest/{email}")
    Call<EmailCheckResult> emailCheck (@Path("email")String email);

    //닉네임 중복체크
    @GET("users/idtest/{userid}")
    Call<NicknameCheckResult> nicknameCheck(@Path("userid")String userid);

    //코스작성
    @Multipart
    @POST("/course/edit")
    Call<CourseWriteResult> writeCourse(@Part("body") CourseWriteDatas courseWriteDatas,
                                            @Part MultipartBody.Part[] image);

    //코스 상세보기
    @POST("course/")
    Call<DetailCourseDatas> clickDetailCourseList (@Body DetailCourseInfo detailCourseInfo);

    //코스 그리드뷰 띄우기
    @POST("/course/list")
    Call<CourseResult> getCourseResult(@Body CourseInfo courseInfo);

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

    // 댓글 작성
    @POST("/course/comment")
    Call<CommentWriteResult> writeComment(@Body CommentWriteDatas commentWriteDatas);

    //댓글 불러오기
    @POST("/course/comments")
    Call<CommentResult> getComment(@Body CommentInfo commentInfo);

    //코스 좋아요
    @POST("/course/like")
    Call<AddLikeResult> addLikeResult (@Body AddLikeInfo addLikeInfo);

    //관광지 좋아요
    @POST("trips/like")
    Call<AddLikeResult> addTripLikeResult(@Body AddTripsLikeInfo addLikeInfo);


    @GET("/mypage/{userid}")
    Call<UserInfoResult> getUserInfo(@Path("userid") String userid);

    @GET("/users/alarm/{userid}")
    Call<AlarmDatas> getAlarm(@Path("userid") String userid);

    @Multipart
    @POST("/users/modify")
    Call<ResultMessage> modifyProfile(@Part MultipartBody.Part image,
                                      @Part RequestBody userid);

    @GET("/mypage/logout/{userid}")
    Call<ResultMessage> requestLogout(@Path("userid") String userid);
}
