package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import streaming.test.org.togethertrip.R;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{
    EditText input_email, input_password;
    ImageButton btn_login, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (ImageButton) findViewById(R.id.btn_login);
        btn_signup = (ImageButton) findViewById(R.id.btn_signup);


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
