package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.CourseListDatas;
import streaming.test.org.togethertrip.datas.DetailCourseDatas;
import streaming.test.org.togethertrip.datas.DetailCourseInfo;
import streaming.test.org.togethertrip.datas.like.AddLikeInfo;
import streaming.test.org.togethertrip.network.NetworkService;
import streaming.test.org.togethertrip.ui.comment.CommentActivity;

import static android.content.ContentValues.TAG;


public class DetailCourseActivity extends AppCompatActivity {
    Intent getIntent;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    AddLikeInfo addLikeInfo;
    ArrayList<CourseListDatas> courseListDatas;
    TextView CommentCount, heartCount;
    Context context;
    NetworkService service;
    LinearLayout commentLayout;
    ImageButton comment, heart;
    DetailCourseFragment detailCourseFragment;
    DetailCourseDatas.Page page;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course3);
        commentLayout = (LinearLayout)findViewById(R.id.commentLayout);
        comment = (ImageButton)findViewById(R.id.touristSpot_detail_commentsbtn);
        heart = (ImageButton)findViewById(R.id.course_detail_heartbtn);
        heartCount = (TextView)findViewById(R.id.course_detail_heartCount);
        CommentCount = (TextView)findViewById(R.id.comment_detail_commentsCount);
        getIntent = getIntent();
        addLikeInfo = new AddLikeInfo();
        service = ApplicationController.getInstance().getNetworkService();

        final DetailCourseInfo detailCourseInfo = new DetailCourseInfo();
        detailCourseInfo.userid = getIntent.getExtras().getString("userid");
        detailCourseInfo.courseid = getIntent.getExtras().getInt("courseid");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        CommentCount.setText(String.valueOf(getIntent.getExtras().getInt("commentcount")));
        heartCount.setText(String.valueOf(getIntent.getExtras().getInt("heartCount")));
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Log.d(TAG, "clickItem: userid / courseId : " +  detailCourseInfo.userid +
                " / " + String.valueOf( detailCourseInfo.courseid));

        //댓글 창으로 넘어가는 부분
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(getApplicationContext(), CommentActivity.class);
                commentIntent.putExtra("courseid",detailCourseInfo.courseid);
                startActivity(commentIntent);
            }
        });


        // 코스 디테일 프레그먼트 통신
        NetworkService list_networkService = ApplicationController.getInstance().getNetworkService();
        Log.d(TAG, "clickItem: networkService: " + list_networkService);
        Call<DetailCourseDatas> requestDetailCourseList = list_networkService.clickDetailCourseList(detailCourseInfo);
        requestDetailCourseList.enqueue(new Callback<DetailCourseDatas>() {
            @Override
            public void onResponse(Call<DetailCourseDatas> call, Response<DetailCourseDatas> response) {
                if (response.isSuccessful()) {
                    detailCourseFragment = new DetailCourseFragment(DetailCourseActivity.this,response.body().result);
                    fragmentList.add(0, detailCourseFragment);

                    int i = 1;
                    for(DetailCourseDatas.Page page : response.body().page){
                        DetailCourseFragment2 detailCourseFragment2 = new DetailCourseFragment2(DetailCourseActivity.this, page);
                        fragmentList.add(i,detailCourseFragment2);
                        Bundle bundle = new Bundle();
                        bundle.putInt("courseNumber", i);
                        detailCourseFragment2.setArguments(bundle);
                        i++;
                    }

                    mSectionsPagerAdapter.notifyDataSetChanged();
                    mViewPager.setClipToPadding(false);
                    mViewPager.setPadding(0,0,0,0);
                    mViewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -10);
                    mViewPager.setPageMargin(getResources().getDisplayMetrics().heightPixels/ -10);
                    mViewPager.setCurrentItem(0,true);

                } else {
                    Log.d(TAG, "onResponse: clickList response is not success");
                }
            }
            @Override
            public void onFailure(Call<DetailCourseDatas> call, Throwable t) {
                //검색시 통신 실패
                Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });
//
//        //답변에서 하트 눌렀을 때, 하트 색바꾸기
//        heart.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                addLikeInfo = new AddLikeInfo();
//                addLikeInfo.userid = getIntent.getExtras().getString("userid");
//                addLikeInfo.courseid = getIntent.getExtras().getInt("courseid");
//
//                Call<AddLikeResult> addLike = service.addLikeResult(addLikeInfo);
//                addLike.enqueue(new Callback<AddLikeResult>() {
//                    @Override
//                    public void onResponse(Call<AddLikeResult> call, Response<AddLikeResult> response) {
//
//                        if (response.isSuccessful()) {
//                            if (response.body().result.message.equals("like")) {
//                                Toast.makeText(context, "좋아요", Toast.LENGTH_SHORT).show();
//                                heart.setBackgroundResource(R.drawable.course_main_heart_color);
//                                heartCount.setText(String.valueOf(response.body().result.likecount));
//                                Log.i("likecount",String.valueOf(response.body().result.likecount));
//                            }
//                            else if (response.body().result.message.equals("unlike")) {
//                                Toast.makeText(context, "좋아요 취소", Toast.LENGTH_SHORT).show();
//                                heart.setBackgroundResource(R.drawable.course_main_heart_line);
////                              iv_heart.setBackgroundResource(R.drawable.course_main_heart_line);
//                                heartCount.setText(String.valueOf(response.body().result.likecount));
//                            }
//                            mSectionsPagerAdapter.notifyDataSetChanged();
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<AddLikeResult> call, Throwable t) {
//                        //Toast.makeText(context, "에러가 발생했습니다. 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });

    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            try {
                return fragmentList.get(position);
            } catch (Exception e) {
                return new HomeFragment();
            }
        }

        @Override
        public float getPageWidth(int position) {
            if(position == 0) return 1f;
            else return 0.99999999f;

        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

}

