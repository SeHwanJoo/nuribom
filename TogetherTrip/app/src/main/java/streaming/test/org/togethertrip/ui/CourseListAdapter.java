package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import streaming.test.org.togethertrip.datas.like.AddLikeInfo;
import streaming.test.org.togethertrip.datas.like.AddLikeResult;
import streaming.test.org.togethertrip.network.NetworkService;
import streaming.test.org.togethertrip.viewholder.CourseListViewHolder;

/**
 * Created by minseung on 2017-10-07.
 */

public class CourseListAdapter extends BaseAdapter{

    Context context;
    ArrayList<CourseListDatas> CourseListDatas;
    SharedPreferences loginInfo;
    NetworkService service;
    AddLikeInfo addLikeInfo;
    Intent commentIntent;
    private LayoutInflater mInflater;

    public CourseListAdapter(Context context, ArrayList<CourseListDatas> courseListDatas) {
        this.context = context;
        CourseListDatas = courseListDatas;
        this.mInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
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
        final CourseListViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.course_list_view_item, null);
            //convertView = mInflater.inflate(R.layout.course_list_view_item, parent, false);

            viewHolder = new CourseListViewHolder();
            viewHolder.course_image = (ImageView)convertView.findViewById(R.id.iv_course_item_image);
            viewHolder.coureName = (TextView)convertView.findViewById(R.id.tv_course_item_courseName);
            viewHolder.date = (TextView)convertView.findViewById(R.id.tv_course_item_date);
            viewHolder.category = (TextView)convertView.findViewById(R.id.tv_course_item_type);
            viewHolder.heart_count = (TextView)convertView.findViewById(R.id.tv_course_heartCount);
            viewHolder.comment_count = (TextView)convertView.findViewById(R.id.tv_course_comment);
            viewHolder.iv_heart = (ImageView)convertView.findViewById(R.id.iv_heart);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (CourseListViewHolder) convertView.getTag();
        }
        service = ApplicationController.getInstance().getNetworkService();


        //답변에서 하트 눌렀을 때, 하트 색바꾸기
        viewHolder.iv_heart.setOnClickListener(new View.OnClickListener(){
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
                                viewHolder.iv_heart.setBackgroundResource(R.drawable.course_main_heart_color);
                                viewHolder.heart_count.setText(String.valueOf(response.body().result.likecount));
                                Log.i("likecount",String.valueOf(response.body().result.likecount));
                            }
                            else if (response.body().result.message.equals("unlike")) {
                                Toast.makeText(context, "좋아요 취소", Toast.LENGTH_SHORT).show();
                                viewHolder.iv_heart.setBackgroundResource(R.drawable.course_main_heart_line);
//                              iv_heart.setBackgroundResource(R.drawable.course_main_heart_line);
                                viewHolder.heart_count.setText(String.valueOf(response.body().result.likecount));
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
                .into(viewHolder.course_image);
        if(CourseListDatas.get(position).image != null)Log.i("image",CourseListDatas.get(position).image);
        viewHolder.coureName.setText(String.valueOf(CourseListDatas.get(position).title));
        viewHolder.date.setText(CourseListDatas.get(position).date);
        viewHolder.category.setText(CourseListDatas.get(position).category);
        viewHolder.heart_count.setText(String.valueOf(CourseListDatas.get(position).likecount));
        viewHolder.comment_count.setText(String.valueOf(CourseListDatas.get(position).commentcount));


        return convertView;
    }

    // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}

