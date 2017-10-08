package streaming.test.org.togethertrip.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.DetailSpotListClickResponse;
import streaming.test.org.togethertrip.datas.DetailWithTour;
import streaming.test.org.togethertrip.datas.MapPoint;

public class TouristSpotDetail extends AppCompatActivity {
    final static String TAG = "TouristSpotDetailErr";

    ScrollView scrollView;
    ImageView imgView;
    ImageButton heartbtn, commentsbtn;
    TextView hearttxt, commentstxt;
    TextView detail_overView, detail_spotName, detail_spotAddr;
    ImageButton filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;
    TextView tv_wheelchairs, tv_bathroom, tv_parkinglot, tv_elevator, tv_route;

    RelativeLayout detail_mapRl;
    TMapData tmapData = null;
    MapPoint mapPoint;
    TMapView tmapView;

    Intent intent;
    String addr;

    DetailSpotListClickResponse.DetailCommon detailCommon;
    DetailSpotListClickResponse.DetailIntro detailIntro;
    DetailSpotListClickResponse.DetailInfo detailInfo;
    DetailWithTour detailWithTour;

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

        scrollView = (ScrollView) findViewById(R.id.touristSpot_detail_scroll);
        imgView = (ImageView) findViewById(R.id.touristSpot_detail_img);
        heartbtn = (ImageButton) findViewById(R.id.touristSpot_detail_heartbtn);
        commentsbtn = (ImageButton) findViewById(R.id.touristSpot_detail_commentsbtn);
        hearttxt = (TextView) findViewById(R.id.touristSpot_detail_hearttxt);
        commentstxt = (TextView) findViewById(R.id.touristSpot_detail_commentstxt);
        detail_mapRl = (RelativeLayout) findViewById(R.id.detail_mapRl);

        //받아온 데이터들 상세보기에 대입!
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

        try {
            detail_spotName.setText(detailCommon.title);
            detail_overView.setText(Html.fromHtml(detailCommon.overview).toString());
            detail_spotAddr.setText(addr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //시설정보들 유무 검사, 이미지로 표시 메소드
        checkFacilities();

        //주소를 통해 좌표값 얻기
        convertToAddr();
        showMarkerPoint();

        tmapView = new TMapView(this);
        tmapView.setSKPMapApiKey("d9c128a3-3d91-3162-a305-e4b65bea1b55");
        tmapView.setCompassMode(true);
        tmapView.setIconVisibility(true);
        tmapView.setZoomLevel(15);
        tmapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapView.setTrackingMode(true);
        tmapView.setSightVisible(true);

        detail_mapRl.addView(tmapView);

        //스크롤바 사용기능 설정
        scrollView.setVerticalScrollBarEnabled(true);

        commentsbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TouristSpotReview.class);
                startActivity(intent);
            }
        });
        commentstxt.setOnClickListener(new View.OnClickListener() {
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
    }

    //마커 찍는 메소드
    public void showMarkerPoint() {
        mapPoint = new MapPoint("강남", 37.510350, 127.066847);

        TMapPoint point = new TMapPoint(mapPoint.getLatitude(),
                mapPoint.getLongitude());
        TMapMarkerItem item1 = new TMapMarkerItem();
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);

        item1.setTMapPoint(point);
        item1.setName(mapPoint.getName());
        item1.setVisible(item1.VISIBLE);
        item1.setIcon(bitmap);

        tmapView.addMarkerItem("locationPoint", item1);
    }


    //명칭 검색을 통해 마커찍기
    public void convertToAddr() {
//        TMapData tmapData = new TMapData();
//        tmapData.findAllPOI(addr, new TMapData.FindAllPOIListenerCallback() {
//            @Override
//            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
//                for(int i=0; i<arrayList.size();i++){
//                    TMapPOIItem item= arrayList.get(i);
//
//                    Log.d(TAG, "onFindAllPOI: 주소로찾기: " + item.getPOIPoint().toString());
//                }
//            }
//        });
    }

}
