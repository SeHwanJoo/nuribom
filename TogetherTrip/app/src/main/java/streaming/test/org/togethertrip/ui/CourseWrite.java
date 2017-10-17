package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.CourseWriteDatas;
import streaming.test.org.togethertrip.datas.CourseWriteResult;
import streaming.test.org.togethertrip.network.NetworkService;

import static streaming.test.org.togethertrip.R.id.courseTitle;

public class CourseWrite extends AppCompatActivity implements CourseWriteFragment.DataSetListner{
    Button okbtn;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    CourseWriteFragment courseWriteFragment;
    CourseWriteFragment2 courseWriteFragment2;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    FloatingActionButton nextfab;
    CourseWriteDatas courseWriteDatas;

    //번들로 받을 데이터
    Uri uri;
    String title;
    String category;

    NetworkService networkService;
    Bundle course1Bundle;

    static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_write_viewpager);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        courseWriteFragment = new CourseWriteFragment(this);
        courseWriteFragment2 = new CourseWriteFragment2(this);
        fragmentList.add(position, courseWriteFragment);
        position++;

        okbtn = (Button)findViewById(R.id.okbtn);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //fab 버튼누르면 작성창
        nextfab = (FloatingActionButton) findViewById(R.id.nextfab);
        nextfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getBaseContext(),CourseWrite.class));

                fragmentList.add(position, new CourseWriteFragment2(getBaseContext()));
                position++;
                mSectionsPagerAdapter.notifyDataSetChanged();

                mViewPager.setClipToPadding(false);
                mViewPager.setPadding(0,0,0,0);
                mViewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -10);
                mViewPager.setPageMargin(getResources().getDisplayMetrics().heightPixels/ -10);
                mViewPager.setCurrentItem(position,true);

                Toast.makeText(CourseWrite.this, "받은 데이터: " + title+uri+category, Toast.LENGTH_SHORT).show();
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bundleeeee", "onItemClick: detailIntent: " + title);

                Toast.makeText(CourseWrite.this, "받은 데이터: " + title+uri+category, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CourseWrite.this,data.title, Toast.LENGTH_SHORT).show();
//                okNetwork();
            }
        });

//        Bundle bundle = getArg.getString("Obj");
//        int id = bundle.getInt("id");
//        Item item = (Item) bundle.getSerializable("item");



    }

    public void okNetwork(){

        courseWriteDatas.main.title="abc";
        Log.d("gggggg", "search: searchData.keyword: " + courseWriteDatas.main.title);


        Call<CourseWriteResult> courseWriteTitle = networkService.writeCourse(courseWriteDatas);
        courseWriteTitle.enqueue(new Callback<CourseWriteResult>() {
            @Override
            public void onResponse(Call<CourseWriteResult> call, Response<CourseWriteResult> response) {
                if (response.isSuccessful()) {
                    /*
                    TODO 잘 실행 되는지?
                     */
                    Log.d("coursetitle", "onResponse: search: " + courseTitle);

                } else {

                }
            }
            @Override
            public void onFailure(Call<CourseWriteResult> call, Throwable t) {
                //검색시 통신 실패
            }
        });
    }



    //페이지 선택 확인을 위한 어댑터
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        position = 0;
        finish();
    }

    @Override
    public void FirstFragmentDataSet(Uri uri, String category, String title) {
        this. uri = uri;
        this.category = category;
        this.title = title;

    }


}