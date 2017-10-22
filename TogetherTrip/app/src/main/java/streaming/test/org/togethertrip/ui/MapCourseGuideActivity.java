package streaming.test.org.togethertrip.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import streaming.test.org.togethertrip.R;

public class MapCourseGuideActivity extends AppCompatActivity {
    final static String TAG = "MapCourseGuideActivity";
    private final static String mTMapApiKey = "d9c128a3-3d91-3162-a305-e4b65bea1b55";

    RelativeLayout mapView;
    String title;
    double mapX, mapY;
    TMapView tmapView;

    Intent mapIntent;
    LocationManager locationManager;
    LocationListener locationListener;
    double currentX, currentY;
    String locationProvider;
    boolean CompleteFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_course_guide);
        mapView = (RelativeLayout) findViewById(R.id.mapView);

        CompleteFlag = false;

        mapIntent = getIntent();
        title = mapIntent.getStringExtra("title");
        mapX = mapIntent.getDoubleExtra("mapX", 0.0);
        mapY = mapIntent.getDoubleExtra("mapY", 0.0);

        initTmap();


    }

    //맵 초기화 및 생성
    public void initTmap() {
        tmapView = new TMapView(this);
        tmapView.setSKPMapApiKey(mTMapApiKey);
        tmapView.setCenterPoint(mapX, mapY);
        tmapView.setZoomLevel(13); //지도 ZoomLevel지정 작을 수록 넓은 범위 보여줌
        mapView.addView(tmapView);

        addMarker();

        //아이콘 및 지도 생성
        tmapView.setIconVisibility(true);
        tmapView.setTrackingMode(true); //현재 위치표시
        tmapView.setSightVisible(true);
    }

    //마커 추가 메소드
    public void addMarker() {
        TMapPoint point = new TMapPoint(mapY, mapX);
        TMapMarkerItem marker = new TMapMarkerItem();
        marker.setTMapPoint(point);

        //풍선뷰 안의 항목에 글 지정
        marker.setCalloutTitle(title);
        marker.setCanShowCallout(true);
        marker.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_place_red));
//        marker.setAutoCalloutVisible(true); //풍선뷰 보일지 여부
        tmapView.addMarkerItem("marker", marker);
    }

    //현재위치 받아오기
    public void currentLatLng() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 정확도
        criteria.setPowerRequirement(Criteria.POWER_HIGH); // 전원 소비량
        criteria.setAltitudeRequired(false); // 고도, 높이 값을 얻어 올지를 결정
        criteria.setBearingRequired(false);// provider 기본 정보(방위, 방향)
        criteria.setSpeedRequired(true); // 속도
        criteria.setCostAllowed(true); // 위치 정보를 얻어 오는데 들어가는 금전적 비용
        locationProvider = locationManager.getBestProvider(criteria, true);

        //GPS 프로바이더를 통해 위치를 받도록 설정
        //2초 간격으로 위치 업데이트
        //위치 정보를 업데이트할 최소 거리 0m
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, 2000, 0, locationListener);

        try{
            
        }catch(Exception e){
            e.printStackTrace();
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentX = location.getLatitude();
                currentY = location.getLongitude();

                if(!CompleteFlag){

                }else{

                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


    }

    //지도에 경로 그리기
    public void drawLine(){
        TMapPoint startPoint = new TMapPoint(currentX, currentY);
        TMapPoint endPoint = new TMapPoint(mapX, mapY);

        final TMapData tmapData = new TMapData();
        try {
            tmapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
