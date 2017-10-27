package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.melnykov.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.CourseListDatas;
import streaming.test.org.togethertrip.datas.DetailCourseDatas;
import streaming.test.org.togethertrip.datas.DetailCourseInfo;
import streaming.test.org.togethertrip.datas.DetailCourseResult;
import streaming.test.org.togethertrip.datas.DetailSpotListDatas;
import streaming.test.org.togethertrip.network.NetworkService;

import static streaming.test.org.togethertrip.ui.CourseWrite.position;

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
        courseDate = (TextView) view.findViewById(R.id.course_detail_date);
        courseCategory = (TextView) view.findViewById(R.id.course_detail_category);
        courseWriter = (TextView) view.findViewById(R.id.course_detail_writer);

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





    public interface DataSetListner{
        void FirstFragmentImageSet(MultipartBody.Part image);
        void FirstFragmentCategorySet( String category);
        void FirstFragmentTitleSet(String title);
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
