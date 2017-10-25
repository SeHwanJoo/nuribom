package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
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
import streaming.test.org.togethertrip.datas.DetailSpotListClickResponse;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResult;
import streaming.test.org.togethertrip.datas.DetailSpotListDatas;
import streaming.test.org.togethertrip.datas.OtherInfo;
import streaming.test.org.togethertrip.datas.SearchData;
import streaming.test.org.togethertrip.datas.TouristSpotSearchList;
import streaming.test.org.togethertrip.datas.TouristSpotSearchResult;
import streaming.test.org.togethertrip.network.NetworkService;

import static streaming.test.org.togethertrip.ui.MainActivity.context;

public class Home_SearchActivity extends AppCompatActivity {
    static final String TAG = "SpotFragmentLog";
    EditText edit_homeSearch;
    ImageButton searchBtn, STTBtn, tripImgBtn, touristSpotImgBtn;
    View select_trip, select_tourist;
    TextView search_example;

    NetworkService networkService;
    String search_keyword;

    GridView CourselistView;
    CourseListAdapter courseListAdapter;
    ArrayList<CourseListDatas> courseListDatas;
    CourseSearchData courseSearchData;

    ListView touristSpot_listView;
    SearchData searchData;
    ArrayList<TouristSpotSearchList> spotResultListDatas;
    TouristSpot_ListViewAdapter adapter;
    DetailSpotListClickResponse detailSpotListClickResponse;
    Intent detailIntent;
    String firstImgUri;
    OtherInfo otherInfo;
    String addr;
    String nickName;


    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        Intent intent = getIntent();
        nickName = intent.getStringExtra("nickName");

        courseListAdapter = new CourseListAdapter(getApplicationContext(), null);

        edit_homeSearch = (EditText) findViewById(R.id.edit_homeSearch);

        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        STTBtn = (ImageButton) findViewById(R.id.STTBtn);
        tripImgBtn = (ImageButton) findViewById(R.id.tripImgBtn);
        touristSpotImgBtn = (ImageButton) findViewById(R.id.touristSpotImgBtn);

        select_trip = (View) findViewById(R.id.select_trip);
        select_tourist = (View) findViewById(R.id.select_tourist);

        search_example = (TextView) findViewById(R.id.search_example);

        CourselistView = (GridView) findViewById(R.id.course_gridView);
        touristSpot_listView = (ListView) findViewById(R.id.touristSpot_listView);

        touristSpot_listView.setOnItemClickListener(itemClickListener);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edittext받은 값 보내기

                //여행지, 코스 중 선택되어있는 것에 따라 검색하고 리스트 보여지게
                if (touristSpotImgBtn.getTag().equals("true")) {
                    search_example.setVisibility(View.INVISIBLE);
                    CourselistView.setVisibility(View.VISIBLE);
                    courseSearch();
                } else if (tripImgBtn.getTag().equals("true")) {
                    search_example.setVisibility(View.INVISIBLE);
                    touristSpot_listView.setVisibility(View.VISIBLE);
                    search();
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

                tripImgBtn.setTag("true");
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

                touristSpot_listView.setVisibility(View.INVISIBLE);

            }
        });
    }

    //코스 검색
    public void courseSearch() {
//        Log.d(TAG, "search: " + search_keyword);
        search_keyword = edit_homeSearch.getText().toString();

        if (search_keyword == null) search_keyword = "go";
        else if (search_keyword.equals("")) search_keyword = "go";

        courseSearchData = new CourseSearchData();

        courseSearchData.userid = nickName;
        courseSearchData.keyword = search_keyword;

        networkService = ApplicationController.getInstance().getNetworkService();

        Call<CourseResult> requestCourseData = networkService.getCourseResult(courseSearchData.keyword, courseSearchData.userid);
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


    //관광지 검색 메소드
    public void search() {
        search_keyword = edit_homeSearch.getText().toString();

        if (search_keyword == null) search_keyword = "광화문";
        else if (search_keyword.equals("")) search_keyword = "광화문";

        Log.d(TAG, "search: " + search_keyword);
        searchData = new SearchData();

        searchData.userid = nickName;
        searchData.keyword = search_keyword;

        networkService = ApplicationController.getInstance().getNetworkService();

        Call<TouristSpotSearchResult> requestDriverApplyOwner = networkService.searchTouristSpot(searchData);
        requestDriverApplyOwner.enqueue(new Callback<TouristSpotSearchResult>() {
            @Override
            public void onResponse(Call<TouristSpotSearchResult> call, Response<TouristSpotSearchResult> response) {
                if (response.isSuccessful()) {
                    spotResultListDatas = response.body().result;
                    //    Log.v("YG", spotResultListDatas.get(0).Tripinfo.toString());
                    Log.d(TAG, "onResponse: search() 성공");

                    adapter = new TouristSpot_ListViewAdapter(context, spotResultListDatas);
                    touristSpot_listView.setAdapter(adapter);

                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<TouristSpotSearchResult> call, Throwable t) {
                //검색시 통신 실패
                Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

        // detail 네트워킹
    public void clickItem(View view, int position){
        Log.d(TAG, "clickItem: 진입");
        DetailSpotListDatas detailSpotListDatas = new DetailSpotListDatas();
        detailSpotListDatas.contentid = spotResultListDatas.get(position).tripinfo.contentid;
        detailSpotListDatas.contenttypeid = spotResultListDatas.get(position).tripinfo.contenttypeid;

        Log.d(TAG, "clickItem: contentId / contentTypeId : " + detailSpotListDatas.contentid +
                " / " + detailSpotListDatas.contenttypeid);

        /*
        * TODO 나중에 userid는 받아와야함!
         */
        detailSpotListDatas.userid = nickName;

        addr = spotResultListDatas.get(position).tripinfo.addr1;
        Log.d(TAG, "onResponse: addr: " + addr);

        NetworkService list_networkService = ApplicationController.getInstance().getNetworkService();
        Log.d(TAG, "clickItem: networkService: " + list_networkService);
        Call<DetailSpotListClickResult> requestDetailSpotList = list_networkService.clickDetailSpotList(detailSpotListDatas);
        requestDetailSpotList.enqueue(new Callback<DetailSpotListClickResult>() {
            @Override
            public void onResponse(Call<DetailSpotListClickResult> call, Response<DetailSpotListClickResult> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: detail 통신");

                    detailSpotListClickResponse = response.body().tripinfo;
                    otherInfo = response.body().otherinfo;

                    detailIntent = new Intent(context, TouristSpotDetail.class);
                    Intent firstImgIntent = new Intent(context, SpotDetailImgFragment.class);
                    detailIntent.putExtra("stringAddr", addr);
                    detailIntent.putExtra("detailCommon", detailSpotListClickResponse.detailCommon);
                    detailIntent.putExtra("detailIntro", detailSpotListClickResponse.detailIntro);
                    detailIntent.putParcelableArrayListExtra("detailInfo", detailSpotListClickResponse.detailInfo);
                    detailIntent.putExtra("detailWithTour", detailSpotListClickResponse.detailWithTour);
                    detailIntent.putParcelableArrayListExtra("detailImage", detailSpotListClickResponse.detailImage);
                    detailIntent.putExtra("otherInfo", otherInfo);
                    firstImgIntent.putExtra("firstImgUri", firstImgUri);

                    Log.d(TAG, "onResponse: detailInfo: " + detailSpotListClickResponse.detailInfo);
                    Log.d(TAG, "onResponse: detailImage: " + detailSpotListClickResponse.detailImage);

                    startActivity(detailIntent);

                } else {
                    Log.d(TAG, "onResponse: clickList response is not success");
                }
            }
            @Override
            public void onFailure(Call<DetailSpotListClickResult> call, Throwable t) {
                //검색시 통신 실패
                Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            clickItem(view, position);
        }
    };
}

