package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import streaming.test.org.togethertrip.R;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{
    EditText input_email, input_password;
    Button btn_login, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                break;
            case R.id.btn_signup:
                finish();
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }
}
