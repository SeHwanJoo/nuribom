package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.CourseListDatas;

/**
 * Created by minseung on 2017-10-07.
 */

public class CourseListAdapter extends BaseAdapter{
    Context context;
    ArrayList<CourseListDatas> CourseListDatas;

    ImageView course_image;
    TextView coureName;
    TextView date;
    TextView category;
    TextView heart_count;
    TextView comment_count;

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
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.course_list_view_item, null);
                course_image = (ImageView)convertView.findViewById(R.id.iv_course_item_image);
                coureName = (TextView)convertView.findViewById(R.id.tv_course_item_courseName);
                date = (TextView)convertView.findViewById(R.id.tv_course_item_date);
                category = (TextView)convertView.findViewById(R.id.tv_course_item_type);
                heart_count = (TextView)convertView.findViewById(R.id.tv_course_heartCount);
                comment_count = (TextView)convertView.findViewById(R.id.tv_course_comment);
            }
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


}

