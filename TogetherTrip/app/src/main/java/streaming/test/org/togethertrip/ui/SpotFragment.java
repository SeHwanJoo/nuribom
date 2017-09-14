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
import butterknife.OnClick;
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

    @BindView(R.id.btn_search) Button btn_search;
    @BindView(R.id.btn_map) Button btn_map;
    @BindView(R.id.real_searchBtn) Button real_searchBtn;
    @BindView(R.id.tv_main) TextView tv_main;
    @BindView(R.id.edit_search) EditText edit_search;
    @BindView(R.id.filter_all) Button filter_all;
    @BindView(R.id.filter_touristSpot) Button filter_touristSpot;
    @BindView(R.id.filter_culture) Button filter_culture;
    @BindView(R.id.filter_stay) Button filter_stay;
    @BindView(R.id.filter_shopping) Button filter_shopping;
    @BindView(R.id.filter_food) Button filter_food;

    String keyword;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tourist_spot, container, false);
        ButterKnife.bind(activity);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_search)
    public void searchClick(){
        tv_main.setVisibility(GONE);
        btn_search.setVisibility(GONE);
        edit_search.setVisibility(View.VISIBLE);
        real_searchBtn.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.real_searchBtn)
    public void realSearchClick(){
        search();
    }

    @OnClick({R.id.filter_all, R.id.filter_touristSpot, R.id.filter_culture, R.id.filter_stay, R.id.filter_shopping, R.id.filter_food})
    public void filterClick(View view){
        switch(view.getId()){
            case R.id.filter_all:

                break;
            case R.id.filter_touristSpot:

                break;
            case R.id.filter_culture:

                break;
            case R.id.filter_stay:

                break;
            case R.id.filter_shopping:

                break;
            case R.id.filter_food:

                break;
        }
    }

    //검색 네트워킹하는 메소드
    public void search(){
        keyword = edit_search.getText().toString();

        if(keyword == null) keyword = "a";

        networkService = ApplicationController.getInstance().getNetworkService();
        Log.d(TAG, "onClick: networkService :" + networkService );

        Call<TouristSpotSearchResult> requestDriverApplyOwner = networkService.searchTouristSpot(keyword);
        requestDriverApplyOwner.enqueue(new Callback<TouristSpotSearchResult>() {
            @Override
            public void onResponse(Call<TouristSpotSearchResult> call, Response<TouristSpotSearchResult> response) {
                if (response.isSuccessful()) {
                    /*
                    TODO 잘 실행 되는지?
                     */
                    Log.d(TAG, "onResponse: search: " + keyword);
                    spotResultListDatas = response.body().result;

                    adapter = new TouristSpot_ListViewAdapter(context, null);
                    spotList.setAdapter(adapter);

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
