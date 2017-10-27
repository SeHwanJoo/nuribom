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
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import streaming.test.org.togethertrip.R;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivityLog";
    Activity activity = this;
    static Context context;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    @BindViews({R.id.button_home, R.id.button_spot, R.id.button_course, R.id.button_alarm, R.id.button_mypage})
    List<Button> tabButtonList;
//    @BindView(R.id.tv_main) TextView tv_main;
//    @BindView(R.id.btn_map) Button btn_map;
//    @BindView(R.id.btn_search) Button btn_search;
//    @BindView(R.id.real_searchBtn) Button real_searchBtn;
//    @BindView(R.id.home_searchBtn) Button home_searchBtn;
//    @BindView(R.id.edit_search) EditText edit_search;

    AlarmFragment alarm;
    CourseFragment course;
    HomeFragment home;
    MypageFragment mypage;
    SpotFragment spot;

    String receivedEmail, receivedProfileImg, receivedUserNickName, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //키보드 생성시 화면 밀림현상 없애기
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        context = this;

        Intent receivedIntent = getIntent();
        receivedEmail = receivedIntent.getStringExtra("email");
        receivedProfileImg = receivedIntent.getStringExtra("profileImg");
        receivedUserNickName = receivedIntent.getStringExtra("userNickName");
        token = receivedIntent.getStringExtra("token");

        //Fragment 생성
        alarm = new AlarmFragment();
        course = new CourseFragment(this, receivedUserNickName);
        home = new HomeFragment(this, receivedUserNickName);
        mypage = new MypageFragment(this, receivedEmail, receivedProfileImg, receivedUserNickName, token);
        spot = new SpotFragment(this, receivedUserNickName);

        //Fragment 추가
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        fragmentList.add(0, home);
        fragmentList.add(1, spot);
        fragmentList.add(2, course);
        fragmentList.add(3, alarm);
        fragmentList.add(4, mypage);

        //viewPager 연결
        mViewPager = (ViewPager) findViewById(R.id.container);
        Log.d(TAG, "onCreate: mViewPager: " + mViewPager);
        Log.d(TAG, "onCreate: mSectionPagerAdapter: " + mSectionsPagerAdapter);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //페이지가 선택되었을 때
            @Override
            public void onPageSelected(int position) {
                tabSelect(position);
                switch(position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mypage.checkLogin();

    }

    //페이지 선택 확인을 위한 어댑터
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
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
        public int getCount() {
            return fragmentList.size();
        }

    }

    //아래 탭에서 홈 클릭시
    @OnClick(R.id.button_home)
    public void homeTab(View view) {
        mViewPager.setCurrentItem(0);
        tabSelect(0);


    }

    //아래 탭에서 관광지 선택시
    @OnClick(R.id.button_spot)
    public void spotTab(View view) {
        mViewPager.setCurrentItem(1);
        tabSelect(1);

    }

    //아래 탭에서 코스 선택시
    @OnClick(R.id.button_course)
    public void courseTab(View view) {
        mViewPager.setCurrentItem(2);
        tabSelect(2);

    }

    //아래 탭에서 알람 선택시
    @OnClick(R.id.button_alarm)
    public void alarmTab(View view) {
        mViewPager.setCurrentItem(3);
        tabSelect(3);

    }

    //아래 탭에서 마이페이지 선택시
    @OnClick(R.id.button_mypage)
    public void mypageTab(View view) {
        mViewPager.setCurrentItem(4);
        tabSelect(4);

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
        switch(page){
            case 0: //home
                tabButtonList.get(0).setBackgroundResource(R.drawable.navi_home_on);
                tabButtonList.get(1).setBackgroundResource(R.drawable.navi_trip_off);
                tabButtonList.get(2).setBackgroundResource(R.drawable.navi_course_off);
                tabButtonList.get(3).setBackgroundResource(R.drawable.navi_alrarm_off);
                tabButtonList.get(4).setBackgroundResource(R.drawable.navi_mypage_off);
                break;
            case 1: //spot
                tabButtonList.get(0).setBackgroundResource(R.drawable.navi_home_off);
                tabButtonList.get(1).setBackgroundResource(R.drawable.navi_trip_on);
                tabButtonList.get(2).setBackgroundResource(R.drawable.navi_course_off);
                tabButtonList.get(3).setBackgroundResource(R.drawable.navi_alrarm_off);
                tabButtonList.get(4).setBackgroundResource(R.drawable.navi_mypage_off);
                break;
            case 2: //course
                tabButtonList.get(0).setBackgroundResource(R.drawable.navi_home_off);
                tabButtonList.get(1).setBackgroundResource(R.drawable.navi_trip_off);
                tabButtonList.get(2).setBackgroundResource(R.drawable.navi_course_on);
                tabButtonList.get(3).setBackgroundResource(R.drawable.navi_alrarm_off);
                tabButtonList.get(4).setBackgroundResource(R.drawable.navi_mypage_off);
                break;
            case 3: //alarm
                tabButtonList.get(0).setBackgroundResource(R.drawable.navi_home_off);
                tabButtonList.get(1).setBackgroundResource(R.drawable.navi_trip_off);
                tabButtonList.get(2).setBackgroundResource(R.drawable.navi_course_off);
                tabButtonList.get(3).setBackgroundResource(R.drawable.navi_alrarm_on);
                tabButtonList.get(4).setBackgroundResource(R.drawable.navi_mypage_off);
                break;
            case 4: //mypage
                tabButtonList.get(0).setBackgroundResource(R.drawable.navi_home_off);
                tabButtonList.get(1).setBackgroundResource(R.drawable.navi_trip_off);
                tabButtonList.get(2).setBackgroundResource(R.drawable.navi_course_off);
                tabButtonList.get(3).setBackgroundResource(R.drawable.navi_alrarm_off);
                tabButtonList.get(4).setBackgroundResource(R.drawable.navi_mypage_on);
                break;

        }

    }

    public static Context getContext(){
        return context;
    }



}
