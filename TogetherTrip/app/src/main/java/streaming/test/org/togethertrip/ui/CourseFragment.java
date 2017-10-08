package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import streaming.test.org.togethertrip.R;

/**
 * Created by taehyung on 2017-09-06.
 */

public class CourseFragment extends Fragment {
    Activity activity;
    Context context;
    FloatingActionButton fab;
    GridView listView;
    CourseListAdapter courseListAdapter;
    ArrayList<CourseListViewitem> courseListViewitemArrayList;

    public CourseFragment(){

    }

    public CourseFragment(Activity activity){
        this.activity = activity;
        context = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_course, container, false);
        listView = (GridView)view.findViewById(R.id.course_gridView);
        courseListViewitemArrayList = new ArrayList<CourseListViewitem>();
        courseListViewitemArrayList.add(new CourseListViewitem(R.drawable.sally,"종로뿌시기","2017.10.07~2017.10.9(2일간)","힐링",3,5));
        courseListViewitemArrayList.add(new CourseListViewitem(R.drawable.sally,"종로뿌시기","2017.10.07~2017.10.9(2일간)","힐링",3,5));
        courseListViewitemArrayList.add(new CourseListViewitem(R.drawable.sally,"종로뿌시기","2017.10.07~2017.10.9(2일간)","힐링",3,5));
        courseListViewitemArrayList.add(new CourseListViewitem(R.drawable.sally,"종로뿌시기","2017.10.07~2017.10.9(2일간)","힐링",3,5));
        courseListViewitemArrayList.add(new CourseListViewitem(R.drawable.sally,"종로뿌시기","2017.10.07~2017.10.9(2일간)","힐링",3,5));
        courseListViewitemArrayList.add(new CourseListViewitem(R.drawable.sally,"종로뿌시기","2017.10.07~2017.10.9(2일간)","힐링",3,5));


        courseListAdapter = new CourseListAdapter(getContext(),courseListViewitemArrayList);
        listView.setAdapter(courseListAdapter);




        //fab 버튼누르면 작성창
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CourseWrite.class));
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
