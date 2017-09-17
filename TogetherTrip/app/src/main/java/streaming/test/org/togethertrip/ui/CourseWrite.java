package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import streaming.test.org.togethertrip.R;

import static streaming.test.org.togethertrip.R.id.nextbtn;

public class CourseWrite extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_write);
        button = (Button) findViewById(nextbtn);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseWrite2.class);
                startActivity(intent);
            }
        });
    }
}
