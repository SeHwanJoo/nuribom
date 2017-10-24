package streaming.test.org.togethertrip.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.tmapapi.GeometryDeserializer;
import streaming.test.org.togethertrip.datas.tmapapi.TmapAPIResult;
import streaming.test.org.togethertrip.datas.tmapapi.TmapGeometry;
import streaming.test.org.togethertrip.ui.MainActivity;

/**
 * Created by taehyung on 2017-10-22.
 */

public class TMapNetworkManager {
    private final String TAG = "TMAPNetworkManagerLog";
    private static TMapNetworkManager instance;
    private static final String tmapApiKey = "d9c128a3-3d91-3162-a305-e4b65bea1b55";
    public static final String TMAP_SERVER = "http://apis.skplanetx.com/tmap";
    private static final String SEARCH_FIND_PATH_URL = TMAP_SERVER + "/routes/pedestrian?callback=&version=1";
    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAIL = 0;
    private static final int DEFAULT_CACHE_SIZE = 50*1024*1024;
    private static final String DEFAULT_CACHE_DIR = "miniapp";

    OkHttpClient mClient;
    Context context;

    public static TMapNetworkManager getInstance(){
        if(instance == null){
            instance = new TMapNetworkManager();
        }
        return instance;
    }

    private TMapNetworkManager(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        this.context= MainActivity.getContext();
        CookieManager cookieManager = new CookieManager();
        builder.cookieJar(new JavaNetCookieJar(cookieManager)); // 메모리 저장하는 쿠키

        File dir = new File(context.getExternalCacheDir(), DEFAULT_CACHE_DIR);
        if(!dir.exists()){
            dir.mkdir();
        }

        builder.cache(new Cache(dir, DEFAULT_CACHE_SIZE));

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        mClient = builder.build();
    }

    public interface OnResultListner<T>{
        public void onSuccess(Request request, T result);
        public void onFail(Request request, IOException exception);
    }

    class NetworkHandler extends Handler{
        public NetworkHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NetworkResult result = (NetworkResult) msg.obj;

            switch(msg.what){
                case MESSAGE_SUCCESS:
                    result.listner.onSuccess(result.request, result.result);
                    break;
                case MESSAGE_FAIL:
                    result.listner.onFail(result.request, result.exception);
                    break;
            }
        }
    }

    NetworkHandler mHandler = new NetworkHandler(Looper.getMainLooper());
    static class NetworkResult<T>{
        Request request;
        OnResultListner<T> listner;
        IOException exception;
        T result;
    }

    Gson gson = new Gson();
    Gson routeGson = new GsonBuilder().registerTypeAdapter(TmapGeometry.class, new GeometryDeserializer()).create();


    public Request getSearchFindPath(Object tag, double startX, double startY, double endX, double endY, String startName, String endName,
                                     OnResultListner<TmapAPIResult> listner) throws UnsupportedEncodingException{
        final RequestBody body = new FormBody.Builder() // 바디 설정
                .add("startX", startX+"")
                .add("startY", startY+"")
                .add("endX", endX + "")
                .add("endY", endY + "")
                .add("startName", startName)
                .add("endName", endName)
                .add("reqCoordType", "WGS84GEO")
                .add("resCoordType", "WGS84GEO")
                .build();

        Request request = new Request.Builder()
                .url(SEARCH_FIND_PATH_URL)
                .addHeader("Accept", "application/json")
                .addHeader("appKey", tmapApiKey)
                .post(body)
                .build();

        final NetworkResult<TmapAPIResult> result = new NetworkResult<>();
        result.request = request;
        result.listner = listner;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: okhttp 통신 onFailure" + e);
                result.exception = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    TmapAPIResult data = new TmapAPIResult();

                    Log.d(TAG, "onResponse: 통신 성공" + data);

                    try{
                        data= routeGson.fromJson(response.body().charStream(), TmapAPIResult.class);
                        Log.d(TAG, "onResponse: data 셋팅 try : " + data);
                    }catch(JsonSyntaxException e){
                        e.printStackTrace();
                    }finally{
                        if(response.body()!= null) try{ response.body().close(); } catch(Exception e) { e.printStackTrace(); };
                    }

                    if(data!=null){
                        result.result = data;
                    }

                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                }else{
                    throw new IOException(response.message());
                }
            }
        });

        Log.d(TAG, "getSearchFindPath: request 반환 직전: " + request);

        return request;


    }




}
