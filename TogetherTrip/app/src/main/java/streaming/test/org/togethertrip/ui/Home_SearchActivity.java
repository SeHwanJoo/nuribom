package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.CourseListDatas;
import streaming.test.org.togethertrip.datas.CourseResult;
import streaming.test.org.togethertrip.datas.CourseSearchData;
import streaming.test.org.togethertrip.network.NetworkService;

public class Home_SearchActivity extends AppCompatActivity {
    EditText edit_homeSearch;
    ImageButton searchBtn, STTBtn, tripImgBtn, touristSpotImgBtn;
    View select_trip, select_tourist;
    TextView search_example;

    GridView CourselistView;
    CourseListAdapter courseListAdapter;
    ArrayList<CourseListDatas> courseListDatas;
    NetworkService networkService;
    String search_keyword;
    CourseSearchData courseSearchData;


    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);

        courseListAdapter = new CourseListAdapter(getApplicationContext(), null);

        edit_homeSearch = (EditText)findViewById(R.id.edit_homeSearch);

        searchBtn = (ImageButton)findViewById(R.id.searchBtn);
        STTBtn = (ImageButton)findViewById(R.id.STTBtn);
        tripImgBtn = (ImageButton)findViewById(R.id.tripImgBtn);
        touristSpotImgBtn = (ImageButton)findViewById(R.id.touristSpotImgBtn);

        select_trip = (View) findViewById(R.id.select_trip);
        select_tourist = (View)findViewById(R.id.select_tourist);

        search_example = (TextView) findViewById(R.id.search_example);

        CourselistView = (GridView)findViewById(R.id.course_gridView);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edittext받은 값 보내기

                //여행지, 코스 중 선택되어있는 것에 따라 검색하고 리스트 보여지게
                if(touristSpotImgBtn.getTag().equals("true")){
                    search_example.setVisibility(View.INVISIBLE);
                    CourselistView.setVisibility(View.VISIBLE);
                    courseSearch();
                }
                else if(tripImgBtn.getTag().equals("true")){

                }


            }
        });

        STTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tripImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_trip.setBackgroundColor(Color.parseColor("#263fe1"));
                select_tourist.setBackgroundColor(Color.parseColor("#dedede"));
                touristSpotImgBtn.setTag("false");

                CourselistView.setVisibility(View.INVISIBLE);
                //리스트 setVisibility, 통신
            }
        });

        touristSpotImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_trip.setBackgroundColor(Color.parseColor("#dedede"));
                select_tourist.setBackgroundColor(Color.parseColor("#263fe1"));

                touristSpotImgBtn.setTag("true");
                tripImgBtn.setTag("false");

            }
        });
    }

    //코스 검색
    public void courseSearch(){
//        Log.d(TAG, "search: " + search_keyword);
        search_keyword = edit_homeSearch.getText().toString();

        if(search_keyword == null) search_keyword = "go";
        else if(search_keyword.equals("")) search_keyword = "go";

        courseSearchData = new CourseSearchData();

        courseSearchData.userid = "joo";
        courseSearchData.keyword = search_keyword;

        networkService = ApplicationController.getInstance().getNetworkService();

        Call<CourseResult> requestCourseData = networkService.getCourseResult(courseSearchData.keyword,courseSearchData.userid);
        requestCourseData.enqueue(new Callback<CourseResult>() {

            @Override
            public void onResponse(Call<CourseResult> call, Response<CourseResult> response) {
                if (response.isSuccessful()) {
                    courseListDatas = response.body().result;
                    courseListAdapter = new CourseListAdapter(getApplicationContext(), courseListDatas);
                    CourselistView.setAdapter(courseListAdapter);
                }
            }

            @Override
            public void onFailure(Call<CourseResult> call, Throwable t) {
                Toast.makeText(activity, "통신실패", Toast.LENGTH_SHORT).show();

            }

        });

    }
}
