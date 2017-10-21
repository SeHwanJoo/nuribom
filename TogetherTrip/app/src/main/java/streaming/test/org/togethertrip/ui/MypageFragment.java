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



    TextView loginOrLogout;
    TextView signUpOrSignIn;
    TextView mywrite_course, mywrite_review, myLocker;

    public MypageFragment(Activity activity){
        this.activity = activity;
        this.context = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

            }else{ // 로그인이 되어있을 떄
                view = inflater.inflate(R.layout.activity_mypage, container, false);

                loginOrLogout = (TextView) view.findViewById(R.id.settings_logout);
                mywrite_course = (TextView) view.findViewById(R.id.mywrite_course);
                mywrite_review = (TextView) view.findViewById(R.id.mywrite_review);
                myLocker = (TextView) view.findViewById(R.id.mylocker);

                mywrite_course.setText(""+userInfoResult.result.course);
                mywrite_review.setText(""+(userInfoResult.result.coursecomment+userInfoResult.result.tripreviews));
                myLocker.setText(""+(userInfoResult.result.courselike+userInfoResult.result.triplike));



            }
        }catch(Exception e){
            e.printStackTrace();
            view = inflater.inflate(R.layout.mypage_nologin, container, false);

            loginOrLogout = (TextView) view.findViewById(R.id.settings_login);
            signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);

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

        String userId = "joo";

        Call<UserInfoResult> requestDriverApplyOwner = networkService.getUserInfo(userId);
        requestDriverApplyOwner.enqueue(new Callback<UserInfoResult>() {
            @Override
            public void onResponse(Call<UserInfoResult> call, Response<UserInfoResult> response) {
                if (response.isSuccessful()) {
                    userInfoResult = response.body();

                    Log.d(TAG, "onResponse: result joo: " + userInfoResult.result);
                    Log.d(TAG, "onResponse: message : " + userInfoResult.message);
                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
