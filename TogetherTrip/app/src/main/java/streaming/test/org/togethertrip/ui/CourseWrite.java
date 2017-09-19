package streaming.test.org.togethertrip.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

    static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_write_viewpager);
        button = (Button) findViewById(R.id.nextbtn);

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

                mViewPager.setCurrentItem(position);
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

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }
}
