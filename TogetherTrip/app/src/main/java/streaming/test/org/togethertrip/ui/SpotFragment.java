package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import streaming.test.org.togethertrip.datas.DetailImage;
import streaming.test.org.togethertrip.datas.DetailInfo;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResponse;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResult;
import streaming.test.org.togethertrip.datas.DetailSpotListDatas;
import streaming.test.org.togethertrip.datas.OtherInfo;
import streaming.test.org.togethertrip.datas.SearchData;
import streaming.test.org.togethertrip.datas.TouristSpotSearchList;
import streaming.test.org.togethertrip.datas.TouristSpotSearchResult;
import streaming.test.org.togethertrip.network.NetworkService;

import static android.view.View.GONE;

/**
 * Created by taehyung on 2017-09-06.
 */

public class SpotFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    static final String TAG = "SpotFragmentLog";
    Context context;
    Activity activity;

    ListView spotList;
    ArrayList<TouristSpotSearchList> spotResultListDatas;
    TouristSpot_ListViewAdapter adapter;

    DetailSpotListClickResponse detailSpotListClickResponse;
    OtherInfo otherInfo;
    ArrayList<DetailInfo> detailInfo;
    ArrayList<DetailImage> detailImage;
    Intent detailIntent;
    String firstImgUri;

    NetworkService networkService;

    ImageButton btn_search;
    ImageButton btn_map;
    ImageButton real_searchBtn;
    TextView tv_main;
    EditText edit_search;

    ImageButton filter_all,filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;
    FloatingActionButton touristSpot_fab_btn;

    SwipeRefreshLayout spot_refreshLayout;

    String choice_sido = "";
    String choice_category = "";
    Spinner spinner_location;
    Spinner spinner_category;
    SpinnerAdapter adspin1; // location SpinnerAdater
    SpinnerAdapter2 adspin2; // category SpinnerAdapter
    final static String[] arrayLocation = {"전체","서울", "인천", "경기도", "강원도", "충청북도",
            "충청남도", "전라북도", "전라남도", "경상북도", "경상남도"};
    final static String[] arrayCategory = {"전체", "음식점", "쇼핑", "숙박", "문화시설", "관광지"};
    TextView tv_category;
    String search_keyword;
    SearchData searchData;

    String addr;
    String nickName;
    String contentId, contentTypeId;


    public SpotFragment(){

    }

    public SpotFragment(Activity activity, String nickName){
        this.activity = activity;
        context = activity;
        this.nickName = nickName;
        adapter = new TouristSpot_ListViewAdapter(context, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tourist_spot, container, false);

        networkService = ApplicationController.getInstance().getNetworkService();

        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        btn_map = (ImageButton) view.findViewById(R.id.btn_map);
        real_searchBtn = (ImageButton) view.findViewById(R.id.real_searchBtn);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        edit_search = (EditText) view.findViewById(R.id.edit_search);
        spotList = (ListView) view.findViewById(R.id.touristSpot_listView);
        touristSpot_fab_btn = (FloatingActionButton) view.findViewById(R.id.touristSpot_fab_btn);
        spot_refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.spot_refreshLayout);

        filter_all = (ImageButton) view.findViewById(R.id.filter_all);
        filter_wheelchairs = (ImageButton) view.findViewById(R.id.filter_wheelchairs);
        filter_bathroom = (ImageButton) view.findViewById(R.id.filter_bathroom);
        filter_parkinglot = (ImageButton) view.findViewById(R.id.filter_parkinglot);
        filter_elevator = (ImageButton) view.findViewById(R.id.filter_elevator);

        spinner_location = (Spinner) view.findViewById(R.id.spinner_location);
        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);

        filter_all.setOnClickListener(this);
        filter_wheelchairs.setOnClickListener(this);
        filter_bathroom.setOnClickListener(this);
        filter_parkinglot.setOnClickListener(this);
        filter_elevator.setOnClickListener(this);

        spot_refreshLayout.setOnRefreshListener(this);

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
                search();
            }
        });

        adspin1 = new SpinnerAdapter(activity, arrayLocation, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adspin1);
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adspin1.getItem(position).equals("전체")){
                    choice_sido = "";
                }else if(adspin1.getItem(position).equals("서울")){
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
                choice_sido = "서울";
            }
        });

        adspin2 = new SpinnerAdapter2(activity, arrayCategory, android.R.layout.simple_spinner_dropdown_item);
        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adspin2);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adspin2.getItem(position).equals("전체")){
                    choice_category = "";
                }else if(adspin2.getItem(position).equals("음식점")){
                    choice_category = "음식점";
                }else if(adspin2.getItem(position).equals("쇼핑")){
                    choice_category = "쇼핑";
                }else if(adspin2.getItem(position).equals("숙박")){
                    choice_category = "숙박";
                }else if(adspin2.getItem(position).equals("문화시설")){
                    choice_category = "문화시설";
                }else if(adspin2.getItem(position).equals("관광지")){
                    choice_category = "관광지";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                choice_category = "전체";
            }
        });

        spotList.setOnItemClickListener(itemClickListener);

        touristSpot_fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * TODO (후기)작성버튼 클릭시
                 */
                startActivity(new Intent(context, TouristSpotReviewWrite.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search();

    }

    //관광지 검색 메소드
    public void search(){
        search_keyword = edit_search.getText().toString();

        if(search_keyword == null) search_keyword = "광화문";
        else if(search_keyword.equals("")) search_keyword = "광화문";

        Log.d(TAG, "search: " + search_keyword);
        searchData = new SearchData();
        /*
         * TODO 나중에 userid는 받아와야됨~
         */
        searchData.userid = nickName;
        searchData.keyword = search_keyword;

        Call<TouristSpotSearchResult> requestDriverApplyOwner = networkService.searchTouristSpot(searchData);
        requestDriverApplyOwner.enqueue(new Callback<TouristSpotSearchResult>() {
            @Override
            public void onResponse(Call<TouristSpotSearchResult> call, Response<TouristSpotSearchResult> response) {
                if (response.isSuccessful()) {
                    spotResultListDatas = response.body().result;
                    //    Log.v("YG", spotResultListDatas.get(0).Tripinfo.toString());
                    Log.d(TAG, "onResponse: search() 성공");

                    adapter = new TouristSpot_ListViewAdapter(context, spotResultListDatas);
                    spotList.setAdapter(adapter);

                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<TouristSpotSearchResult> call, Throwable t) {
                //검색시 통신 실패
                if(nickName==null){
                    Toast.makeText(activity, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            tv_category.setTextSize(12);
            return convertView;
        }
    }

    //리스트 클릭 리스너
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            clickItem(view, position);
        }
    };

    // detail 네트워킹
    public void clickItem(View view, int position){
        Log.d(TAG, "clickItem: 진입");
        DetailSpotListDatas detailSpotListDatas = new DetailSpotListDatas();
        detailSpotListDatas.contentid = spotResultListDatas.get(position).tripinfo.contentid;
        detailSpotListDatas.contenttypeid = spotResultListDatas.get(position).tripinfo.contenttypeid;

        Log.d(TAG, "clickItem: contentId / contentTypeId : " + detailSpotListDatas.contentid +
                " / " + detailSpotListDatas.contenttypeid);

        /*
        * TODO 나중에 userid는 받아와야함!
         */
        detailSpotListDatas.userid = nickName;

        addr = spotResultListDatas.get(position).tripinfo.addr1;
        contentId = spotResultListDatas.get(position).tripinfo.contentid;
        contentTypeId = spotResultListDatas.get(position).tripinfo.contenttypeid;

        NetworkService list_networkService = ApplicationController.getInstance().getNetworkService();
        Log.d(TAG, "clickItem: networkService: " + list_networkService);
        Call<DetailSpotListClickResult> requestDetailSpotList = list_networkService.clickDetailSpotList(detailSpotListDatas);
        requestDetailSpotList.enqueue(new Callback<DetailSpotListClickResult>() {
            @Override
            public void onResponse(Call<DetailSpotListClickResult> call, Response<DetailSpotListClickResult> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: detail 통신");

                    detailSpotListClickResponse = response.body().tripinfo;
                    otherInfo = response.body().otherinfo;

                    detailIntent = new Intent(context, TouristSpotDetail.class);
                    Intent firstImgIntent = new Intent(context, SpotDetailImgFragment.class);
                    detailIntent.putExtra("stringAddr", addr);
                    detailIntent.putExtra("contentId", contentId);
                    detailIntent.putExtra("contentTypeId", contentTypeId);
                    detailIntent.putExtra("detailCommon", detailSpotListClickResponse.detailCommon);
                    detailIntent.putExtra("detailIntro", detailSpotListClickResponse.detailIntro);
                    detailIntent.putParcelableArrayListExtra("detailInfo", detailSpotListClickResponse.detailInfo);
                    detailIntent.putExtra("detailWithTour", detailSpotListClickResponse.detailWithTour);
                    detailIntent.putParcelableArrayListExtra("detailImage", detailSpotListClickResponse.detailImage);
                    detailIntent.putExtra("otherInfo", otherInfo);
                    detailIntent.putExtra("firstImgUri", detailSpotListClickResponse.detailImage.get(0).originimgurl);

                    firstImgUri = detailSpotListClickResponse.detailImage.get(0).originimgurl;

                    firstImgIntent.putExtra("firstImgUri", firstImgUri);
                    Log.d(TAG, "onResponse: firstImgUri: " + firstImgUri);

                    Log.d(TAG, "onResponse: detailInfo: " + detailSpotListClickResponse.detailInfo);
                    Log.d(TAG, "onResponse: detailImage: " + detailSpotListClickResponse.detailImage);

                    startActivity(detailIntent);

                } else {
                    Log.d(TAG, "onResponse: clickList response is not success");
                }
            }
            @Override
            public void onFailure(Call<DetailSpotListClickResult> call, Throwable t) {
                //검색시 통신 실패
                if(nickName==null){
                    Toast.makeText(activity, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Swipe시 갱신되는 메소드
    @Override
    public void onRefresh() {
        search();
        spot_refreshLayout.setRefreshing(false);
    }

}
