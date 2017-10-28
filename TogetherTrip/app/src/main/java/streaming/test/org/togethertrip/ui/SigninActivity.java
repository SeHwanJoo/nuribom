package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.login.LoginDatas;
import streaming.test.org.togethertrip.datas.login.LoginEchoResult;
import streaming.test.org.togethertrip.datas.login.LoginResult;
import streaming.test.org.togethertrip.network.NetworkService;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SigninActivityLog";
    Activity activity;

    EditText input_email, input_password;
    ImageButton btn_login, btn_signup;
    String email, password;

    LoginResult loginResult;
    LoginEchoResult loginEchoResult;
    LoginDatas loginDatas;

    SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        activity = this;

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (ImageButton) findViewById(R.id.btn_login);
        btn_signup = (ImageButton) findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                if(input_email.length()==0){
                    Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    input_email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(input_email.getText().toString()).matches()){
                    Toast.makeText(this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                }else if(input_password.length()==0){
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    loginDatas = new LoginDatas();
                    loginDatas.email = input_email.getText().toString();
                    loginDatas.password = input_password.getText().toString();
                    loginDatas.token = FirebaseInstanceId.getInstance().getToken();

                    requestSignin(loginDatas);

                }

                break;
            case R.id.btn_signup:
                finish();
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }

    //로그인 네트워크
    public void requestSignin(LoginDatas loginDatas){
        NetworkService networkService = ApplicationController.getInstance().getNetworkService();

        Call<LoginResult> requestLogin = networkService.requestSignin(loginDatas);
        requestLogin.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "reponse.body: " + response.body().message);
                    if (response.body().message.equals("ok")) { // 로그인 성공
                        loginEchoResult = response.body().result;
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("email", loginEchoResult.email);
                        intent.putExtra("password", loginEchoResult.password);
                        intent.putExtra("profileImg", loginEchoResult.img);
                        intent.putExtra("userNickName", loginEchoResult.userid);
                        intent.putExtra("token", loginEchoResult.token);

                        //앞서 쌓여있던 NoLogin된 메인 액티비티 제거하라는 플래그
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        loginInfo = getSharedPreferences("loginSetting", 0);
                        SharedPreferences.Editor editor = loginInfo.edit();
                        editor.putString("email", loginEchoResult.email);
                        editor.putString("password", loginEchoResult.password);
                        editor.putString("nickname", loginEchoResult.userid);
                        editor.commit();

//                        intent.putExtra("token", loginEchoResult.token);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "아이디 혹은 암호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
