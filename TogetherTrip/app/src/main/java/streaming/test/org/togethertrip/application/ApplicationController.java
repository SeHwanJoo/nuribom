package streaming.test.org.togethertrip.application;

import android.app.Application;
import android.content.Context;

import com.tsengvn.typekit.Typekit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import streaming.test.org.togethertrip.network.NetworkService;

/**
 * Created by taehyung on 2017-09-04.
 */

public class ApplicationController extends Application {

    private static ApplicationController instance;    // 먼저 어플리케이션 인스턴스 객체를 하나 선언

    private static String baseUrl = "http://13.124.160.125:3000/";  // 베이스 url 초기화

    private NetworkService networkService;                        // 네트워크 서비스 객체 선언

    private static Context context;

    public static ApplicationController getInstance() {
        return instance;
    }    // 인스턴스 객체 반환  왜? static 안드에서 static 으로 선언된 변수는 매번 객체를 새로 생성하지 않아도 다른 액티비티에서
    //자유롭게 사용가능합니다.

    public NetworkService getNetworkService() {
        return networkService;
    }    // 네트워크서비스 객체 반환

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        ApplicationController.instance = this; //인스턴스 객체 초기화
        buildService();

        //폰트 정의
        Typekit.getInstance()
                .addCustom1(Typekit.createFromAsset(this, "fonts/NotoSansCJKkr-Regular.otf"))
                .addCustom2(Typekit.createFromAsset(this, "fonts/NotoSansCJKkr-Medium.otf"))
                .addCustom3(Typekit.createFromAsset(this, "fonts/NotoSansCJKkr-Bold.otf"));
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    public static Context getContext(){
        return context;
    }

}
