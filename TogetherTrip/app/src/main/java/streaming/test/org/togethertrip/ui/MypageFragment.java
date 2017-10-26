package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.UserInfoResult;
import streaming.test.org.togethertrip.network.NetworkService;

/**
 * Created by taehyung on 2017-09-06.
 */

public class MypageFragment extends Fragment {
    private static final String TAG = "MypageFragmentLog";
    Activity activity;
    Context context;

    NetworkService networkservice;
    UserInfoResult userInfoResult;
    String checkString;

    String email, profileImg, nickName, token;

    TextView loginOrLogout;
    TextView signUpOrSignIn, settings_profile;
    TextView mywrite_course, mywrite_review, myLocker;

    LogoutDialog logoutDialog;

    public MypageFragment(Activity activity, String email, String profileImg, String nickName, String token){
        this.activity = activity;
        this.context = activity;
        this.email = email;
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.token = token;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_mypage, container, false);
        View view = null;

        checkLogin();
        try {
            if (userInfoResult.message.equals("no") ) { // 로그인이 안되어있을 때
                view = inflater.inflate(R.layout.mypage_nologin, container, false);

                loginOrLogout = (TextView) view.findViewById(R.id.settings_login);
                signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);

                loginOrLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(activity, SigninActivity.class));
                    }
                });
                signUpOrSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(activity, SignupActivity.class));
                    }
                });

            }else{ // 로그인이 되어있을 때
                view = inflater.inflate(R.layout.activity_mypage, container, false);

                loginOrLogout = (TextView) view.findViewById(R.id.settings_logout);
                mywrite_course = (TextView) view.findViewById(R.id.mywrite_course);
                mywrite_review = (TextView) view.findViewById(R.id.mywrite_review);
                myLocker = (TextView) view.findViewById(R.id.mylocker);
                settings_profile = (TextView) view.findViewById(R.id.settings_profile);
                CircleImageView userProfile = (CircleImageView) view.findViewById(R.id.userProfile);
                TextView userNickName = (TextView) view.findViewById(R.id.userNickName);
                TextView userEmail = (TextView) view.findViewById(R.id.userEmail);

                userEmail.setText(email);
                userNickName.setText(nickName);
                Log.d(TAG, "onCreateView: image in Mypage: " + profileImg);
                if(profileImg == null) {
                    userProfile.setImageResource(R.drawable.mypage_profile_defalt);
                }else{
                    Glide.with(context).load(profileImg).into(userProfile);
                }
                mywrite_course.setText(""+userInfoResult.result.course);
                mywrite_review.setText(""+(userInfoResult.result.coursecomment+userInfoResult.result.tripreviews));
                myLocker.setText(""+(userInfoResult.result.courselike+userInfoResult.result.triplike));

                loginOrLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutDialog = new LogoutDialog(context, leftListner, rightLisnter);
                        logoutDialog.show();
                    }
                });

                settings_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(context, ProfileChangeActivity.class));
                    }
                });
            }
        }catch(Exception e){
            //로그인 유무 체크 중 예외 발생시 디폴트로 No Login으로 띄우기
            e.printStackTrace();
            view = inflater.inflate(R.layout.mypage_nologin, container, false);

            loginOrLogout = (TextView) view.findViewById(R.id.settings_login);
            signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);
            loginOrLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SigninActivity.class));
                }
            });
            signUpOrSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SignupActivity.class));
                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //첫 진입시 로그인 유무확인 네트워킹
    public void checkLogin(){

        NetworkService networkService = ApplicationController.getInstance().getNetworkService();

        if(nickName == null){
            nickName="No Login User@";
        }

        Log.d(TAG, "checkLogin: email: " + nickName);

        Call<UserInfoResult> requestDriverApplyOwner = networkService.getUserInfo(nickName);
        requestDriverApplyOwner.enqueue(new Callback<UserInfoResult>() {
            @Override
            public void onResponse(Call<UserInfoResult> call, Response<UserInfoResult> response) {
                if (response.isSuccessful()) {
                    userInfoResult = response.body();

                    Log.d(TAG, "onResponse: result: " + userInfoResult.result);
                    Log.d(TAG, "onResponse: message : " + userInfoResult.message);
                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                if(nickName==null){
                    Toast.makeText(activity, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void logout(){

        NetworkService networkService = ApplicationController.getInstance().getNetworkService();

        nickName = "No Login User##";

        Call<UserInfoResult> requestDriverApplyOwner = networkService.getUserInfo(nickName);
        requestDriverApplyOwner.enqueue(new Callback<UserInfoResult>() {
            @Override
            public void onResponse(Call<UserInfoResult> call, Response<UserInfoResult> response) {
                if (response.isSuccessful()) {
                    userInfoResult = response.body();

                    Log.d(TAG, "onResponse: logout result: " + userInfoResult.result);
                    Log.d(TAG, "onResponse: logout message : " + userInfoResult.message);
                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private View.OnClickListener leftListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //취소 클릭시
            logoutDialog.dismiss();
        }
    };

    private View.OnClickListener rightLisnter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //로그아웃 클릭시
            logout();
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            logoutDialog.dismiss();


        }
    };

}
