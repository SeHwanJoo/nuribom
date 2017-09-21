package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.TouristSpotSearchList;
import streaming.test.org.togethertrip.datas.TouristSpotSearchResult;
import streaming.test.org.togethertrip.network.NetworkService;

import static android.view.View.GONE;

/**
 * Created by taehyung on 2017-09-06.
 */

public class SpotFragment extends Fragment {
    static final String TAG = "SpotFragmentLog";
    Context context;
    Activity activity;

    ListView spotList;
    ArrayList<TouristSpotSearchList> spotResultListDatas;
    TouristSpot_ListViewAdapter adapter;

    NetworkService networkService;

    @BindView(R.id.filter_all) Button filter_all;
    @BindView(R.id.filter_touristSpot) Button filter_touristSpot;
    @BindView(R.id.filter_culture) Button filter_culture;
    @BindView(R.id.filter_stay) Button filter_stay;
    @BindView(R.id.filter_shopping) Button filter_shopping;
    @BindView(R.id.filter_food) Button filter_food;

    Button btn_search;
    Button btn_map;
    Button real_searchBtn;
    TextView tv_main;
    EditText edit_search;

    String search_keyword;

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
            search();

            spotList = (ListView) activity.findViewById(R.id.touristSpot_listView);
            spotList.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tourist_spot, container, false);
        ButterKnife.bind(activity);

        btn_search = (Button) view.findViewById(R.id.btn_search);
        btn_map = (Button) view.findViewById(R.id.btn_map);
        real_searchBtn = (Button) view.findViewById(R.id.real_searchBtn);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        edit_search = (EditText) view.findViewById(R.id.edit_search);
        spotList = (ListView) view.findViewById(R.id.touristSpot_listView);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_search.setVisibility(GONE);
                real_searchBtn.setVisibility(View.VISIBLE);
                tv_main.setVisibility(GONE);
                edit_search.setVisibility(View.VISIBLE);
            }
        });

        real_searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_keyword = edit_search.getText().toString();

                search();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    @OnClick(R.id.btn_search)
//    public void searchClick(){
//        tv_main.setVisibility(GONE);
//        btn_search.setVisibility(GONE);
//        edit_search.setVisibility(View.VISIBLE);
//        real_searchBtn.setVisibility(View.VISIBLE);
//    }
//
//    @OnClick(R.id.real_searchBtn)
//    public void realSearchClick(){
//        search();
//    }
//
//    @OnClick({R.id.filter_all, R.id.filter_touristSpot, R.id.filter_culture, R.id.filter_stay, R.id.filter_shopping, R.id.filter_food})
//    public void filterClick(View view){
//        switch(view.getId()){
//            case R.id.filter_all:
//
//                break;
//            case R.id.filter_touristSpot:
//
//                break;
//            case R.id.filter_culture:
//
//                break;
//            case R.id.filter_stay:
//
//                break;
//            case R.id.filter_shopping:
//
//                break;
//            case R.id.filter_food:
//
//                break;
//        }
//    }

    //관광지 검색 메소드
    public void search(){
        search_keyword = edit_search.getText().toString();

        if(search_keyword == null) search_keyword = "광화문";

        networkService = ApplicationController.getInstance().getNetworkService();

        Call<TouristSpotSearchResult> requestDriverApplyOwner = networkService.searchTouristSpot(search_keyword);
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
                    //response.isSuccessful() = false
                }
            }
            @Override
            public void onFailure(Call<TouristSpotSearchResult> call, Throwable t) {
                //검색시 통신 실패
            }
        });
    }
}
