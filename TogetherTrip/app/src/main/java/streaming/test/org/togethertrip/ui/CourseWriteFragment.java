package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.melnykov.fab.FloatingActionButton;

import streaming.test.org.togethertrip.R;


/**
 * Created by taehyung on 2017-09-06.
 */

public class CourseWriteFragment extends Fragment {
    private static final String TAG = "CourseWriteFragment";
    private static final int PICK_IMAGE_REQUEST_CODE = 100;
    Context context;
    Activity activity;
    ImageView imageView;
    Intent intent;
    EditText courseTitle;

    //CourseWrite 액티비티의 버튼
    Button okBtn;
    FloatingActionButton nextfab;

    String title;
    Uri mUri;
    DataSetListner mListner;


    String choice_category = "";
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

        try{
            mListner.FirstFragmentDataSet(mUri, choice_category, title);
            courseTitle.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //입력하기 전에
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        입력되는 텍스트에 변화가 있을때
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //입력이 끝났을 때
                        title = courseTitle.getText().toString();
                    }
                });
        }catch(Exception e){

        }

//        okBtn = (Button) activity.findViewById(R.id.okbtn);
//        nextfab = (FloatingActionButton) activity.findViewById(R.id.nextfab);
//
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    mListner.FirstFragmentDataSet(mUri, choice_category, title);
//                }catch(Exception e){
//
//                }
//
//            }
//        });
//
//        nextfab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //타이틀 입력 받기
//                courseTitle.addTextChangedListener(new TextWatcher() {
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                        //입력하기 전에
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
////                        입력되는 텍스트에 변화가 있을때
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        //입력이 끝났을 때
//                        title = courseTitle.getText().toString();
//                    }
//                });
//
//            }
//        });


        spinner_category = (Spinner) view.findViewById(R.id.categoryspinner);

        adspin1 = new CourseWriteFragment.SpinnerAdapter(activity, arrayLocation1, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adspin1);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adspin1.getItem(position).equals("카테고리1")){
                    choice_category = "카테고리1";
                }else if(adspin1.getItem(position).equals("카테고리2")){
                    choice_category = "카테고리2";
                }else if(adspin1.getItem(position).equals("카테고리3")){
                    choice_category = "카테고리3";
                }else if(adspin1.getItem(position).equals("카테고리4")){
                    choice_category = "카테고리4";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // 줄때
//        Fragment f = new Fragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("Obj", course1);   // Object 넘기기
//        f.setArguments(bundle);
/*
TODO 메롱
 */
        Toast.makeText(context, "김태형은 핵바보다", Toast.LENGTH_SHORT).show();


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

                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: " + uri) ;
                Glide.with(context).load(uri).into(imageView);
                mUri = uri;
            }else{
                Toast.makeText(context, "이미지를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
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

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.parseColor("#1E3790"));
            tv.setTextSize(12);
            tv.setHeight(50);
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



    /*
    * TODO 프래그먼트 <-> 액티비티간 통신
    * 액티비티 내의 ok버튼을 CourseWriteFragment, CourseWriteFragment2에서 인지하고
    * Fragment들에 있는 데이터들을 합쳐 서버에 통신해야함 어려워 못하겠어 엉엉
    */


    public interface DataSetListner{
        void FirstFragmentDataSet(Uri uri, String category, String title);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DataSetListner){
            mListner = (DataSetListner) context;
        }else{
            throw new RuntimeException(context.toString() + "must be implement DataSetListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListner = null;
    }
}

