package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.sacot41.scviewpager.DotsView;
import com.dev.sacot41.scviewpager.SCViewPager;
import com.dev.sacot41.scviewpager.SCViewPagerAdapter;

import java.util.ArrayList;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.ui.home_ad_fragment.FirstAdFragment;
import streaming.test.org.togethertrip.ui.home_ad_fragment.SecondAdFragment;
import streaming.test.org.togethertrip.ui.home_ad_fragment.ThirdAdFragment;

/**
 * Created by taehyung on 2017-09-06.
 * 코드상 폰트값이 바뀌는 부분은 일단 주석처리! 속도가 너무느려서!
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    //상단 광고 페이지 수
    private final int NUM_PAGES = 3;
    View view;

    Activity activity;
    Context context;
    ImageButton filter_all,filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;
    TextView recommend_spot, recommend_course;
    View select_touristSpot, select_course;
    ImageButton home_searchBtn;

    ImageView recommend_spot_img_first,recommend_spot_img_second, recommend_spot_img_third;

    String nickName;

    //상단 광고 부분 viewPager, dot 뷰
    SCViewPager mViewPager;
    mPagerAdapter mPageAdapter;
    DotsView mDotsView;

    FirstAdFragment faf;
    SecondAdFragment saf;
    ThirdAdFragment taf;

    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

    public HomeFragment(){

    }

    public HomeFragment(Activity activity, String nickName){
        this.activity = activity;
        this.nickName = nickName;
        context = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_container, container, false);

        recommend_spot_img_first = (ImageView) view.findViewById(R.id.recommend_spot_img_first);
        recommend_spot_img_second = (ImageView) view.findViewById(R.id.recommend_spot_img_second);
        recommend_spot_img_third = (ImageView )view.findViewById(R.id.recommend_spot_img_third);

        /*TODO minSDK 잠시내려서 주석 21버전 이상에서 사용 가능
        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.border_round);
        recommend_spot_img_first.setBackground(drawable);
        recommend_spot_img_first.setClipToOutline(true);*/

        //지도 기반 검색시 사용할 textView(주변 시설 검색)
        TextView tv_nearSearch = (TextView) view.findViewById(R.id.tv_nearSearch);

        mViewPager = (SCViewPager) view.findViewById(R.id.home_viewpager_ad);
        mDotsView = (DotsView) view.findViewById(R.id.home_dotsview);
        mDotsView.setDotRessource(R.drawable.dot_selected, R.drawable.dot_unselected);
        mDotsView.setNumberOfPage(NUM_PAGES);

        faf = new FirstAdFragment();
        saf = new SecondAdFragment();
        taf = new ThirdAdFragment();

        mPageAdapter = new mPagerAdapter(getFragmentManager());
        mPageAdapter.setNumberOfPage(3);
        fragmentList.add(0, faf);
        fragmentList.add(1, saf);
        fragmentList.add(2, taf);

        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDotsView.selectDot(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //필터링을 위한 뷰 연결
        filter_all = (ImageButton) view.findViewById(R.id.filter_all);
        filter_wheelchairs = (ImageButton) view.findViewById(R.id.filter_wheelchairs);
        filter_bathroom = (ImageButton) view.findViewById(R.id.filter_bathroom);
        filter_parkinglot = (ImageButton) view.findViewById(R.id.filter_parkinglot);
        filter_elevator = (ImageButton) view.findViewById(R.id.filter_elevator);

        //필터링을 위한 리스너 연결
        filter_all.setOnClickListener(this);
        filter_wheelchairs.setOnClickListener(this);
        filter_bathroom.setOnClickListener(this);
        filter_parkinglot.setOnClickListener(this);
        filter_elevator.setOnClickListener(this);

        //추천 여행지, 추천 코스 뷰 연결
        recommend_spot = (TextView) view.findViewById(R.id.recommend_spot);
        recommend_course = (TextView)view.findViewById(R.id.recommend_course);

        select_touristSpot = (View) view.findViewById(R.id.select_touristSpot);
        select_course = (View)view.findViewById(R.id.select_course);

        home_searchBtn = (ImageButton) view.findViewById(R.id.home_searchBtn);

        home_searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Home_SearchActivity.class);
                intent.putExtra("nickName", nickName);
                startActivity(intent);
            }
        });

        recommend_spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_touristSpot.setBackgroundColor(Color.parseColor("#263fe1"));
                select_course.setBackgroundColor(Color.parseColor("#dedede"));
                recommend_spot.setTextColor(Color.parseColor("#1D1D1D"));
                recommend_course.setTextColor(Color.parseColor("#8C8C8C"));
                /*recommend_spot.setTypeface(Typeface.createFromAsset(context.getAssets()
                                                , "fonts/NotoSansCJKkr-Medium.otf"));
                recommend_course.setTypeface(Typeface.createFromAsset(context.getAssets()
                        , "fonts/NotoSansCJKkr-Regular.otf"));*/

                recommend_spot_img_first.setImageResource(R.drawable.home_first2);
                recommend_spot_img_second.setImageResource(R.drawable.home_second);
                recommend_spot_img_third.setImageResource(R.drawable.home_third);
            }
        });

        recommend_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_touristSpot.setBackgroundColor(Color.parseColor("#dedede"));
                select_course.setBackgroundColor(Color.parseColor("#263fe1"));
                recommend_spot.setTextColor(Color.parseColor("#8C8C8C"));
                recommend_course.setTextColor(Color.parseColor("#1D1D1D"));
                /*recommend_spot.setTypeface(Typeface.createFromAsset(context.getAssets()
                        , "fonts/NotoSansCJKkr-Regular.otf"));
                recommend_course.setTypeface(Typeface.createFromAsset(context.getAssets()
                        , "fonts/NotoSansCJKkr-Medium.otf"));*/

                recommend_spot_img_first.setImageResource(0);
                recommend_spot_img_second.setImageResource(0);
                recommend_spot_img_third.setImageResource(0);
            }
        });

        return view;
    }

    //시설 필터링 on/off 메소드
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.filter_all:
                if(filter_all.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_all_off).getConstantState())) {
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_on);
                    filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_off);
                    filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_off);
                    filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_off);
                    filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_off);
                }else{
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }
                break;
            case R.id.filter_wheelchairs:
                if(filter_wheelchairs.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_wheelchairs_off).getConstantState())) {
                    filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else{
                    filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_off);
                }
                break;
            case R.id.filter_bathroom:
                if(filter_bathroom.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_bathroom_off).getConstantState())) {
                    filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else{
                    filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_off);
                }
                break;
            case R.id.filter_parkinglot:
                if(filter_parkinglot.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_parkinglot_off).getConstantState())) {
                    filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else{
                    filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_off);
                }
                break;
            case R.id.filter_elevator:
                if(filter_elevator.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_elevator_off).getConstantState())) {
                    filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else {
                    filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_off);
                }
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public class mPagerAdapter extends SCViewPagerAdapter{

        public mPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try{
                Log.d("adFrame", "getItem: " + fragmentList.get(position));
                return fragmentList.get(position);
            }catch (Exception e){
                Log.d("adFrame", "getItem: " + e.toString());
                Log.d("adFrame", "getItem: " + fragmentList );
                return null;

            }
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

}
