package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;

/**
 * Created by taehyung on 2017-09-06.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    Activity activity;
    Context context;
    ImageButton filter_all,filter_wheelchairs, filter_bathroom, filter_parkinglot, filter_elevator;
    TextView recommed_spot, recommend_course;
    View select_touristSpot, select_course;
    ImageButton home_searchBtn;

    public HomeFragment(){

    }

    public HomeFragment(Activity activity){
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
        View view = inflater.inflate(R.layout.main_container, container, false);

        filter_all = (ImageButton) view.findViewById(R.id.filter_all);
        filter_wheelchairs = (ImageButton) view.findViewById(R.id.filter_wheelchairs);
        filter_bathroom = (ImageButton) view.findViewById(R.id.filter_bathroom);
        filter_parkinglot = (ImageButton) view.findViewById(R.id.filter_parkinglot);
        filter_elevator = (ImageButton) view.findViewById(R.id.filter_elevator);

        filter_all.setOnClickListener(this);
        filter_wheelchairs.setOnClickListener(this);
        filter_bathroom.setOnClickListener(this);
        filter_parkinglot.setOnClickListener(this);
        filter_elevator.setOnClickListener(this);

        recommed_spot = (TextView) view.findViewById(R.id.recommend_spot);
        recommend_course = (TextView)view.findViewById(R.id.recommend_course);

        select_touristSpot = (View) view.findViewById(R.id.select_touristSpot);
        select_course = (View)view.findViewById(R.id.select_course);

        home_searchBtn = (ImageButton) view.findViewById(R.id.home_searchBtn);

        home_searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Home_SearchActivity.class);
                startActivity(intent);
            }
        });

        recommed_spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_touristSpot.setBackgroundColor(Color.parseColor("#263fe1"));
                select_course.setBackgroundColor(Color.parseColor("#dedede"));

            }
        });

        recommend_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_touristSpot.setBackgroundColor(Color.parseColor("#dedede"));
                select_course.setBackgroundColor(Color.parseColor("#263fe1"));
            }
        });

        return view;
    }

    //시설 필터링 on/off 메소드
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.filter_all:
                if(filter_all.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_all_off).getConstantState())) {
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_on);
                    filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_off);
                    filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_off);
                    filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_off);
                    filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_off);
                }else{
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }
                break;
            case R.id.filter_wheelchairs:
                if(filter_wheelchairs.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_wheelchairs_off).getConstantState())) {
                    filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else{
                    filter_wheelchairs.setBackgroundResource(R.drawable.trips_facilityfilter_wheelchairs_off);
                }
                break;
            case R.id.filter_bathroom:
                if(filter_bathroom.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_bathroom_off).getConstantState())) {
                    filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else{
                    filter_bathroom.setBackgroundResource(R.drawable.trips_facilityfilter_bathroom_off);
                }
                break;
            case R.id.filter_parkinglot:
                if(filter_parkinglot.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_parkinglot_off).getConstantState())) {
                    filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else{
                    filter_parkinglot.setBackgroundResource(R.drawable.trips_facilityfilter_parkinglot_off);
                }
                break;
            case R.id.filter_elevator:
                if(filter_elevator.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.trips_facilityfilter_elevator_off).getConstantState())) {
                    filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_on);
                    filter_all.setBackgroundResource(R.drawable.trips_facilityfilter_all_off);
                }else {
                    filter_elevator.setBackgroundResource(R.drawable.trips_facilityfilter_elevator_off);
                }
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
