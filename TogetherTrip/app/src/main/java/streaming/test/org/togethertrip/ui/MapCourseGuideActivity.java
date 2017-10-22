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
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

import streaming.test.org.togethertrip.R;

public class MapCourseGuideActivity extends AppCompatActivity {
    final static String TAG = "MapCourseGuideActivity";
    private final static String mTMapApiKey = "d9c128a3-3d91-3162-a305-e4b65bea1b55";

    RelativeLayout mapView;
    String title;
    double mapX, mapY; // 해당 관광지의 좌표
    TMapView tmapView;

    Intent mapIntent;
    LocationManager locationManager;
    LocationListener locationListener;
    double currentX, currentY; // 현재 내 위치
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
        currentLatLng();


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

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                //권한 없을 시 권한 주기
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(MapCourseGuideActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(MapCourseGuideActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.with(this)
                        .setPermissionListener(permissionListener)
                        .setRationaleMessage("위치 정보를 받아오려면 권한이 필요합니다.")
                        .setDeniedMessage("거부하지마세용")
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                        .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .check();
            }
            try {
                Location location = new Location(LocationManager.NETWORK_PROVIDER);
                currentX = location.getLatitude();
                currentY = location.getLongitude();
            }catch(Exception e1){
                e1.printStackTrace();
            }
            try{
                Location location = new Location(LocationManager.GPS_PROVIDER);
                currentX = location.getLatitude();
                currentY = location.getLongitude();
            }catch(Exception e1){
                e1.printStackTrace();
            }
            Log.d(TAG, "onLocationChanged: NETWORK PROVIDER : " + currentX + " / " + currentY);

            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                //네트워크 이용 현재 위치 갱신
                //2초 간격으로 위치 업데이트
                //위치 정보를 업데이트할 최소 거리 0m
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        currentX = location.getLatitude();
                        currentY = location.getLongitude();
                        Log.d(TAG, "onLocationChanged: NETWORK PROVIDER : " + currentX + " / " + currentY);
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
                });


            }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //GPS이용 현재 위치 갱신
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        currentX = location.getLatitude();
                        currentY = location.getLongitude();

                        Log.d(TAG, "onLocationChanged: GPS PROVIDER : " + currentX + " / " + currentY);
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
                });
            }else{
                Log.d(TAG, "currentLatLng: 여기로 들어오면 안되는데... 제발 앞에 조건에서 걸리게해주세요");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
