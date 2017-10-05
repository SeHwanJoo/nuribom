package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;

public class TouristSpotDetail extends AppCompatActivity {
    final static String TAG = "TouristSpotDetailErr";

    ScrollView scrollView;
    ImageView imgView;
    ImageButton heartbtn, commentsbtn;
    TextView hearttxt, commentstxt;

    BitmapDrawable bitmap;

    Intent intent;
    String contentId;

    //@BindView(R.id.touristSpot_detail_commentsbtn) ImageButton commentsbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot_detail);
        intent = this.getIntent();
        contentId = intent.getStringExtra("contentId");

        Log.d(TAG, "onCreate: " + contentId);

        scrollView = (ScrollView)findViewById(R.id.touristSpot_detail_scroll);
        imgView = (ImageView)findViewById(R.id.touristSpot_detail_img);
        heartbtn = (ImageButton)findViewById(R.id.touristSpot_detail_heartbtn);
        commentsbtn = (ImageButton)findViewById(R.id.touristSpot_detail_commentsbtn);
        hearttxt = (TextView)findViewById(R.id.touristSpot_detail_hearttxt);
        commentstxt = (TextView)findViewById(R.id.touristSpot_detail_commentstxt);

        //스크롤바 사용기능 설정
        scrollView.setVerticalScrollBarEnabled(true);

        commentsbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TouristSpotReview.class);
                startActivity(intent);
            }
        });
    }

//    @OnClick(R.id.touristSpot_detail_commentsbtn)
//    public void commentClick(){
//
//    }

}