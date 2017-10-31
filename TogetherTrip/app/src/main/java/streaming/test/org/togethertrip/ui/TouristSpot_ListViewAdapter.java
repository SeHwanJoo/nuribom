package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.DetailWithTour;
import streaming.test.org.togethertrip.datas.TouristSpotSearchList;
import streaming.test.org.togethertrip.datas.like.AddLikeResult;
import streaming.test.org.togethertrip.datas.like.AddTripsLikeInfo;
import streaming.test.org.togethertrip.network.NetworkService;

/**
 * Created by taehyung on 2017-09-05.
 */

//ListView사용을 위한 어댑터
public class TouristSpot_ListViewAdapter extends BaseAdapter implements Filterable {
    final static String TAG = "ListViewAdapterLog";

    //ContentTypeId에 따른 타입 종류
    final int SPOT = 12;
    final int CULTURE = 14;
    final int FESTIVAL = 15;
    final int REPORTS = 28;
    final int ACCOMMODATION = 32;
    final int SHOPPING = 38;
    final int RESTAURANT = 39;


    ArrayList<TouristSpotSearchList> touristSpotSearchResultList;
    Activity activity;
    Context context;

    String contentId;
    String contentTypeId;

    String parking, route, wheelchair, elevator, restroom, handicapEtc, braileblock;
    String addr;

    AddTripsLikeInfo addTripsLikeInfo;
    ImageButton ib_bigImgHeart;
    TextView tv_heartCount;
    CircleImageView iv_profileImg;

    Filter listFilter;
    ArrayList<TouristSpotSearchList> filteredItemList;

    ImageButton filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;
    DetailWithTour detailWithTour;

    int filterResource;

    SharedPreferences loginInfo;
    String nickname;

    NetworkService networkService;

    public TouristSpot_ListViewAdapter(Context context){
        this.context = context;
    }

    public TouristSpot_ListViewAdapter(Context context ,ArrayList<TouristSpotSearchList> touristSpotSearchResultList){
        this.context = context;
        this.touristSpotSearchResultList = touristSpotSearchResultList;
    }

    @Override
    public int getCount() {
        if(touristSpotSearchResultList == null){
            return 0;
        }else {
            return touristSpotSearchResultList.size();
        }
    }

    @Override
    public TouristSpotSearchList getItem(int position) {
        return touristSpotSearchResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        loginInfo = context.getSharedPreferences("loginSetting", 0);
        nickname = loginInfo.getString("nickname", "");
        Log.d(TAG, "getView: nickname: " + nickname);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.tourist_spot_list_view_item,null);
        }


        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iv_bigImg = (ImageView) convertView.findViewById(R.id.iv_bigImg);
        ib_bigImgHeart = (ImageButton) convertView.findViewById(R.id.ib_bigImgHeart);
        iv_profileImg = (CircleImageView) convertView.findViewById(R.id.iv_profileImg);
        TextView tv_spotName = (TextView) convertView.findViewById(R.id.tv_spotName);
        TextView tv_spotAddr = (TextView) convertView.findViewById(R.id.tv_spotAddr);
        tv_heartCount = (TextView) convertView.findViewById(R.id.tv_heartCount);
        TextView tv_commentCount = (TextView)convertView.findViewById(R.id.tv_commentCount);

        filter_bathroom = (ImageButton) convertView.findViewById(R.id.filter_bathroom);
        filter_elevator = (ImageButton) convertView.findViewById(R.id.filter_elevator);
        filter_parkinglot = (ImageButton) convertView.findViewById(R.id.filter_parkinglot);
        filter_wheelchairs = (ImageButton) convertView.findViewById(R.id.filter_wheelchairs);

        // 아이템 내 각 위젯에 데이터 반영
       Glide.with(context).load(touristSpotSearchResultList.get(position).tripinfo.firstimage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(iv_bigImg);
        tv_spotAddr.setText(touristSpotSearchResultList.get(position).tripinfo.addr1);
        tv_spotName.setText(touristSpotSearchResultList.get(position).tripinfo.title);
        contentId = touristSpotSearchResultList.get(position).tripinfo.contentid;
        contentTypeId = touristSpotSearchResultList.get(position).tripinfo.contenttypeid;

        parking = touristSpotSearchResultList.get(position).detailWithTour.parking;
        route = touristSpotSearchResultList.get(position).detailWithTour.route;
        wheelchair = touristSpotSearchResultList.get(position).detailWithTour.wheelchair;
        elevator = touristSpotSearchResultList.get(position).detailWithTour.elevator;
        restroom = touristSpotSearchResultList.get(position).detailWithTour.restroom;
        handicapEtc = touristSpotSearchResultList.get(position).detailWithTour.handicapetc;
        braileblock = touristSpotSearchResultList.get(position).detailWithTour.braileblock;
        tv_heartCount.setText(String.valueOf(touristSpotSearchResultList.get(position).tripinfo.likecount));
        tv_commentCount.setText(String.valueOf(touristSpotSearchResultList.get(position).tripinfo.commentcount));

        //시설 정보 유무에 따른 이미지
        checkFacilities();

        //contentType에 따른 이미지
        try {
            checkContentType(Integer.parseInt(contentTypeId));
        }catch(Exception e){
            e.printStackTrace();
            iv_profileImg.setImageResource(R.drawable.default_image);
        }

        networkService = ApplicationController.getInstance().getNetworkService();
        //하트 버튼 눌렸을 때, 하트 색 바꾸기
        ib_bigImgHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartNetwork(position);
            }
        });

        //상세보기에서 주소 보여주기 위함
        addr = touristSpotSearchResultList.get(position).tripinfo.addr1;

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /*
    * TODO 필터링...
     */
    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        } return listFilter ;

    }

    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0){
                results.values = touristSpotSearchResultList;
                results.count = touristSpotSearchResultList.size();
            }else{
                ArrayList<TouristSpotSearchList> itemList = new ArrayList<TouristSpotSearchList>();
                for(TouristSpotSearchList item : touristSpotSearchResultList){
                    /*
                    TODO 버튼이 눌림을 확인하고 item 추가
                    http://recipes4dev.tistory.com/96

                     */

                }

                results.values = itemList;
                results.count = itemList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItemList = (ArrayList<TouristSpotSearchList>) results.values;

            if(results.count>0){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    }

    public void checkFacilities(){
        try {
            if (!elevator.equals(null)) {
                filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_on);
            }
        }catch (NullPointerException ne){
            filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_off);
        }
        try{
            if(!parking.equals(null)) {
                filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_on);
            }
        }catch (NullPointerException ne){
            filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_off);
        }
        try {
            if (!restroom.equals(null)) {
                filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_on);
            }
        }catch (NullPointerException ne){
            filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_off);
        }
        try{
            if(!wheelchair.equals(null)) {
                filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_on);
            }
        }catch (NullPointerException ne){
            filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_off);
        }
    }

    public void checkContentType(int type){
        switch(type){
            case SPOT:
                iv_profileImg.setImageResource(R.drawable.trips_categoryimage_touristspot);
                break;
            case CULTURE:
                iv_profileImg.setImageResource(R.drawable.trips_categoryimage_culturalfacility);
                break;
            case FESTIVAL:
                iv_profileImg.setImageResource(R.drawable.trips_categoryimage_festival);
                break;
            case REPORTS:
                iv_profileImg.setImageResource(R.drawable.trips_categoryimage_sports);
                break;
            case ACCOMMODATION:
                iv_profileImg.setImageResource(R.drawable.trips_categoryimage_accommodations);
                break;
            case SHOPPING:
                iv_profileImg.setImageResource(R.drawable.trips_categoryimage_shopping);
                break;
            case RESTAURANT:
                iv_profileImg.setImageResource(R.drawable.trips_categoryimage_restrant);
                break;
            default:

        }
    }

    public void heartNetwork(int position){
        addTripsLikeInfo = new AddTripsLikeInfo();
        addTripsLikeInfo.userid = nickname;
        addTripsLikeInfo.contentid = touristSpotSearchResultList.get(position).tripinfo.contentid;

        Log.d(TAG, "likeInfo: " + addTripsLikeInfo.userid);
        Log.d(TAG, "likeInfo: " + addTripsLikeInfo.contentid);
        Call<AddLikeResult> addTripsLike = networkService.addTripLikeResult(addTripsLikeInfo);
        addTripsLike.enqueue(new Callback<AddLikeResult>() {
            @Override
            public void onResponse(Call<AddLikeResult> call, Response<AddLikeResult> response) {
                if(response.isSuccessful()){
                    if(response.body().result.message.equals("like")){
                        ib_bigImgHeart.setBackgroundResource(R.drawable.trips_heart_on);
                        tv_heartCount.setText(response.body().result.likecount);
                        Log.d(TAG, "onResponse: like response: " + response.body().result.likecount);
                        Toast.makeText(activity, "좋아요!", Toast.LENGTH_SHORT).show();
                    }else if(response.body().result.message.equals("unlike")){
                        ib_bigImgHeart.setBackgroundResource(R.drawable.trips_heart_off);
                        tv_heartCount.setText(response.body().result.likecount);
                        Toast.makeText(activity, "좋아요 취소", Toast.LENGTH_SHORT).show();

                    }
                    notifyDataSetChanged();
                }else{
                    //TODO 태형 : 통신시 이부분으로 들어옴 확인할 것
                    Log.d(TAG, "onResponse: response.isSuccessful(): " + response.isSuccessful());
                }

                /*
                 * notifyDataSetChanged() 이부분에 달려있으면 스크롤 할때마다 갱신됨! 메모리 낭비 심한거같은데...
                 */

            }

            @Override
            public void onFailure(Call<AddLikeResult> call, Throwable t) {

            }
        });
    }

}
