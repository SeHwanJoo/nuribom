package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;

import java.util.ArrayList;

/**
 * Created by user on 2017-10-07.
 */

public class CourseListAdapter extends BaseAdapter{
    Context context;
    ArrayList<CourseListViewitem> CourseListViewitemArrayList;

    ImageView course_image;
    TextView coureName;
    TextView date;
    TextView type;
    TextView heart_count;
    TextView comment_count;

    public CourseListAdapter(Context context, ArrayList<CourseListViewitem> courseListViewitemArrayList) {
        this.context = context;
        CourseListViewitemArrayList = courseListViewitemArrayList;
    }

        //이 리스트뷰가 몇개의 item을 가지고 있는지를 알려주는 함수
        @Override
        public int getCount() {
            //리스트뷰의 사이즈만큼 반환
           return this.CourseListViewitemArrayList.size();
        }

        //현재 어떤 아이템인지
        @Override
        public Object getItem(int position) {
            //저장되어 있는 객체 중 position에 따라서 리턴
            return this.CourseListViewitemArrayList.get(position);
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
                type = (TextView)convertView.findViewById(R.id.tv_course_item_type);
                heart_count = (TextView)convertView.findViewById(R.id.tv_course_heartCount);
                comment_count = (TextView)convertView.findViewById(R.id.tv_course_comment);
            }
            course_image.setImageResource(CourseListViewitemArrayList.get(position).getCourse_image());
            coureName.setText(CourseListViewitemArrayList.get(position).getTitle());
            date.setText(CourseListViewitemArrayList.get(position).getCourse_date());
            type.setText(CourseListViewitemArrayList.get(position).getType());
            heart_count.setText(String.valueOf(CourseListViewitemArrayList.get(position).getHeartCount()));
            comment_count.setText(String.valueOf(CourseListViewitemArrayList.get(position).getCommentCount()));
            return convertView;
        }
}

