package streaming.test.org.togethertrip.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yongbeam.y_photopicker.util.photopicker.PhotoPickerActivity;
import com.yongbeam.y_photopicker.util.photopicker.utils.YPhotoPickerIntent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.ReviewResult;
import streaming.test.org.togethertrip.network.NetworkService;

/*
    2017-10-01
    Created by mingseung
 */

public class TouristSpotReviewWrite extends AppCompatActivity {

    final static String TAG = "ReviewActicityLog";
    public final static int REQUEST_CODE = 1;
    final int REQ_CODE_SELECT_IMAGE = 100;
    ArrayList<String> imgUrls = null;
    private Uri[] data;
    String edit_userid;
    String edit_contentid;
    String edit_date;
    String msg;
    //EditText edit_stars;
    TextView textViewDate;
    private ImageView[] img;
    RatingBar ratingbar_review_stars;
    EditText edit_contents;
    Button completeBtn;
    Button addImgBtn;
    ImageView imageViewDate;
    LinearLayout datepicker;
    TextView imgNameTextView;
    int year, month, day ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_spot_review_write);

        ratingbar_review_stars = (RatingBar)findViewById(R.id.ratingbar_review_stars);
        edit_contents = (EditText)findViewById(R.id.edittext_review_content);
        completeBtn = (Button)findViewById(R.id.completeBtn);
        addImgBtn = (Button)findViewById(R.id.button_review_img);
        datepicker = (LinearLayout)findViewById(R.id.datepicker);
        imgNameTextView = (TextView) findViewById(R.id.addImageName);
        imageViewDate= (ImageView)findViewById(R.id.imageview_tsrw_date);
        img = new ImageView[3];
        data = new Uri[3];
        imgUrls = new ArrayList<String>();
        textViewDate = (TextView)findViewById(R.id.textview_date);
        img[0] = (ImageView)findViewById(R.id.imageview_review_1);
        img[1] = (ImageView)findViewById(R.id.imageview_review_2);
        //img[2] = (ImageView)findViewById(R.id.
        edit_userid = "joo";
        edit_contentid = "trips";
        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);


         /*날짜 선택 다이얼로그*/
        imageViewDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(TouristSpotReviewWrite.this, dateSetListener, year, month, day).show();

            }
        });

        /*갤러리 호출*/
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YPhotoPickerIntent intent = new YPhotoPickerIntent(TouristSpotReviewWrite.this);
                intent.setMaxSelectCount(20);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                intent.setSelectCheckBox(false);
                intent.setMaxGrideItemCount(3);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        /*작성완료*/
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_userid.length() == 0 || edit_contents.length() == 0) {
                    Toast.makeText(getApplication(), "아이디 및 내용을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                   /* RequestBody 객체에 edittext값들을 저장.*/
                    final RequestBody userid = RequestBody.create(MediaType.parse("multipart/from-data"), edit_userid);
                    final RequestBody contentid = RequestBody.create(MediaType.parse("multipart/from-data"), edit_contentid);
                    RequestBody stars = RequestBody.create(MediaType.parse("multipart/from-data"), String.valueOf(ratingbar_review_stars.getRating()));
                    final RequestBody content = RequestBody.create(MediaType.parse("multipart/from-data"), edit_contents.getText().toString());
                    final RequestBody date = RequestBody.create(MediaType.parse("multipart/from-data"),msg);

                    MultipartBody.Part[] image = new MultipartBody.Part[3];

                    for(int i=0;i<imgUrls.size();i++)

                    {
                        if (imgUrls.get(i) == "") {
                            image = null;
                        } else {
                            data[i]=Uri.fromFile(new File(imgUrls.get(i)));

                            /**
                             * 비트맵 관련한 자료는 아래의 링크에서 참고
                             * http://mainia.tistory.com/468
                             */

                        /*
                        이미지를 리사이징하는 부분입니다.
                        리사이징하는 이유!! 안드로이드는 메모리에 민감하다고 세미나에서 말씀드렸습니다~
                        구글에서는 최소 16MByte로 정하고 있으나, 제조사 별로 또 디바이스별로 메모리의 크기는 다릅니다.
                        또한, 이미지를 서버에 업로드할 때 이미지의 크기가 크다면 시간이 오래 걸리고 데이터 소모가 큽니다!!
                         */
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            InputStream in = null; // here, you need to get your context.
                            try {
                                in = getContentResolver().openInputStream(data[i]);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            /*inputstream 형태로 받은 이미지로 부터 비트맵을 만들어 바이트 단위로 압축
                            그이우 스트림 배열에 담아서 전송합니다.
                             */

                            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                            // 압축 옵션( JPEG, PNG ) , 품질 설정 ( 0 - 100까지의 int형 ), 압축된 바이트 배열을 담을 스트림
                            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                            File photo = new File(imgUrls.get(i)); // 가져온 파일의 이름을 알아내려고 사용합니다

                            // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!
                            image[i] = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);


                        }

//                    /*
//                    이번에는 post 메소드 입니다. body(이미지),writer,title,content 를 넘깁니다.
//                     파일과 텍스트를 함께 넘길 때는 multipart를 사용합니다.
//                     */

                        NetworkService networkService = ApplicationController.getInstance().getNetworkService();
                        Call<ReviewResult> reviewRegister = networkService.reviewRegister(userid, contentid, stars, content, date,image);
                        reviewRegister.enqueue(new Callback<ReviewResult>() {
                            @Override
                            public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "reponse.body: " + response.body().message);

                                    if (response.body().message.equals("success")) {
                                        Toast.makeText(getApplication(), "후기쓰기가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplication(), "후기쓰기 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ReviewResult> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "에러입니다", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: ");
                            }
                        });
                    }
                }
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            // TODO Auto-generated method stub
            msg = String.format("%d / %d / %d", year,monthOfYear+1, dayOfMonth);
            textViewDate.setText(msg);
            Toast.makeText(TouristSpotReviewWrite.this, msg, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> photos = null;
        if(resultCode==RESULT_OK && requestCode==REQUEST_CODE){
            if(data!=null){
                imgUrls.clear();
                for(int i=0;i<2;i++){
                    img[i].setVisibility(View.GONE);
                }
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                for(int i=photos.size();i<2;i++){
                }
                if(photos.size()!=2) {
                    img[photos.size()].setVisibility(View.VISIBLE);
                    img[photos.size()].setImageResource(R.drawable.camera);
                }
                for(int i=0;i<photos.size();i++){
                    img[i].setVisibility(View.VISIBLE);
                    Uri uri = Uri.fromFile(new File(photos.get(i)));

                    /**
                     * 비트맵 관련한 자료는 아래의 링크에서 참고
                     * http://mainia.tistory.com/468
                     */

                        /*이미지를 리사이징하는 부분*/
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inSampleSize = 2;
                    // options.inSampleSize = 4; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다

                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                            /*inputstream 형태로 받은 이미지로 부터 비트맵을 만들어 바이트 단위로 압축
                            그이우 스트림 배열에 담아서 전송합니다.
                             */

                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    img[i].setImageBitmap(bitmap);
                    this.imgUrls.add(i,photos.get(i));
                }
            }
        }
    }



}
