package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.DetailCourseDatas;

/**
 * Created by user on 2017-10-27.
 */

public class DetailCourseFragment2 extends Fragment {
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    Context context;
    ImageView courseImgView;
    TextView courseDetailSpotName, courseDetailDate, pageContent, courseDetailLocation, courseNumber;


    Activity activity;
    Intent intent;
    DetailCourseDatas.Page page;

    public DetailCourseFragment2(Context context, DetailCourseDatas.Page page) {
        this.context = context;
        this.page = page;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_course2, container, false);
        courseImgView = (ImageView) view.findViewById(R.id.detail_course_elbum2);
        courseDetailSpotName = (TextView) view.findViewById(R.id.course_detail_spotname);
        courseDetailDate = (TextView)view.findViewById(R.id.course_detail_date);
        courseDetailLocation=(TextView)view.findViewById(R.id.course_detail_location);
        pageContent =(TextView)view.findViewById(R.id.page_content);
        courseNumber = (TextView)view.findViewById(R.id.course_number);

        courseDetailSpotName.setText(page.title);
        courseDetailLocation.setText(page.title);
        courseDetailDate.setText(page.courseid);
        pageContent.setText(page.content);
        Glide.with(context).load(page.image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(courseImgView);
        courseNumber.setText(String.valueOf(getArguments().getInt("courseNumber")));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}