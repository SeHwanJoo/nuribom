package streaming.test.org.togethertrip.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import streaming.test.org.togethertrip.R;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Toast.makeText(this, "get test", Toast.LENGTH_SHORT).show();
    }
}
