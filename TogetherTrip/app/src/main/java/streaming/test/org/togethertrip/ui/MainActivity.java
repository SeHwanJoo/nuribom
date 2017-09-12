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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import streaming.test.org.togethertrip.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
    @BindView(R.id.real_searchBtn) Button real_searchBtn;
    @BindView(R.id.home_searchBtn) Button home_searchBtn;
    @BindView(R.id.edit_search) EditText edit_search;

    AlarmFragment alarm;
    CourseFragment course;
    HomeFragment home;
    MypageFragment mypage;
    SpotFragment spot;

    String search_keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //키보드 생성시 화면 밀림현상 없애기
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Fragment 생성
        alarm = new AlarmFragment();
        course = new CourseFragment(this);
        home = new HomeFragment();
        mypage = new MypageFragment();
        spot = new SpotFragment();

        //Fragment 추가
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        fragmentList.add(0, home);
        fragmentList.add(1, spot);
        fragmentList.add(2, course);
        fragmentList.add(3, alarm);
        fragmentList.add(4, mypage);

        //viewPager 연결
        mViewPager = (ViewPager) findViewById(R.id.container);
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
                        btn_map.setVisibility(View.GONE);
                        tv_main.setText("NAME");
                        btn_search.setVisibility(GONE);
                        real_searchBtn.setVisibility(GONE);
                        home_searchBtn.setVisibility(VISIBLE);
                        break;
                    case 1:
                        btn_map.setVisibility(VISIBLE);
                        tv_main.setText("관광지");
                        btn_search.setVisibility(VISIBLE);
                        break;
                    case 2:
                        btn_map.setVisibility(GONE);
                        tv_main.setText("코스");
                        btn_search.setVisibility(VISIBLE);
                        break;
                    case 3:
                        btn_map.setVisibility(GONE);
                        tv_main.setText("알람");
                        btn_search.setVisibility(GONE);
                        break;
                    case 4:
                        btn_map.setVisibility(GONE);
                        tv_main.setText("마이페이지");
                        btn_search.setVisibility(VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

        btn_map.setVisibility(GONE);
        tv_main.setText("NAME");
        edit_search.setVisibility(GONE);
        btn_search.setVisibility(GONE);
        real_searchBtn.setVisibility(GONE);
        home_searchBtn.setVisibility(VISIBLE);

    }

    //아래 탭에서 관광지 선택시
    @OnClick(R.id.button_spot)
    public void spotTab(View view) {
        mViewPager.setCurrentItem(1);
        tabSelect(1);

        btn_map.setVisibility(VISIBLE);
        tv_main.setText("관광지");
        btn_search.setVisibility(VISIBLE);
        real_searchBtn.setVisibility(GONE);
        home_searchBtn.setVisibility(GONE);
    }

    //아래 탭에서 코스 선택시
    @OnClick(R.id.button_course)
    public void courseTab(View view) {
        mViewPager.setCurrentItem(2);
        tabSelect(2);

        btn_map.setVisibility(GONE);
        tv_main.setText("코스");
        btn_search.setVisibility(VISIBLE);
        real_searchBtn.setVisibility(GONE);
        home_searchBtn.setVisibility(GONE);
    }

    //아래 탭에서 알람 선택시
    @OnClick(R.id.button_alarm)
    public void alarmTab(View view) {
        mViewPager.setCurrentItem(3);
        tabSelect(3);

        btn_map.setVisibility(GONE);
        tv_main.setText("알람");
        edit_search.setVisibility(GONE);
        btn_search.setVisibility(GONE);
        real_searchBtn.setVisibility(GONE);
        home_searchBtn.setVisibility(GONE);
    }

    //아래 탭에서 마이페이지 선택시
    @OnClick(R.id.button_mypage)
    public void mypageTab(View view) {
        mViewPager.setCurrentItem(4);
        tabSelect(4);

        btn_map.setVisibility(GONE);
        tv_main.setText("마이페이지");
        btn_search.setVisibility(GONE);
        real_searchBtn.setVisibility(GONE);
        home_searchBtn.setVisibility(GONE);
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

    //지도 아이콘 클릭시 작동하는 메소드
    @OnClick(R.id.btn_map)
    public void mapClick(){
        startActivity(new Intent(this, TouristSpotDetail.class));
    }

    //검색 버튼 클릭시 작동하는 메소드
    @OnClick({R.id.btn_search,R.id.real_searchBtn,R.id.home_searchBtn})
    public void searchClick(View view){
        switch(view.getId()){
            //
            case R.id.btn_search:
                edit_search.setVisibility(VISIBLE);
                tv_main.setVisibility(GONE);

                search_keyword = edit_search.getText().toString();
                Toast.makeText(activity, "검색어: " + search_keyword, Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_searchBtn:
                startActivity(new Intent(this, Home_SearchActivity.class));
                break;
            case R.id.real_searchBtn:
                /*
                TODO 검색버튼 클릭시 네트워킹
                 */

                break;
        }
    }
}
