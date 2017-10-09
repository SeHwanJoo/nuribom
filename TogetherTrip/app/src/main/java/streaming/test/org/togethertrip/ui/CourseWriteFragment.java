package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import streaming.test.org.togethertrip.R;


/**
 * Created by taehyung on 2017-09-06.
 */

public class CourseWriteFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    Context context;
    Activity activity;
    ImageView imageView;
    Intent intent;
    EditText courseTitle;

    String choice_sido = "";
    Spinner spinner_category;
    CourseWriteFragment.SpinnerAdapter adspin1;
    final static String[] arrayLocation1 = {"카테고리1", "카테고리2", "카테고리3", "카테고리4"};


    public CourseWriteFragment() {}

    public CourseWriteFragment(Activity activity){
        this.context = activity;
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course_write, container, false);

        imageView =(ImageView)view.findViewById(R.id.elbum);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        courseTitle = (EditText) view.findViewById(R.id.courseTitle);
        String course1 = courseTitle.toString();

        spinner_category = (Spinner) view.findViewById(R.id.categoryspinner);

        adspin1 = new CourseWriteFragment.SpinnerAdapter(activity, arrayLocation1, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adspin1);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adspin1.getItem(position).equals("카테고리1")){
                    choice_sido = "카테고리1";
                }else if(adspin1.getItem(position).equals("카테고리2")){
                    choice_sido = "카테고리2";
                }else if(adspin1.getItem(position).equals("카테고리3")){
                    choice_sido = "카테고리3";
                }else if(adspin1.getItem(position).equals("카테고리4")){
                    choice_sido = "카테고리4";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // 줄때
        Fragment f = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Obj", course1);   // Object 넘기기
        f.setArguments(bundle);
/*
TODO 메롱
 */
        Toast.makeText(context, "박다영은 핵바보다", Toast.LENGTH_SHORT).show();


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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(context).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);

//                try {
                //Uri에서 이미지 이름을 얻어온다.
                //String name_Str = getImageNameToUri(data.getData());
                //이미지 데이터를 비트맵으로 받아온다.
//                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                Uri uri = data.getData();
                Log.d("zzzdf", "onActivityResult: " + uri) ;
                Glide.with(context).load(uri).into(imageView);
                //배치해놓은 ImageView에 set
//                    imageView.setImageBitmap(image_bitmap);

                //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }
    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[] {};

        public SpinnerAdapter(final Context context,
                              final String[] objects, final int textViewResourceId) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        /**
         * 스피너 클릭시 보여지는 View의 정의
         */
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_dropdown_item, parent, false);
            }

//            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
//            tv.setText(items[position]);
//            tv.setTextColor(Color.parseColor("#1E3790"));
//            tv.setTextSize(12);
//            tv.setHeight(50);
            return convertView;
        }

        /**
         * 기본 스피너 View 정의
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.parseColor("#1E3790"));
            tv.setTextSize(12);
            return convertView;
        }
    }

}

