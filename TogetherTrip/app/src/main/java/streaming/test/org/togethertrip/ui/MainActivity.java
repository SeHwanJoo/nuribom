package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import streaming.test.org.togethertrip.R;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    Activity activity = this;
    Context context =this;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    @BindViews({R.id.button_home, R.id.button_spot, R.id.button_course, R.id.button_alarm, R.id.button_mypage})
    List<Button> tabButtonList;
    @BindView(R.id.tv_main) TextView tv_main;
    @BindView(R.id.btn_map) Button btn_map;
    @BindView(R.id.btn_search) Button btn_search;

    AlarmFragment alarm;
    CourseFragment course;
    HomeFragment home;
    MypageFragment mypage;
    SpotFragment spot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        alarm = new AlarmFragment();
        course = new CourseFragment();
        home = new HomeFragment();
        mypage = new MypageFragment();
        spot = new SpotFragment();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        fragmentList.add(0, home);
        fragmentList.add(1, spot);
        fragmentList.add(2, course);
        fragmentList.add(3, alarm);
        fragmentList.add(4, mypage);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabSelect(position);
                switch(position){
                    case 0:
                        btn_map.setVisibility(View.GONE);
                        tv_main.setText("NAME");
                        btn_search.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        btn_map.setVisibility(View.VISIBLE);
                        tv_main.setText("관광지");
                        btn_search.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        btn_map.setVisibility(GONE);
                        tv_main.setText("코스");
                        btn_search.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        btn_map.setVisibility(GONE);
                        tv_main.setText("알람");
                        btn_search.setVisibility(GONE);
                        break;
                    case 4:
                        btn_map.setVisibility(GONE);
                        tv_main.setText("마이페이지");
                        btn_search.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                return fragmentList.get(position);
            } catch (Exception e) {
                Log.e("DriverActivity", "PagerAdapter", e);
                return new HomeFragment();
            }
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    @OnClick(R.id.button_home)
    public void homeTab(View view) {
        mViewPager.setCurrentItem(0);
        tabSelect(0);
        btn_map.setVisibility(GONE);
        tv_main.setText("NAME");
        btn_search.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.button_spot)
    public void spotTab(View view) {
        mViewPager.setCurrentItem(1);
        tabSelect(1);
//        mViewPager.removeView(course.getView());

        btn_map.setVisibility(View.VISIBLE);
        tv_main.setText("관광지");
        btn_search.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.button_course)
    public void courseTab(View view) {
        mViewPager.setCurrentItem(2);
        tabSelect(2);

        btn_map.setVisibility(GONE);
        tv_main.setText("코스");
        btn_search.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.button_alarm)
    public void alarmTab(View view) {
        mViewPager.setCurrentItem(3);
        tabSelect(3);

        btn_map.setVisibility(GONE);
        tv_main.setText("알람");
        btn_search.setVisibility(GONE);
    }

    @OnClick(R.id.button_mypage)
    public void mypageTab(View view) {
        mViewPager.setCurrentItem(4);
        tabSelect(4);

        btn_map.setVisibility(GONE);
        tv_main.setText("마이페이지");
        btn_search.setVisibility(GONE);
    }

    /**
     * 페이지 이동에 따른 탭 버튼들의 이미지들에 대한 처리
     * @param page
     */
    public void tabSelect(int page){
        mViewPager.setCurrentItem(page);
        for(Button btn : tabButtonList){
            btn.setSelected(false);
            btn.setTextColor(Color.GRAY);

        }
        tabButtonList.get(page).setSelected(true);
        tabButtonList.get(page).setTextColor(Color.BLACK);

    }

    @OnClick(R.id.btn_map)
    public void mapClick(){
        startActivity(new Intent(this, TouristSpotDetail.class));
    }
}
