package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.SearchData;
import streaming.test.org.togethertrip.datas.TouristSpotSearchList;
import streaming.test.org.togethertrip.datas.TouristSpotSearchResult;
import streaming.test.org.togethertrip.network.NetworkService;

import static android.view.View.GONE;

/**
 * Created by taehyung on 2017-09-06.
 */

public class SpotFragment extends Fragment implements View.OnClickListener{
    static final String TAG = "SpotFragmentLog";
    Context context;
    Activity activity;

    ListView spotList;
    ArrayList<TouristSpotSearchList> spotResultListDatas;
    TouristSpot_ListViewAdapter adapter;

    NetworkService networkService;

    ImageButton btn_search;
    ImageButton btn_map;
    ImageButton real_searchBtn;
    TextView tv_main;
    EditText edit_search;

    ImageButton filter_all,filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;

    String choice_sido = "";
    Spinner spinner_location;
    SpinnerAdapter adspin1;
    final static String[] arrayLocation = {"서울", "인천", "경기도", "강원도", "충청북도",
            "충청남도", "전라북도", "전라남도", "경상북도", "경상남도"};

    String search_keyword;
    SearchData searchData;

    public SpotFragment(){

    }

    public SpotFragment(Activity activity){
        this.activity = activity;
        context = activity;

        adapter = new TouristSpot_ListViewAdapter(context, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tourist_spot, container, false);

        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        btn_map = (ImageButton) view.findViewById(R.id.btn_map);
        real_searchBtn = (ImageButton) view.findViewById(R.id.real_searchBtn);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        edit_search = (EditText) view.findViewById(R.id.edit_search);
        spotList = (ListView) view.findViewById(R.id.touristSpot_listView);

        filter_all = (ImageButton) view.findViewById(R.id.filter_all);
        filter_wheelchairs = (ImageButton) view.findViewById(R.id.filter_wheelchairs);
        filter_bathroom = (ImageButton) view.findViewById(R.id.filter_bathroom);
        filter_parkinglot = (ImageButton) view.findViewById(R.id.filter_parkinglot);
        filter_elevator = (ImageButton) view.findViewById(R.id.filter_elevator);

        spinner_location = (Spinner) view.findViewById(R.id.spinner_location);

        filter_all.setOnClickListener(this);
        filter_wheelchairs.setOnClickListener(this);
        filter_bathroom.setOnClickListener(this);
        filter_parkinglot.setOnClickListener(this);
        filter_elevator.setOnClickListener(this);


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
//        adspin1 = ArrayAdapter.createFromResource(activity, R.array.city, android.R.layout.simple_spinner_dropdown_item );
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_location.setAdapter(adspin1);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //관광지 검색 메소드
    public void search(){
        Log.d(TAG, "search: in!");
        if(search_keyword == null) search_keyword = "광화문";

        search_keyword = edit_search.getText().toString();
        searchData = new SearchData();
        /*
         * TODO 나중에 userid는 받아와야됨~
         */
        searchData.userid = "joo";
        searchData.keyword = search_keyword;
        Log.d(TAG, "search: searchData.keyword: " + searchData.keyword);

        networkService = ApplicationController.getInstance().getNetworkService();
        Log.d(TAG, "search: networkService: " + networkService);

        Call<TouristSpotSearchResult> requestDriverApplyOwner = networkService.searchTouristSpot(searchData);
        requestDriverApplyOwner.enqueue(new Callback<TouristSpotSearchResult>() {
            @Override
            public void onResponse(Call<TouristSpotSearchResult> call, Response<TouristSpotSearchResult> response) {
                if (response.isSuccessful()) {
                    /*
                    TODO 잘 실행 되는지?
                     */
                    Log.d(TAG, "onResponse: search: " + search_keyword);
                    spotResultListDatas = response.body().result;
                //    Log.v("YG", spotResultListDatas.get(0).Tripinfo.toString());
                    Log.d(TAG, "onResponse: spotResultListDatas: " + spotResultListDatas);

                    adapter = new TouristSpot_ListViewAdapter(context, spotResultListDatas);
                    Log.d(TAG, "onResponse: adapter: " + adapter);
                    Log.d(TAG, "onResponse: spotList: " + spotList);
                    spotList.setAdapter(adapter);
                    Log.d(TAG, "onResponse: " + spotResultListDatas.get(0));

                } else {
                    Log.d(TAG, "onResponse: response is not success");
                }
            }
            @Override
            public void onFailure(Call<TouristSpotSearchResult> call, Throwable t) {
                //검색시 통신 실패
                Log.d(TAG, "onFailure: !!!");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.filter_all:
                filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_on);
                break;
            case R.id.filter_wheelchairs:
                filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_on);
                break;
            case R.id.filter_bathroom:
                filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_on);
                break;
            case R.id.filter_parkinglot:
                filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_on);
                break;
            case R.id.filter_elevator:
                filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_on);
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
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

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
