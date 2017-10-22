package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.Network;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.CourseListDatas;
import streaming.test.org.togethertrip.datas.CourseSearchData;
import streaming.test.org.togethertrip.datas.like.AddLikeInfo;
import streaming.test.org.togethertrip.datas.like.AddLikeResult;
import streaming.test.org.togethertrip.network.NetworkService;

/**
 * Created by minseung on 2017-10-07.
 */

public class CourseListAdapter extends BaseAdapter{
    Context context;
    ArrayList<CourseListDatas> CourseListDatas;
    NetworkService service;
    ImageView course_image;
    TextView coureName;
    TextView date;
    TextView category;
    TextView heart_count;
    TextView comment_count;
    TextView courseid;
    AddLikeInfo addLikeInfo;
    ImageView iv_heart;


    public CourseListAdapter(Context context, ArrayList<CourseListDatas> courseListDatas) {
        this.context = context;
        CourseListDatas = courseListDatas;
    }

        //이 리스트뷰가 몇개의 item을 가지고 있는지를 알려주는 함수

    @Override
    public int getCount() {
        if(CourseListDatas == null){
            return 0;
        }else {
            return CourseListDatas.size();
        }
    }


    //현재 어떤 아이템인지
        @Override
        public Object getItem(int position) {
            //저장되어 있는 객체 중 position에 따라서 리턴
            return CourseListDatas.get(position);
        }

        //현재 어떤 포지션인지 알려주는 부분
        @Override
        public long getItemId(int position) {
            return position;
        }

        //순차적으로 한칸씩 화면을 구성해주는 부분
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.course_list_view_item, null);
                course_image = (ImageView)convertView.findViewById(R.id.iv_course_item_image);
                coureName = (TextView)convertView.findViewById(R.id.tv_course_item_courseName);
                date = (TextView)convertView.findViewById(R.id.tv_course_item_date);
                category = (TextView)convertView.findViewById(R.id.tv_course_item_type);
                heart_count = (TextView)convertView.findViewById(R.id.tv_course_heartCount);
                comment_count = (TextView)convertView.findViewById(R.id.tv_course_comment);
                iv_heart = (ImageView)convertView.findViewById(R.id.iv_heart);
            }

            service = ApplicationController.getInstance().getNetworkService();


            //답변에서 하트 눌렀을 때, 하트 색바꾸기
            iv_heart.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                       addLikeInfo = new AddLikeInfo();
                       addLikeInfo.userid = "joo";
                       addLikeInfo.courseid = CourseListDatas.get(position).courseid;

                    Call<AddLikeResult> addLike = service.addLikeResult(addLikeInfo);
                        addLike.enqueue(new Callback<AddLikeResult>() {
                            @Override
                            public void onResponse(Call<AddLikeResult> call, Response<AddLikeResult> response) {

                                if (response.isSuccessful()) {
                                    if (response.body().result.message.equals("like")) {
                                        Toast.makeText(context, "좋아요", Toast.LENGTH_SHORT).show();
                                        iv_heart.setBackgroundResource(R.drawable.course_main_heart_color);
                                        heart_count.setText(String.valueOf(response.body().result.likecount));
                                    }
                                    else if (response.body().result.message.equals("unlike")) {
                                        Toast.makeText(context, "좋아요 취소", Toast.LENGTH_SHORT).show();
                                        iv_heart.setBackgroundResource(R.drawable.course_main_heart_line);
//                                        iv_heart.setBackgroundResource(R.drawable.course_main_heart_line);
                                        heart_count.setText(String.valueOf(response.body().result.likecount));
                                    }
                                    notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onFailure(Call<AddLikeResult> call, Throwable t) {
                                //Toast.makeText(context, "에러가 발생했습니다. 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
            });



            Glide.with(context).load(CourseListDatas.get(position).image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(course_image);
            coureName.setText(String.valueOf(CourseListDatas.get(position).title));
            date.setText(CourseListDatas.get(position).date);
            category.setText(CourseListDatas.get(position).category);
            heart_count.setText(String.valueOf(CourseListDatas.get(position).likecount));
            comment_count.setText(String.valueOf(CourseListDatas.get(position).commentcount));
            return convertView;
        }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}

