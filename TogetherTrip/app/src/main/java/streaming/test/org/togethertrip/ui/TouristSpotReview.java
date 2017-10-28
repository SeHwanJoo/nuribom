package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.TouristSpotReviewListData;
import streaming.test.org.togethertrip.datas.TouristSpotReviewResult;
import streaming.test.org.togethertrip.network.NetworkService;

public class TouristSpotReview extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private ArrayList<TouristSpotReviewListData> mDatas;
    private LinearLayoutManager mLayoutManager;
    private TouristSpotReviewRecyclerAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private TouristSpotReviewListData data;
    NetworkService service;
    Button change;
    FloatingActionButton reviewbtn;

    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    Intent getContent;

    String contentId, contentTypeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot_review);
        Log.i("Tag", "메인");

        getContent = this.getIntent();
        contentId = getContent.getStringExtra("contentId");
        contentTypeId = getContent.getStringExtra("contentTypeId");
        ////////////////////////서비스 객체 초기화////////////////////////
        service = ApplicationController.getInstance().getNetworkService();

        ////////////////////////뷰 객체 초기화////////////////////////
        reviewbtn = (FloatingActionButton) findViewById(R.id.reviewFab);
        recyclerView = (RecyclerView)findViewById(R.id.touristSpot_recyclerView);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.RefreshLayout);
        recyclerView.setHasFixedSize(true);
        refreshLayout.setOnRefreshListener(this);
        ////////////////////////레이아웃 매니저 설정////////////////////////
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        ////////////////////////각 배열에 모델 개체를 가지는 ArrayList 초기화////////////////////////
        mDatas = new ArrayList<TouristSpotReviewListData>();

        ////////////////////////리사이클러 뷰와 어뎁터 연동////////////////////////
        ////////////////////////파라미터로 위의 ArrayList와 클릭이벤트////////////////////////
        adapter = new TouristSpotReviewRecyclerAdapter(mDatas, getApplicationContext() ,clickEvent);
        recyclerView.setAdapter(adapter);


        ////////////////////////리스트 목록 추가 버튼에 리스너 설정////////////////////////
        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TouristSpotReviewWrite.class);
                intent.putExtra("contentId", contentId);
                intent.putExtra("contentTypeId", contentTypeId);
                startActivity(intent);
            }
        });

        /*
          OnCreate()- 생명주기 내의 통신
         */
        Call<TouristSpotReviewResult> requestMainData = service.getMainResult(contentId);
        requestMainData.enqueue(new Callback<TouristSpotReviewResult>() {
            @Override
            public void onResponse(Call<TouristSpotReviewResult> call, Response<TouristSpotReviewResult> response) {

                if (response.isSuccessful()) {
                    mDatas = response.body().result;
                    adapter.setAdapter(mDatas);
                }
            }

            @Override
            public void onFailure(Call<TouristSpotReviewResult> call, Throwable t) {
                Log.i("리사이클러뷰 불러오기 실패", t.getMessage());
            }
        });
//
//        change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "난 후기를 쓸테야", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, TouristSpotReviewWrite.class);
//                startActivity(intent);
//            }
//        });

    }

    /*
    onRestart()를 오버라이드하여 onPause -> onRestart 시
    리스트를 갱신하는 ListReload 메소드를 실행!!
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        ListReload();
    }

    /*
    리스트를 당기면 갱신되는 메소드입니다 사용하기 위해서
    implements SwipeRefreshLayout.OnRefreshListener 와
     xml에서 리스트를 감싸는 SwipeRefreshLayout 가 필요합니다!!
     */
    @Override
    public void onRefresh() {
        ListReload();
        refreshLayout.setRefreshing(false);
        Toast.makeText(getApplicationContext(), "페이지 리로드", Toast.LENGTH_SHORT).show();
    }

    /*
    리스트를 갱신하는 메소드입니다.
     */
    public void ListReload() {
        Call<TouristSpotReviewResult> requestMainData = service.getMainResult("trips");
        requestMainData.enqueue(new Callback<TouristSpotReviewResult>() {
            @Override
            public void onResponse(Call<TouristSpotReviewResult> call, Response<TouristSpotReviewResult> response) {

                if (response.isSuccessful()) {
                    if (response.body().message.equals("success")) {
                        mDatas = response.body().result;
                        adapter.setAdapter(mDatas);
                    }

                }
            }

            @Override
            public void onFailure(Call<TouristSpotReviewResult> call, Throwable t) {

            }
        });
    }

    ////////////////////////클릭 이벤트 정의////////////////////////
    public View.OnClickListener clickEvent = new View.OnClickListener() {
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildPosition(v);
            //           Toast.makeText(getApplication(), itemPosition + "번째 후기입니다.", Toast.LENGTH_SHORT).show();//            String tempId = mDatas.get(itemPosition).userid;
//            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
//            intent.putExtra("id", String.valueOf(tempId));
//            startActivity(intent);
        }
    };

    ////////////////////////취소 버튼을 오버라이드////////////////////////
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        /**
         * Back키 두번 연속 클릭 시 앱 종료
         */
        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }
}
