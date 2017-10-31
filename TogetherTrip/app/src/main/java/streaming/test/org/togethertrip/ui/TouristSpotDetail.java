package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.DetailImage;
import streaming.test.org.togethertrip.datas.DetailInfo;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResponse;
import streaming.test.org.togethertrip.datas.DetailWithTour;
import streaming.test.org.togethertrip.datas.OtherInfo;

import static android.support.constraint.ConstraintSet.WRAP_CONTENT;

public class TouristSpotDetail extends FragmentActivity {
    final static String TAG = "TouristSpotDetailLog";
    private final static String mTMapApiKey = "d9c128a3-3d91-3162-a305-e4b65bea1b55";
    //ContentTypeId에 따른 타입 종류
    final int SPOT = 12;
    final int CULTURE = 14;
    final int FESTIVAL = 15;
    final int REPORTS = 28;
    final int ACCOMMODATION = 32;
    final int SHOPPING = 38;
    final int RESTAURANT = 39;

    Context context = this;
    Activity activity = this;

    ScrollView scrollView;
    ImageView imgView;
    ImageButton heartbtn, commentsbtn;
    TextView tv_heartCount, tv_commentCount;
    TextView detail_overView, detail_spotName, detail_spotAddr;
    ImageButton filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;
    TextView tv_wheelchairs, tv_bathroom, tv_parkinglot, tv_elevator, tv_route, tv_braileblock, tv_handicapEtc;
    TextView tv_location;
    CircleImageView iv_profileImg;
    LinearLayout detailInfo_container;

    RelativeLayout detail_mapRl;
    double mapX,mapY;
    TMapView tmapView;
    ImageButton detail_directionBtn;

    Intent intent;
    String addr;
    String title;
    int detailInfoSize;
    int detailImageSize;

    DetailSpotListClickResponse.DetailCommon detailCommon;
    DetailSpotListClickResponse.DetailIntro detailIntro;
    ArrayList<DetailInfo> detailInfo;
    DetailWithTour detailWithTour;
    ArrayList<DetailImage> detailImage;
    OtherInfo otherInfo;

    ArrayList<SpotDetailImgFragment> imgList = new ArrayList<SpotDetailImgFragment>();
    SpotDetailImgFragment spotDetailImgFragment;
    ViewPager viewPager;
    MyFragmentAdapter mFragmentAdapter;
    String firstImgUri;

    String contentId, contentTypeId;

    //@BindView(R.id.touristSpot_detail_commentsbtn) ImageButton commentsbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot_detail);
        intent = this.getIntent();
        firstImgUri = intent.getStringExtra("firstImgUri");

        mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), imgList);

        contentId = intent.getStringExtra("contentId");
        contentTypeId = intent.getStringExtra("contentTypeId");
        addr = intent.getStringExtra("stringAddr");
        detailCommon = (DetailSpotListClickResponse.DetailCommon) intent.getSerializableExtra("detailCommon");
        detailIntro = (DetailSpotListClickResponse.DetailIntro) intent.getSerializableExtra("detailIntro");
        detailInfo = intent.getParcelableArrayListExtra("detailInfo");
        detailWithTour = (DetailWithTour) intent.getSerializableExtra("detailWithTour");
        detailImage = intent.getParcelableArrayListExtra("detailImage");
        otherInfo = (OtherInfo) intent.getSerializableExtra("otherInfo");

        try {
            detailInfoSize = detailInfo.size();
            detailImageSize = detailImage.size();
        }catch(NullPointerException ne){
            detailInfoSize = 0;
            detailImageSize = 0;
        }

        title = detailCommon.title;
        //해당 관광지 x,y좌표 저장
        mapX = Double.parseDouble(detailCommon.mapx);
        mapY = Double.parseDouble(detailCommon.mapy);
        //길안내 버튼
        detail_directionBtn = (ImageButton) findViewById(R.id.detail_directionBtn);
        detail_directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(context, MapCourseGuideActivity.class);
                mapIntent.putExtra("mapX", mapX);
                mapIntent.putExtra("mapY", mapY);
                mapIntent.putExtra("title", title);

                startActivity(mapIntent);
            }
        });

        scrollView = (ScrollView) findViewById(R.id.touristSpot_detail_scroll);
        heartbtn = (ImageButton) findViewById(R.id.touristSpot_detail_heartbtn);
        commentsbtn = (ImageButton) findViewById(R.id.touristSpot_detail_commentsbtn);
        tv_heartCount = (TextView) findViewById(R.id.touristSpot_detail_heartCount);
        tv_commentCount = (TextView) findViewById(R.id.touristSpot_detail_commentsCount);
        detail_mapRl = (RelativeLayout) findViewById(R.id.detail_mapRl);
        detailInfo_container = (LinearLayout) findViewById(R.id.detailInfo_container);
        iv_profileImg = (CircleImageView) findViewById(R.id.iv_profileImg);

        //각종 view 연결
        detail_spotName = (TextView) findViewById(R.id.detail_spotName);
        detail_spotAddr = (TextView) findViewById(R.id.detail_spotAddr);
        detail_overView = (TextView) findViewById(R.id.detail_overView);
        filter_bathroom = (ImageButton) findViewById(R.id.filter_bathroom);
        filter_elevator = (ImageButton) findViewById(R.id.filter_elevator);
        filter_parkinglot = (ImageButton) findViewById(R.id.filter_parkinglot);
        filter_wheelchairs = (ImageButton) findViewById(R.id.filter_wheelchairs);
        tv_bathroom = (TextView) findViewById(R.id.tv_bathroom);
        tv_elevator = (TextView) findViewById(R.id.tv_elevator);
        tv_parkinglot = (TextView) findViewById(R.id.tv_parkinglot);
        tv_wheelchairs = (TextView) findViewById(R.id.tv_wheelchairs);
        tv_route = (TextView) findViewById(R.id.tv_route);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_heartCount = (TextView) findViewById(R.id.touristSpot_detail_heartCount);
        tv_commentCount = (TextView) findViewById(R.id.touristSpot_detail_commentsCount);
        tv_braileblock = (TextView) findViewById(R.id.tv_braileblock);
        tv_handicapEtc = (TextView) findViewById(R.id.tv_handicapEtc);

        //contentTypeId별 프로필 이미지 변경
        try {
            checkContentType(Integer.parseInt(contentTypeId));
        }catch(Exception e){
            e.printStackTrace();
            iv_profileImg.setImageResource(R.drawable.default_image);
        }
        viewPager = (ViewPager) findViewById(R.id.touristSpot_detail_img_viewPager);
        viewPager.setAdapter(mFragmentAdapter);

        Log.d(TAG, "onCreate: firstImgUri: " + firstImgUri);
        SpotDetailImgFragment firstSpotDetailImgFragment = new SpotDetailImgFragment(this, firstImgUri);
        imgList.add(0, firstSpotDetailImgFragment);

        mFragmentAdapter.notifyDataSetChanged();

        /*Glide.with(context)
                .load(detailImage.get(0).originimgurl)
                .placeholder(R.drawable.sally)
                .error(R.drawable.sally)
                .into(firstSpotDetailImgFragment.iv_detailImg); // -> null인 이유?*/

        //받아온 데이터들 상세보기에 대입!
        try {
            detail_spotName.setText(detailCommon.title);
            detail_overView.setText(Html.fromHtml(detailCommon.overview).toString());
            detail_spotAddr.setText(addr);
            tv_location.setText(addr);
            /*
            * 이용 안내 setting
            */
            for(int i=0; i<detailInfoSize; i++){
                TextView tv_infoName = new TextView(this);
                tv_infoName.setText(detailInfo.get(i).infoname);
                tv_infoName.setTextColor(Color.parseColor("#1D1D1D"));
                tv_infoName.setTextSize(14);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                lp.setMargins(8, 13, 0, 0);
                tv_infoName.setLayoutParams(lp);

                TextView tv_infoText = new TextView(this);
                tv_infoText.setText(detailInfo.get(i).infotext);
                tv_infoText.setTextColor(Color.parseColor("#686868"));
                tv_infoText.setTextSize(14);
                tv_infoText.setLayoutParams(lp);

                detailInfo_container.addView(tv_infoName);
                tv_infoName.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Medium.otf"));
                detailInfo_container.addView(tv_infoText);
                tv_infoText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansCJKkr-Regular.otf"));
            }


            tv_heartCount.setText(String.valueOf(otherInfo.likecount));
            tv_commentCount.setText(String.valueOf(otherInfo.commentcount));

            Log.d(TAG, "onCreate: message: " + otherInfo.message);
            //하트 여부확인 및 표시
            if(otherInfo.message.equals("unlike")){
                Log.d(TAG, "onCreate: in 하트여부체크 off");
                heartbtn.setBackgroundResource(R.drawable.trips_heart_off);
            }else{
                Log.d(TAG, "onCreate: in 하트여부체크 on");
                heartbtn.setBackgroundResource(R.drawable.trips_heart_on);
            }

            /*
             * TODO 이미지 setting
             */
            for (int i=1; i<detailImageSize; i++){
                imgList.add(i, new SpotDetailImgFragment(this));

                mFragmentAdapter.notifyDataSetChanged();
            }
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Glide.with(context)
                            .load(detailImage.get(position+1).originimgurl)
                            .placeholder(R.drawable.trips_more)
                            .error(R.drawable.trips_more)
                            .into(imgList.get(position).iv_detailImg);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        //시설정보들 유무 검사, 이미지로 표시 메소드
        checkFacilities();

        //맵 생성 및 마커
        initTmap();

        //스크롤바 사용기능 설정
        scrollView.setVerticalScrollBarEnabled(true);

        commentsbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TouristSpotReview.class);
                intent.putExtra("contentId", contentId);
                intent.putExtra("contentTypeId", contentTypeId);
                intent.putExtra("spotName", detailCommon.title);
                startActivity(intent);
            }
        });
        tv_commentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TouristSpotReview.class);
                startActivity(intent);
            }
        });
    }

    //해당 시설 검증 및 표시
    public void checkFacilities() {
        try {
            if (!detailWithTour.elevator.equals(null)) {
                filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_on);
                tv_elevator.setText(detailWithTour.elevator);
            }
        } catch (NullPointerException ne) {
            filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_off);
            tv_elevator.setText("해당 시설 없음");
        }
        try {
            if (!detailWithTour.parking.equals(null)) {
                filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_on);
                tv_parkinglot.setText(detailWithTour.parking);
            }
        } catch (NullPointerException ne) {
            filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_off);
            tv_parkinglot.setText("해당 시설 없음");
        }
        try {
            if (!detailWithTour.restroom.equals(null)) {
                filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_on);
                tv_bathroom.setText(detailWithTour.restroom);
            }
        } catch (NullPointerException ne) {
            filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_off);
            tv_bathroom.setText("해당 시설 없음");
        }
        try {
            if (!detailWithTour.wheelchair.equals(null)) {
                filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_on);
                tv_wheelchairs.setText(detailWithTour.wheelchair);
            }
        } catch (NullPointerException ne) {
            filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_off);
            tv_wheelchairs.setText("해당 시설 없음");
        }
        try {
            if (!detailWithTour.route.equals("null")) {
                tv_route.setText(detailWithTour.route);
            }
        } catch (NullPointerException ne) {
            tv_route.setText("해당 시설 없음");
        }
        try{
            if(!detailWithTour.braileblock.equals("null")){
                tv_braileblock.setText(detailWithTour.braileblock);
            }
        }catch(NullPointerException e){
            tv_braileblock.setText("해당 시설 없음");
        }
        try{
            if(!detailWithTour.handicapetc.equals("null")){
                tv_handicapEtc.setText(detailWithTour.handicapetc);
            }
        }catch(NullPointerException e){
            tv_handicapEtc.setText("해당 시설 없음");
        }
    }

    //맵 초기화 및 생성
    public void initTmap(){
        tmapView = new TMapView(this);
        tmapView.setSKPMapApiKey(mTMapApiKey);
        tmapView.setCenterPoint(mapX,mapY);
        tmapView.setZoomLevel(15); //지도 ZoomLevel지정 작을 수록 넓은 범위 보여줌
        detail_mapRl.addView(tmapView);

        addMarker();

        //아이콘 및 지도 생성
        tmapView.setIconVisibility(true);
        tmapView.setSightVisible(true);
    }

    //마커 추가 메소드
    public void addMarker(){
        TMapPoint point = new TMapPoint(mapY,mapX);
        TMapMarkerItem marker = new TMapMarkerItem();
        marker.setTMapPoint(point);

        //풍선뷰 안의 항목에 글 지정
        marker.setCalloutTitle(title);
        marker.setCanShowCallout(true);
        marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_place_red));
//        marker.setAutoCalloutVisible(true); //풍선뷰 보일지 여부
        tmapView.addMarkerItem("marker", marker);
    }

    //FragmentAdapter
    private class MyFragmentAdapter extends FragmentPagerAdapter{
        private ArrayList<SpotDetailImgFragment> imgList;

        public MyFragmentAdapter(FragmentManager fm, ArrayList<SpotDetailImgFragment> imgList){
            super(fm);
            this.imgList = imgList;
        }

        @Override
        public Fragment getItem(int position) {
            try{
                return imgList.get(position);
            }catch(Exception e){
                e.printStackTrace();
                return new SpotDetailImgFragment();
            }
        }

        @Override
        public int getCount() {
            return imgList.size();
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


}
