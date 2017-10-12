package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResponse;
import streaming.test.org.togethertrip.datas.DetailWithTour;
import streaming.test.org.togethertrip.datas.OtherInfo;

public class TouristSpotDetail extends AppCompatActivity {
    final static String TAG = "TouristSpotDetailLog";
    private final static String mTMapApiKey = "d9c128a3-3d91-3162-a305-e4b65bea1b55";

    ScrollView scrollView;
    ImageView imgView;
    ImageButton heartbtn, commentsbtn;
    TextView tv_heartCount, tv_commentCount;
    TextView detail_overView, detail_spotName, detail_spotAddr;
    ImageButton filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;
    TextView tv_wheelchairs, tv_bathroom, tv_parkinglot, tv_elevator, tv_route, tv_braileblock, tv_handicapEtc;
    TextView tv_location;

    RelativeLayout detail_mapRl;
    double mapX,mapY;
    TMapView tmapView;

    Intent intent;
    String addr;
    String title;

    DetailSpotListClickResponse.DetailCommon detailCommon;
    DetailSpotListClickResponse.DetailIntro detailIntro;
    DetailSpotListClickResponse.DetailInfo detailInfo;
    DetailWithTour detailWithTour;
    OtherInfo otherInfo;

    //@BindView(R.id.touristSpot_detail_commentsbtn) ImageButton commentsbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot_detail);
        intent = this.getIntent();

        addr = intent.getStringExtra("stringAddr");
        detailCommon = (DetailSpotListClickResponse.DetailCommon) intent.getSerializableExtra("detailCommon");
        detailIntro = (DetailSpotListClickResponse.DetailIntro) intent.getSerializableExtra("detailIntro");
        detailWithTour = (DetailWithTour) intent.getSerializableExtra("detailWithTour");
        otherInfo = (OtherInfo) intent.getSerializableExtra("otherInfo");

        title = detailCommon.title;
        //해당 관광지 x,y좌표 저장
        mapX = Double.parseDouble(detailCommon.mapx);
        mapY = Double.parseDouble(detailCommon.mapy);
        Log.d(TAG, "onCreate: " + mapX + " / " + mapY);

        scrollView = (ScrollView) findViewById(R.id.touristSpot_detail_scroll);
        imgView = (ImageView) findViewById(R.id.touristSpot_detail_img);
        heartbtn = (ImageButton) findViewById(R.id.touristSpot_detail_heartbtn);
        commentsbtn = (ImageButton) findViewById(R.id.touristSpot_detail_commentsbtn);
        tv_heartCount = (TextView) findViewById(R.id.touristSpot_detail_heartCount);
        tv_commentCount = (TextView) findViewById(R.id.touristSpot_detail_commentsCount);
        detail_mapRl = (RelativeLayout) findViewById(R.id.detail_mapRl);

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

        //받아온 데이터들 상세보기에 대입!
        try {
            /*
             * 이미지 setting
             */
            detail_spotName.setText(detailCommon.title);
            detail_overView.setText(Html.fromHtml(detailCommon.overview).toString());
            detail_spotAddr.setText(addr);
            tv_location.setText(addr);
            /*
            * 이용 안내 setting
            */

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
}
