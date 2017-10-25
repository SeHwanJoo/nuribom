package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import streaming.test.org.togethertrip.R;

/**
 * Created by Dayoung on 2017-09-19.
 */


public class CourseWriteFragment2 extends Fragment {
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    Context context;
    ImageView imageView;
    EditText content;

    String imgUrl;
    Uri uri;

    Activity activity;
    Intent intent;
    int position;

    public CourseWriteFragment2(Context context,int position){
        this.context = context;
        this.position = position;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_course_write2, container, false);

        imageView=(ImageView)view.findViewById(R.id.elbum2);
        content = (EditText)view.findViewById(R.id.page_content);

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((DataSetListner)activity).FragmentContentSet(position, content.getText().toString());
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void pickImage(){
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //getActivity().startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);

    }
    public void getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        this.imgUrl = imgPath;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    getImageNameToUri(data.getData());

                    uri = data.getData();
                    Glide.with(context).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (imgUrl == "") {
                    ((DataSetListner)activity).FragmentImageSet(position, null);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inSampleSize = 2; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다

                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = context.getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                        /*inputstream 형태로 받은 이미지로 부터 비트맵을 만들어 바이트 단위로 압축
                        그이우 스트림 배열에 담아서 전송합니다.
                         */

                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    // 압축 옵션( JPEG, PNG ) , 품질 설정 ( 0 - 100까지의 int형 ), 압축된 바이트 배열을 담을 스트림
                    RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                    File photo = new File(imgUrl); // 가져온 파일의 이름을 알아내려고 사용합니다


                    // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!
                    ((DataSetListner)activity).FragmentImageSet(position,  MultipartBody.Part.createFormData("image", photo.getName(), photoBody));

                }
            }
        }
    }
    public interface DataSetListner{
        void FragmentImageSet(int position, MultipartBody.Part image);
        void FragmentContentSet(int position,String content);
//        void FirstFragmentDataSet(MultipartBody.Part image, String title, String content, String contentid, String contenttypeid);
//        void FirstFragmentDataSet(MultipartBody.Part image, String title, String content, String contentid, String contenttypeid);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            // 사용될 activity 에 context 정보 가져오는 부분
            this.activity = (Activity)context;
        }
    }
}
