package streaming.test.org.togethertrip.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;

import streaming.test.org.togethertrip.R;

public class TouristSpotReviewListViewItem extends AppCompatActivity {

    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot_review_list_view_item);

    }



    //    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//        @Override
//        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//            // 저는 0개를 주기싫어서, 만약 1개미만이면 강제로 1개를 넣었습니다.
//            if (ratingbar.getRating()<1.0f){
//                ratingbar.setRating(1);
//            }
//        }
//    });
}