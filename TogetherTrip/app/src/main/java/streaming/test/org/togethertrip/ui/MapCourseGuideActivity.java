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
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.Request;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.network.TMapNetworkManager;
import streaming.test.org.togethertrip.datas.tmapapi.TmapAPIResult;

public class MapCourseGuideActivity extends AppCompatActivity {
    final static String TAG = "MapCourseGuideActivity";
    private final static String mTMapApiKey = "d9c128a3-3d91-3162-a305-e4b65bea1b55";
    private final int FINDPATH = 0;

    RelativeLayout mapView;
    String title;
    double mapX, mapY; // 해당 관광지의 좌표
    TMapView tmapView;
    ArrayList<TMapPoint> passList;
    TmapAPIResult RouteResult;
    int Idx;


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
        passList = new ArrayList<>();

        mapIntent = getIntent();
        title = mapIntent.getStringExtra("title");
        mapY = mapIntent.getDoubleExtra("mapX", 0.0);
        mapX = mapIntent.getDoubleExtra("mapY", 0.0);

        Idx = 0;

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
            //Network 이용 현재 위치 받아오기
            try {
                Location location = new Location(LocationManager.NETWORK_PROVIDER);
                currentX = location.getLatitude();
                currentY = location.getLongitude();
            }catch(Exception e1){
                e1.printStackTrace();
            }

            //GPS이용 현재 위치 받아오기
            try{
                Location location = new Location(LocationManager.GPS_PROVIDER);
                currentX = location.getLatitude();
                currentY = location.getLongitude();
            }catch(Exception e1){
                e1.printStackTrace();
            }
            
            Log.d(TAG, "onLocationChanged: NETWORK PROVIDER : " + currentX + " / " + currentY);

            try{
                //Tmap 보호자 경로 탐색
                Log.d(TAG, "currentLatLng: 보호자 경로 검색 try");
                Log.d(TAG, "currentLatLng: getContext: " +MainActivity.getContext());
                TMapNetworkManager.getInstance().getSearchFindPath(MainActivity.getContext(), currentY, currentX,mapY, mapX, "상수네집", "광화문", new TMapNetworkManager.OnResultListner<TmapAPIResult>() {
                    @Override
                    public void onSuccess(Request request, TmapAPIResult result) {
                        Toast.makeText(MapCourseGuideActivity.this, "Tmap 경로 탐색 성공", Toast.LENGTH_SHORT).show();
                        RouteResult = result;

                        for(int i=0;i<result.features.size(); i++){
                            if(result.features.get(i) != null && i != 0){
                                if(result.features.get(i).geometry.type.equals("Point")){
                                    TMapPoint point = new TMapPoint(result.features.get(i).geometry.coordinates[1], result.features.get(i).geometry.coordinates[0]);
                                    passList.add(point);
                                }
                            }
                        }
//                        //TTS 음성 라이브러리 사용
//                        if(Idx == 0 && RouteResult.features != null) {
//
//                            String toSpeak = RouteResult.features.get(Idx).properties.description + "해주세요.";
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                ttsGreater21(toSpeak);
//                            } else {
//                                ttsUnder20(toSpeak);
//                            }
//
//                            Idx++;
//                        }
                    }

                    @Override
                    public void onFail(Request requst, IOException exception) {
                        Toast.makeText(MapCourseGuideActivity.this, "Tmap 경로 탐색 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }

            drawLine();

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

                        //경로 탐색 시작
                        drawLine();
//                        mHandler.sendEmptyMessage(FINDPATH); // 경로 요청
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

                        //경로 탐색 시작
                        drawLine();
//                        mHandler.sendEmptyMessage(FINDPATH); // 경로 요청
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

    public void drawLine(){
        Log.d(TAG, "drawLine: 진입");
        TMapPoint startPoint = new TMapPoint(currentX, currentY);
        TMapPoint endPoint = new TMapPoint(mapX, mapY);

        Log.d(TAG, "drawLine: TMapPoint: " + startPoint + " / " + endPoint);

        final TMapData tmapData = new TMapData();
        tmapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, passList, 0, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                tmapView.removeAllTMapPolyLine();
                tmapView.setLocationPoint(currentY, currentX);
                tmapView.setCompassMode(true);
                tmapView.addTMapPath(tMapPolyLine);
            }
        });
    }

    //TMap 경로 탐색 및 표시
    public void findPath(){
        TMapData tmapData = new TMapData();
        tmapView.setLocationPoint(currentY, currentX);
        tmapView.setCenterPoint(currentY, currentX);
        tmapView.setCompassMode(true);

        TMapPoint startPoint = new TMapPoint(currentX, currentY);
        TMapPoint endPoint = new TMapPoint(mapX, mapY);
        Log.d(TAG, "findPath: startPoint: " + startPoint);
        Log.d(TAG, "findPath: endPoint: " + endPoint);

        try{
            TMapPolyLine polyLine = tmapData.findPathData(startPoint, endPoint);
            tmapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startPoint, endPoint, passList, 0, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {
                    tmapView.removeAllTMapPolyLine();
                    tmapView.addTMapPath(tMapPolyLine);
                }
            });
            Log.d(TAG, "findPath: polyLine: " + polyLine);
            tmapView.addTMapPolyLine("findPath", polyLine);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

//    private Handler mHandler = new Handler(){
//        public void handleMessage(Message msg){
//            if(msg.what == FINDPATH) { // 경로찾기
//                //TMap 표시
//                findPath();
//            }
//            super.handleMessage(msg);
//        }
//    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
