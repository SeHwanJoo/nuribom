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

import java.util.ArrayList;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.DetailCourseDatas;
import streaming.test.org.togethertrip.datas.DetailCourseInfo;
import streaming.test.org.togethertrip.network.NetworkService;

//import static streaming.test.org.togethertrip.ui.CourseWrite.position;

public class DetailCourseFragment extends Fragment {
    private static final String TAG = "DetailCourseFragment";
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    Context context;
    Activity activity;
    ImageView imageView;
    Intent intent;
    TextView courseTitle, courseDate, courseCategory, courseWriter;
    private NetworkService service;
    DetailCourseInfo detailCourseInfo;
    String title;
    ArrayList<DetailCourseDatas.Page> page;
    DetailCourseDatas.Result result;


    public DetailCourseFragment() {}

    public DetailCourseFragment(Context context, DetailCourseDatas.Result result){
        this.context = context;
        this.result = result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_detail_course, container, false);
        imageView =(ImageView)view.findViewById(R.id.course_detail_elbum);
        courseTitle = (TextView) view.findViewById(R.id.course_detail_courseTitle);
        courseDate = (TextView) view.findViewById(R.id.course_detail_calenderview);
        courseCategory = (TextView) view.findViewById(R.id.course_detail_category);
        courseWriter = (TextView) view.findViewById(R.id.course_detail_writer);

        courseDate.setText(result.date);
        courseTitle.setText(result.title);
//        courseDate.setText(result.date);
        courseCategory.setText(result.category);
        courseWriter.setText(result.userid);
        Glide.with(context).load(result.image)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(imageView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override

    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            // 사용될 activity 에 context 정보 가져오는 부분
            this.activity = (Activity)context;
        }
    }



}