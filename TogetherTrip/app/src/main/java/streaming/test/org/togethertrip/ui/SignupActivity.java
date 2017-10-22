package streaming.test.org.togethertrip.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.MessageResult;
import streaming.test.org.togethertrip.datas.RegisterDatas;
import streaming.test.org.togethertrip.network.NetworkService;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    final static String TAG = "SignupActivityLog";

    EditText edit_name;
    EditText edit_email;
    EditText edit_password;
    EditText edit_checkPassword;
    ImageButton btn_join;
    CheckBox cb1, cb2;

    String name;
    String email;
    String password;
    String checkPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_checkPassword = (EditText) findViewById(R.id.edit_checkPassword);
        btn_join = (ImageButton) findViewById(R.id.btn_join);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2); 

        btn_join.setOnClickListener(this);
    }

    @OnClick
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_join:
                if(edit_email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    edit_email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(edit_email.getText().toString()).matches()) {
                    Toast.makeText(getApplicationContext(), "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    edit_email.requestFocus();
                }else if(edit_password.length() == 0){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    edit_password.requestFocus();
                }else if(edit_checkPassword.length() == 0){
                    Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    edit_checkPassword.requestFocus();
                }else if(!edit_password.getText().toString().equals(edit_checkPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    edit_password.requestFocus();
                }else if(edit_name.length() == 0){
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    edit_name.requestFocus();
                }else if(!cb1.isChecked()){
                    Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }else if(!cb2.isChecked()){
                    Toast.makeText(getApplicationContext(), "개인정보 취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }else{ // 모든 항목 정상 입력시
                    RegisterDatas registerDatas = new RegisterDatas();
                    registerDatas.userid = edit_name.getText().toString();
                    registerDatas.email = edit_email.getText().toString();
                    registerDatas.password = edit_password.getText().toString();

                    NetworkService networkService = ApplicationController.getInstance().getNetworkService();
                    Call<MessageResult> requestRegister = networkService.requestSignup(registerDatas);
                    Log.d(TAG, "requestRegister: " + requestRegister);
                    requestRegister.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                            Log.d(TAG, "response.isSuccessful(): " + response.isSuccessful());
                            if (response.isSuccessful()) {
                                Log.d(TAG, "reponse.body: " + response.body().message);
                                if (response.body().message.equals("ok")) {
                                    Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(SignupActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(SignupActivity.this, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: ");
                        }
                    });
                }
                break;

        }
    }
}
