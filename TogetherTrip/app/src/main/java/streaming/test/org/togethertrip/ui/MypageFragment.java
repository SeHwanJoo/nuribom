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
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.UserInfoResult;
import streaming.test.org.togethertrip.network.NetworkService;

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

    String email, profileImg, nickName, token;

    TextView loginOrLogout;
    TextView signUpOrSignIn, settings_profile;
    TextView mywrite_course, mywrite_review, myLocker;

    CircleImageView userProfile;

    LogoutDialog logoutDialog;

    Intent intent;
    String imgUrl;
    Uri uri;

    String profileImgString;

    SharedPreferences loginInfo;


    public MypageFragment(Activity activity, String email, String profileImg, String nickName, String token){
        this.activity = activity;
        this.context = activity;
        this.email = email;
        this.profileImgString = profileImg;
        this.nickName = nickName;
        this.token = token;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_mypage, container, false);
        View view = null;

        checkLogin();
        try {
            if (userInfoResult.message.equals("no") ) { // 로그인이 안되어있을 때
                view = inflater.inflate(R.layout.mypage_nologin, container, false);

                loginOrLogout = (TextView) view.findViewById(R.id.settings_login);
                signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);

                loginOrLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(activity, SigninActivity.class));
                    }
                });
                signUpOrSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(activity, SignupActivity.class));
                    }
                });

            }else{ // 로그인이 되어있을 때
                view = inflater.inflate(R.layout.activity_mypage, container, false);

                loginOrLogout = (TextView) view.findViewById(R.id.settings_logout);
                mywrite_course = (TextView) view.findViewById(R.id.mywrite_course);
                mywrite_review = (TextView) view.findViewById(R.id.mywrite_review);
                myLocker = (TextView) view.findViewById(R.id.mylocker);
                settings_profile = (TextView) view.findViewById(R.id.settings_profile);
                userProfile = (CircleImageView) view.findViewById(R.id.userProfile);
                TextView userNickName = (TextView) view.findViewById(R.id.userNickName);
                TextView userEmail = (TextView) view.findViewById(R.id.userEmail);

                userEmail.setText(email);
                userNickName.setText(nickName);
                Log.d(TAG, "onCreateView: image in Mypage: " + profileImg);

                if(profileImg == null) { // profileImge가 널일때 디폴트 이미지로
                    userProfile.setImageResource(R.drawable.mypage_profile_defalt);
                }else{ // 그렇지 않으면 해당 이미지로
                    Glide.with(context).load(profileImgString).into(userProfile);
                }

                mywrite_course.setText(""+userInfoResult.result.course);
                mywrite_review.setText(""+(userInfoResult.result.coursecomment+userInfoResult.result.tripreviews));
                myLocker.setText(""+(userInfoResult.result.courselike+userInfoResult.result.triplike));

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
                    }
                });
            }
        }catch(Exception e){
            //로그인 유무 체크 중 예외 발생시 디폴트로 No Login으로 띄우기
            e.printStackTrace();
            view = inflater.inflate(R.layout.mypage_nologin, container, false);

            loginOrLogout = (TextView) view.findViewById(R.id.settings_login);
            signUpOrSignIn = (TextView) view.findViewById(R.id.settings_signup);
            loginOrLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, SigninActivity.class));
                }
            });
            signUpOrSignIn.setOnClickListener(new View.OnClickListener() {
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

    //첫 진입시 로그인 유무확인 네트워킹
    public void checkLogin(){

        NetworkService networkService = ApplicationController.getInstance().getNetworkService();

        if(nickName == null){
            nickName="";
        }

        Log.d(TAG, "checkLogin: email: " + nickName);

        Call<UserInfoResult> requestDriverApplyOwner = networkService.getUserInfo(nickName);
        requestDriverApplyOwner.enqueue(new Callback<UserInfoResult>() {
            @Override
            public void onResponse(Call<UserInfoResult> call, Response<UserInfoResult> response) {
                if (response.isSuccessful()) {
                    userInfoResult = response.body();

                    Log.d(TAG, "onResponse: result: " + userInfoResult.result);
                    Log.d(TAG, "onResponse: message : " + userInfoResult.message);
                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                if(nickName==null){
                    Toast.makeText(activity, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "네트워크가 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //로그아웃 네트워킹
    public void logout(){

        NetworkService networkService = ApplicationController.getInstance().getNetworkService();

        nickName = "";

        Call<UserInfoResult> requestDriverApplyOwner = networkService.getUserInfo(nickName);
        requestDriverApplyOwner.enqueue(new Callback<UserInfoResult>() {
            @Override
            public void onResponse(Call<UserInfoResult> call, Response<UserInfoResult> response) {
                if (response.isSuccessful()) {
                    userInfoResult = response.body();

                    Log.d(TAG, "onResponse: logout result: " + userInfoResult.result);
                    Log.d(TAG, "onResponse: logout message : " + userInfoResult.message);
                } else {
                    Log.d(TAG, "onResponse: search response is not success");
                }
            }
            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
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
            Intent intent = new Intent(context, MainActivity.class);

            //로그인시 자동로그인 내역 삭제
            loginInfo = activity.getSharedPreferences("loginSetting", 0);
            SharedPreferences.Editor editor = loginInfo.edit();
            editor.clear();
            editor.commit();

            //앞서 쌓여있던 NoLogin된 메인 액티비티 제거하라는 플래그
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            logoutDialog.dismiss();


        }
    };

    private void pickImage(){
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
                    getImageNameToUri(data.getData());
                    profileImgString = data.getDataString();
                    uri = data.getData();
                    Glide.with(context)
                            .load(data.getDataString())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(userProfile);
                }catch(Exception e){
                    e.printStackTrace();
                }

                if(imgUrl == ""){
                    ((CourseWriteFragment.DataSetListner) activity).FirstFragmentImageSet(null);
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

                        ((CourseWriteFragment.DataSetListner) activity).FirstFragmentImageSet(MultipartBody.Part.createFormData("image", photo.getName(), phtoBody));

                        Glide.with(context)
                                .load(data.getDataString())
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(userProfile);
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        //자원 정리
                        if(baos!=null)  try{ baos.close();} catch(Exception e) {}
                        if(in!=null) try{ in.close(); } catch(Exception e) {}
                    }

                }
            }
        }
    }
}
