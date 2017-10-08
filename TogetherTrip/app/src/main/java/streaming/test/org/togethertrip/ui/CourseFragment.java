package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.CourseListDatas;
import streaming.test.org.togethertrip.datas.CourseResult;
import streaming.test.org.togethertrip.datas.CourseSearchData;
import streaming.test.org.togethertrip.network.NetworkService;

import static android.view.View.GONE;

/**
 * Created by minseung on 2017-10-08.
 */

public class CourseFragment extends Fragment {
    Activity activity;
    Context context;
    FloatingActionButton fab;

    GridView CourselistView;
    CourseListAdapter courseListAdapter;
    ArrayList<CourseListDatas> courseListDatas;
    NetworkService networkService;
    String search_keyword;
    CourseSearchData courseSearchData;

    ImageButton btn_search;
    ImageButton btn_map;
    ImageButton real_searchBtn;
    TextView tv_main;
    EditText edit_search;

    String choice_sido = "";
    Spinner spinner_location;
    SpinnerAdapter adspin1;
    final static String[] arrayLocation = {"서울", "인천", "경기도", "강원도", "충청북도",
            "충청남도", "전라북도", "전라남도", "경상북도", "경상남도"};
    String addr;

    public CourseFragment(){

    }

    public CourseFragment(Activity activity){
        this.activity = activity;
        context = activity;
        courseListAdapter = new CourseListAdapter(context, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course, container, false);
        CourselistView = (GridView)view.findViewById(R.id.course_gridView);
        btn_search = (ImageButton) view.findViewById(R.id.btn_course_search);
        btn_map = (ImageButton) view.findViewById(R.id.btn_course_map);
        real_searchBtn = (ImageButton) view.findViewById(R.id.real_searchBtn);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        edit_search = (EditText) view.findViewById(R.id.edit_search);
        spinner_location = (Spinner) view.findViewById(R.id.spinner_course_location);

        //fab 버튼누르면 작성창
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CourseWrite.class));
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_search.setVisibility(GONE);
                real_searchBtn.setVisibility(View.VISIBLE);
                tv_main.setVisibility(GONE);
                edit_search.setVisibility(View.VISIBLE);
                edit_search.requestFocus();
            }
        });

        real_searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseSearch();
            }
        });

        adspin1 = new CourseFragment.SpinnerAdapter(activity, arrayLocation, android.R.layout.simple_spinner_dropdown_item);
//        adspin1 = ArrayAdapter.createFromResource(activity, R.array.city, android.R.layout.simple_spinner_dropdown_item );
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adspin1);
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adspin1.getItem(position).equals("서울")){
                    choice_sido = "서울";
                }else if(adspin1.getItem(position).equals("인천")){
                    choice_sido = "인천";
                }else if(adspin1.getItem(position).equals("경기도")){
                    choice_sido = "경기도";
                }else if(adspin1.getItem(position).equals("강원도")){
                    choice_sido = "강원도";
                }else if(adspin1.getItem(position).equals("충청북도")){
                    choice_sido = "충청북도";
                }else if(adspin1.getItem(position).equals("충청남도")){
                    choice_sido = "충청남도";
                }else if(adspin1.getItem(position).equals("전라북도")){
                    choice_sido = "전라북도";
                }else if(adspin1.getItem(position).equals("전라남도")){
                    choice_sido = "전라남도";
                }else if(adspin1.getItem(position).equals("경상북도")){
                    choice_sido = "경상북도";
                }else if(adspin1.getItem(position).equals("경상남도")){
                    choice_sido = "경상남도";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         courseSearch();
    }

    //코스 검색
    public void courseSearch(){
//        Log.d(TAG, "search: " + search_keyword);
        search_keyword = edit_search.getText().toString();

        if(search_keyword == null) search_keyword = "go";
        else if(search_keyword.equals("")) search_keyword = "go";

        courseSearchData = new CourseSearchData();

       courseSearchData.userid = "joo";
       courseSearchData.keyword = search_keyword;

        networkService = ApplicationController.getInstance().getNetworkService();

        Call<CourseResult> requestCourseData = networkService.getCourseResult(courseSearchData.keyword,courseSearchData.userid);
        requestCourseData.enqueue(new Callback<CourseResult>() {

            @Override
            public void onResponse(Call<CourseResult> call, Response<CourseResult> response) {
                if (response.isSuccessful()) {
                    courseListDatas = response.body().result;
                    courseListAdapter = new CourseListAdapter(context, courseListDatas);
                    CourselistView.setAdapter(courseListAdapter);
                }
            }

            @Override
            public void onFailure(Call<CourseResult> call, Throwable t) {
                Toast.makeText(context, "통신실패", Toast.LENGTH_SHORT).show();

            }

        });

    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[] {};

        public SpinnerAdapter(final Context context,
                              final String[] objects, final int textViewResourceId) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        /**
         * 스피너 클릭시 보여지는 View의 정의
         */
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_dropdown_item, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.parseColor("#1E3790"));
            tv.setTextSize(12);
            tv.setHeight(50);
            return convertView;
        }

        /**
         * 기본 스피너 View 정의
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.parseColor("#1E3790"));
            tv.setTextSize(12);
            return convertView;
        }
    }
}
