package streaming.test.org.togethertrip.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import streaming.test.org.togethertrip.R;

public class CourseWrite extends AppCompatActivity {
    Button button;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    CourseWriteFragment courseWriteFragment;
    CourseWriteFragment2 courseWriteFragment2;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ImageView imgelbum;
    //int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24*2,getResources().getDisplayMetrics());


    static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_write_viewpager);
        button = (Button) findViewById(R.id.nextbtn);
//        imgelbum = (ImageView) findViewById(R.id.elbum);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        courseWriteFragment = new CourseWriteFragment();
        courseWriteFragment2 = new CourseWriteFragment2();
        fragmentList.add(position, courseWriteFragment);
        position++;

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentList.add(position, new CourseWriteFragment2());
                position++;
                mSectionsPagerAdapter.notifyDataSetChanged();

                mViewPager.setClipToPadding(false);
                mViewPager.setPadding(0,0,0,0);
                mViewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / -9);
                mViewPager.setPageMargin(getResources().getDisplayMetrics().heightPixels/ -8);
                mViewPager.setCurrentItem(position,true);

//                if (position==position-1){
//
//                }
            }
        });
    }


    //페이지 선택 확인을 위한 어댑터
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);

          //  mViewPager.setPageMargin(-margin);
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
            //        return super.getPageWidth(position);

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
    public void onBackPressed() {
        super.onBackPressed();
        position = 0;
        finish();
    }
}