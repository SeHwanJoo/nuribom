package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import streaming.test.org.togethertrip.datas.DetailCourseResult;
import streaming.test.org.togethertrip.network.NetworkService;

import static android.content.ContentValues.TAG;


public class DetailCourseActivity extends AppCompatActivity {
    Intent getIntent;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    ArrayList<CourseListDatas> courseListDatas;
    Context context;

    DetailCourseFragment detailCourseFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course3);
        getIntent = getIntent();
        Log.d(TAG, "clickItem: 진입");
        DetailCourseInfo detailCourseInfo = new DetailCourseInfo();
        detailCourseInfo.userid = "joo";
        detailCourseInfo.courseid = getIntent.getExtras().getInt("courseid");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Log.d(TAG, "clickItem: userid / courseId : " +  detailCourseInfo.userid +
                " / " + String.valueOf( detailCourseInfo.courseid));


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


