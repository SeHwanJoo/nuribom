package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.SearchData;
import streaming.test.org.togethertrip.datas.TouristSpotSearchList;
import streaming.test.org.togethertrip.network.NetworkService;

import static android.view.View.GONE;

public class TouristSpotActivity extends AppCompatActivity {
    final static String TAG = "TouristActivityLog";
    final private Context context = this;
    final private Activity activity = this;

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

    String search_keyword;
    SearchData searchData;

    NetworkService networkService;

    ListView spotList;
    ArrayList<TouristSpotSearchList> spotResultListDatas;
    TouristSpot_ListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot);
        ButterKnife.bind(this);

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

    public void search(){
//        Toast.makeText(context, "in Activity", Toast.LENGTH_SHORT).show();
//        search_keyword = edit_search.getText().toString();
//        searchData = new SearchData();
//        /*
//         * TODO 나중에 userid는 받아와야됨~
//         */
//        searchData.userid = "Joo";
//        searchData.keyword = search_keyword;
//
//        if(search_keyword == null) search_keyword = "광화문";
//
//        networkService = ApplicationController.getInstance().getNetworkService();
//
//        Call<TouristSpotSearchResult> requestDriverApplyOwner = networkService.searchTouristSpot(searchData);
//        requestDriverApplyOwner.enqueue(new Callback<TouristSpotSearchResult>() {
//            @Override
//            public void onResponse(Call<TouristSpotSearchResult> call, Response<TouristSpotSearchResult> response) {
//                if (response.isSuccessful()) {
//                    /*
//                    TODO 잘 실행 되는지?
//                     */
//                    Log.d(TAG, "onResponse: search: " + search_keyword);
//                    spotResultListDatas = response.body().result;
//                    //    Log.v("YG", spotResultListDatas.get(0).Tripinfo.toString());
//                    Log.d(TAG, "onResponse: spotResultListDatas: " + spotResultListDatas);
//
//                    adapter = new TouristSpot_ListViewAdapter(context, spotResultListDatas);
//                    Log.d(TAG, "onResponse: adapter: " + adapter);
//                    Log.d(TAG, "onResponse: spotList: " + spotList);
//                    spotList.setAdapter(adapter);
//                    Log.d(TAG, "onResponse: " + spotResultListDatas.get(0));
//
//                } else {
//                    //response.isSuccessful() = false
//                }
//            }
//            @Override
//            public void onFailure(Call<TouristSpotSearchResult> call, Throwable t) {
//                //검색시 통신 실패
//            }
//        });
    }

}
