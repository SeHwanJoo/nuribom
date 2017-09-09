package streaming.test.org.togethertrip.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import streaming.test.org.togethertrip.R;

public class TouristSpotReview extends AppCompatActivity {
    @BindView(R.id.reviewbtn)Button reviewWrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot_review);

        ButterKnife.bind(this);

    }
    @OnClick(R.id.reviewbtn)
    public void reviewbtnClick(){

    }
}