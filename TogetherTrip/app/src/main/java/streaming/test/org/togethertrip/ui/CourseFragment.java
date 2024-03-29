package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.CourseInfo;
import streaming.test.org.togethertrip.datas.CourseListDatas;
import streaming.test.org.togethertrip.datas.CourseResult;
import streaming.test.org.togethertrip.datas.CourseSearchData;
import streaming.test.org.togethertrip.datas.DetailCourseDatas;
import streaming.test.org.togethertrip.network.NetworkService;

import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static streaming.test.org.togethertrip.R.id.spinner_course_type;

/**
 * Created by minseung on 2017-10-08.
 */

public class CourseFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    Activity activity;
    Context context;
    FloatingActionButton fab;
    ArrayList<DetailCourseDatas.Page> page;
    CourseInfo courseInfo;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    Intent detailIntent;

    GridView CourselistView;
    CourseListAdapter courseListAdapter;
    ArrayList<CourseListDatas> courseListDatas;
    NetworkService networkService;
    String search_keyword;
    CourseSearchData courseSearchData;
    SharedPreferences loginInfo;

    ImageButton btn_search;
    ImageButton btn_map;
    ImageButton real_searchBtn;
    TextView tv_main;
    EditText edit_search;
    SwipeRefreshLayout spot_refreshLayout;

    String choice_sido = "";
    Spinner spinner_location;
    SpinnerAdapter adspin1;
    final static String[] arrayLocation = {"서울", "인천", "경기도", "강원도", "충청북도",
            "충청남도", "전라북도", "전라남도", "경상북도", "경상남도"};

    String choice_category_type;
    Spinner spinner_category_type;
    SpinnerAdapter2 adspin2;
    //TODO 카테고리 정해서 넣어야됨!
    final static String[] arrayCategoryType = {"전체","힐링", "가족", "친구","연인"};
    String nickName;

    public CourseFragment(){

    }

    public CourseFragment(Activity activity, String nickName){
        this.activity = activity;
        context = activity;
        this.nickName = nickName;
        courseListAdapter = new CourseListAdapter(context, null);
        courseListAdapter.notifyDataSetChanged();
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
        loginInfo = getActivity().getSharedPreferences("loginSetting", 0);

        spinner_category_type = (Spinner) view.findViewById(spinner_course_type);

        courseInfo = new CourseInfo();
        courseInfo.userid = loginInfo.getString("nickname","");
        //TODO userid sharedpreference로 받아와야함
        courseInfo.keyword = "";
        //TODO 검색했을때 키워드 받아오기

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

        adspin2 = new SpinnerAdapter2(activity, arrayCategoryType, android.R.layout.simple_spinner_dropdown_item);
        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category_type.setAdapter(adspin2);
        spinner_category_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(adspin2.getItem(position).toString()){
                    case "전체":
                        choice_category_type = "전체";
                        break;
                    case "힐링":
                        choice_category_type = "힐링";
                        break;
                    case "가족":
                        choice_category_type = "가족";
                        break;
                    case "친구":
                        choice_category_type = "친구";
                        break;
                    case "연인":
                        choice_category_type = "연인";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                choice_category_type = "전체";
            }
        });
        courseSearch();

        return view;
    }



//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//         courseSearch();
//    }

    //코스 검색
    public void courseSearch(){
//        Log.d(TAG, "search: " + search_keyword);
        search_keyword = edit_search.getText().toString();

        if(search_keyword == null) search_keyword = "";
        else if(search_keyword.equals("")) search_keyword = "";

        courseSearchData = new CourseSearchData();

       courseSearchData.userid = nickName;
       courseSearchData.keyword = search_keyword;

        networkService = ApplicationController.getInstance().getNetworkService();

        Call<CourseResult> requestCourseData = networkService.getCourseResult(courseInfo);
        requestCourseData.enqueue(new Callback<CourseResult>() {

            @Override
            public void onResponse(Call<CourseResult> call, final Response<CourseResult> response) {
                if (response.isSuccessful()) {
                    courseListDatas = response.body().result;
                    courseListAdapter = new CourseListAdapter(context, courseListDatas);
                    CourselistView.setAdapter(courseListAdapter);
                    courseListAdapter.notifyDataSetChanged();
                    CourselistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            detailIntent = new Intent(context, DetailCourseActivity.class);
                            detailIntent.putExtra("userid",response.body().result.get(position).userid);
                            detailIntent.putExtra("courseid",response.body().result.get(position).courseid);
                            detailIntent.putExtra("commentcount",response.body().result.get(position).commentcount);
                            detailIntent.putExtra("heartCount",response.body().result.get(position).likecount);
                            startActivity(detailIntent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CourseResult> call, Throwable t) {
                if(nickName==null){
                    Toast.makeText(activity, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    //Location Spinner(지역 스피너)
    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[] {};

        public SpinnerAdapter(final Context context,
                              final String[] objects, final int textViewResourceId) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
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
//            tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Medium.otf"));
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
//            tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Medium.otf"));
            tv.setTextSize(12);
            return convertView;
        }
    }

    //Category Spinner(카테고리 스피너)
    public class SpinnerAdapter2 extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[] {};

        public SpinnerAdapter2(final Context context,
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

            TextView tv_category = (TextView) convertView.findViewById(android.R.id.text1);
            tv_category.setText(items[position]);
            if(items[position].equals("전체")){
                tv_category.setTextColor(Color.parseColor("#686868"));
            }else {
                tv_category.setTextColor(Color.parseColor("#1E3790"));
            }
            /*try {
                tv_category.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Medium.otf"));
            }catch(Exception e){
                e.printStackTrace();
            }*/
            tv_category.setTextSize(12);
            tv_category.setHeight(50);
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

            TextView tv_category = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv_category.setText(items[position]);
            if(items[position].equals("전체")){
                tv_category.setTextColor(Color.parseColor("#686868"));
            }else {
                tv_category.setTextColor(Color.parseColor("#1E3790"));
            }
            /*try {
                tv_category.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Medium.otf"));
            }catch(Exception e){
                e.printStackTrace();
            }*/
            tv_category.setTextSize(12);
            tv_category.setGravity(TEXT_ALIGNMENT_CENTER);
            return convertView;
        }
    }




    //Swipe시 갱신되는 메소드
    @Override
    public void onRefresh() {
        courseSearch();
        spot_refreshLayout.setRefreshing(false);
    }


}
