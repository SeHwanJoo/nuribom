package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HEAD;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.ResultMessage;
import streaming.test.org.togethertrip.datas.UserInfoResult;
import streaming.test.org.togethertrip.network.NetworkService;
import streaming.test.org.togethertrip.ui.notice.NoticeActivity;

import static streaming.test.org.togethertrip.R.id.textView13;

/**
 * Created by taehyung on 2017-09-06.
 */

public class MypageFragment extends Fragment {
    private static final String TAG = "MypageFragmentLog";
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    Activity activity;
    Context context;


    NetworkService networkservice;
    UserInfoResult userInfoResult;
    String checkString;

    String userId;

    TextView loginOrLogout;
    TextView signUpOrSignIn, settings_profile;
    TextView mywrite_course, mywrite_review, myLocker;

    LinearLayout ll_logout;
    FrameLayout ll_login;

    CircleImageView userProfile;

    LogoutDialog logoutDialog;

    Intent intent;
    String imgUrl;
    Uri uri;


    SharedPreferences loginInfo;


    public MypageFragment(Activity activity){
        this.activity = activity;
        context = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        loginInfo = activity.getSharedPreferences("loginSetting", 0);

        userId = loginInfo.getString("nickname","");

        networkservice = ApplicationController.getInstance().getNetworkService();

        final View view = inflater.inflate(R.layout.activity_mypage, container, false);

        if(!(userId.equals(""))){
            Call<UserInfoResult> requestMypage = networkservice.getUserInfo(userId);
            requestMypage.enqueue(new Callback<UserInfoResult>() {
                @Override
                public void onResponse(Call<UserInfoResult> call, Response<UserInfoResult> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "reponse.body: " + response.body().message);
                        if (response.body().message.equals("yes")) {


                            ll_logout = (LinearLayout)view.findViewById(R.id.ll_logout);
                            loginOrLogout = (TextView) view.findViewById(R.id.settings_logout);
                            mywrite_course = (TextView) view.findViewById(R.id.mywrite_course);
                            mywrite_review = (TextView) view.findViewById(R.id.mywrite_review);
                            myLocker = (TextView) view.findViewById(R.id.mylocker);
                            settings_profile = (TextView) view.findViewById(R.id.settings_profile);
                            userProfile = (CircleImageView) view.findViewById(R.id.userProfile);
                            TextView userNickName = (TextView) view.findViewById(R.id.userNickName);
                            TextView userEmail = (TextView) view.findViewById(R.id.userEmail);

                            ll_logout.setVisibility(View.GONE);
                            userEmail.setText(response.body().userdata.email);
                            userNickName.setText(response.body().userdata.userid);

                            if(response.body().userdata.img == null) { // profileImge가 널일때 디폴트 이미지로
                                userProfile.setImageResource(R.drawable.mypage_profile_defalt);
                            }else{ // 그렇지 않으면 해당 이미지로
                                Glide.with(context).load(response.body().userdata.img).into(userProfile);
                            }

                            mywrite_course.setText(""+response.body().result.course);
                            mywrite_review.setText(""+(response.body().result.coursecomment+response.body().result.tripreviews));
                            myLocker.setText(""+(response.body().result.courselike+response.body().result.triplike));

                            loginOrLogout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    logoutDialog = new LogoutDialog(context, leftListner, rightLisnter);
                                    logoutDialog.show();
                                }
                            });

                            settings_profile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                        startActivity(new Intent(context, ProfileChangeActivity.class));
                                    pickImage();
                                    network_modify();

                                }
                            });
                        }else{
                            ll_login = (FrameLayout)view.findViewById(R.id.ll_login);
                            loginOrLogout = (TextView) view.findViewById(R.id.settings_logout);
                            signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);
                            settings_profile = (TextView) view.findViewById(R.id.settings_profile);

                            ll_login.setVisibility(View.GONE);
                            loginOrLogout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(activity, SigninActivity.class));
                                }
                            });
                            loginOrLogout.setText("로그인");
                            settings_profile.setText("회원가입");
                            settings_profile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(activity, SignupActivity.class));
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserInfoResult> call, Throwable t) {
                    Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                    ll_login = (FrameLayout)view.findViewById(R.id.ll_login);
                    loginOrLogout = (TextView) view.findViewById(R.id.settings_logout);
                    signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);
                    settings_profile = (TextView) view.findViewById(R.id.settings_profile);

                    ll_login.setVisibility(View.GONE);
                    loginOrLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, SigninActivity.class));
                        }
                    });
                    loginOrLogout.setText("로그인");
                    settings_profile.setText("회원가입");
                    settings_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, SignupActivity.class));
                        }
                    });
                }
            });
        } else{
            ll_login = (FrameLayout)view.findViewById(R.id.ll_login);
            loginOrLogout = (TextView) view.findViewById(R.id.settings_logout);
            signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);
            settings_profile = (TextView) view.findViewById(R.id.settings_profile);

            ll_login.setVisibility(View.GONE);
            loginOrLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SigninActivity.class));
                }
            });
            loginOrLogout.setText("로그인");
            settings_profile.setText("회원가입");
            settings_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SignupActivity.class));
                }
            });
        }
        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public synchronized void network_modify(){
        if(imgUrl == null){
            uri = null;
            Log.i("im","짱구");
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 2; // 1/2로 크기를 줄이겠다는 뜻

            InputStream in = null;
            ByteArrayOutputStream baos = null;

            try{
                in = context.getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);

                RequestBody phtoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                File photo = new File(imgUrl);

                MultipartBody.Part image = MultipartBody.Part.createFormData("image", photo.getName(), phtoBody);

                RequestBody userid = RequestBody.create(MediaType.parse("multipart/from-data"),userId);
                networkservice = ApplicationController.getInstance().getNetworkService();
                Call<ResultMessage> modifyProfile = networkservice.modifyProfile(image,userid);
                modifyProfile.enqueue(new Callback<ResultMessage>() {
                    @Override
                    public void onResponse(Call<ResultMessage> call, Response<ResultMessage> response) {
                        if (response.isSuccessful()) {
                            if(response.body().message.equals("ok")){
                                Toast.makeText(context,"프로필 사진이 변경되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }
                    }
                    @Override
                    public void onFailure(Call<ResultMessage> call, Throwable t) {
                    }
                });



            }catch(Exception e){
                e.printStackTrace();
            }finally{
                //자원 정리
                if(baos!=null)  try{ baos.close();} catch(Exception e) {}
                if(in!=null) try{ in.close(); } catch(Exception e) {}
            }

        }
    }

    //로그아웃 네트워킹
    public void logout(){

        Call<ResultMessage> requestDriverApplyOwner = networkservice.requestLogout(userId);
        requestDriverApplyOwner.enqueue(new Callback<ResultMessage>() {
            @Override
            public void onResponse(Call<ResultMessage> call, Response<ResultMessage> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: logout result: " + userInfoResult.result);
                    Log.d(TAG, "onResponse: logout message : " + userInfoResult.message);
                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<ResultMessage> call, Throwable t) {
                Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //다이얼로그 취소 클릭시
    private View.OnClickListener leftListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            logoutDialog.dismiss();
        }
    };

    //다이얼로그 로그아웃 클릭시
    private View.OnClickListener rightLisnter = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            logout();
            //로그인시 자동로그인 내역 삭제
            SharedPreferences.Editor editor = loginInfo.edit();
            editor.clear();
            editor.commit();

            //앞서 쌓여있던 NoLogin된 메인 액티비티 제거하라는 플래그
            logoutDialog.dismiss();
            startActivity(new Intent(context,MainActivity.class));
        }
    };

    private synchronized void pickImage(){
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);

    }

    //프로필 변경 네트워크
    public void profileChangeNetwork(){

    }

    public void getImageNameToUri(Uri data){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        this.imgUrl = imgPath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                try{
                    uri = data.getData();
                    Glide.with(context)
                            .load(data.getDataString())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(userProfile);

                }catch(Exception e){
                    e.printStackTrace();
                }
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(data.getData(), proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                String imgPath = cursor.getString(column_index);
                String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
                this.imgUrl = imgPath;
                network_modify();


            }
        }
    }

}
