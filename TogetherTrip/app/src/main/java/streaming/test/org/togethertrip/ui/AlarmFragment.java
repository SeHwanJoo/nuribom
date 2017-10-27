package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.AlarmDatas;
import streaming.test.org.togethertrip.network.NetworkService;
import streaming.test.org.togethertrip.viewholder.AlarmRecyclerAdapter;

/**
 * Created by taehyung on 2017-09-06.
 */

public class AlarmFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    Context context;
    Activity activity;

    ArrayList<AlarmDatas.AlarmResult> itemDatas;
    ArrayList<AlarmDatas.AlarmResult> itemDatas1;
    String userid;

    AlarmRecyclerAdapter recyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    AlarmRecyclerAdapter recyclerAdapter1;
    LinearLayoutManager linearLayoutManager1;
    RecyclerView recyclerView1;
    SwipeRefreshLayout mSwipeRefreshLayout;

    NetworkService service;

    public AlarmFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alarm, container, false);

        itemDatas = new ArrayList<AlarmDatas.AlarmResult>();
        itemDatas1 = new ArrayList<AlarmDatas.AlarmResult>();

        userid = "joo";

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        recyclerView = (RecyclerView)view.findViewById(R.id.alarm_rcv);
        recyclerView1 = (RecyclerView)view.findViewById(R.id.alarm_rcv1);

        service = ApplicationController.getInstance().getNetworkService();

        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new AlarmRecyclerAdapter(itemDatas, context);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView1.setHasFixedSize(true);

        recyclerView1.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        recyclerAdapter1 = new AlarmRecyclerAdapter(itemDatas1, context);
        recyclerView1.setAdapter(recyclerAdapter1);
        recyclerView1.setNestedScrollingEnabled(false);

        network();

        return view;
    }

    private void network(){
        Call<AlarmDatas> getAlarm = service.getAlarm(userid);
        getAlarm.enqueue(new Callback<AlarmDatas>() {

            @Override
            public void onResponse(Call<AlarmDatas> call, Response<AlarmDatas> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("yes")) {
                        itemDatas.clear();
                        itemDatas1.clear();
                        for(AlarmDatas.AlarmResult data : response.body().result){
                            if(data.wheatherread == 0) itemDatas.add(data);
                            else if(data.wheatherread == 1) itemDatas1.add(data);
                        }

                        recyclerAdapter.setadapter(itemDatas,context);
                        recyclerView.setAdapter(recyclerAdapter);

                        recyclerAdapter1.setadapter(itemDatas1,context);
                        recyclerView1.setAdapter(recyclerAdapter1);

                    } else if(response.body().message.equals("no alarm")){
                        Toast.makeText(getContext(),"더 알람이 없습니다.",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AlarmDatas> call, Throwable t) {
                Toast.makeText(getContext(),"더 알람이 없습니다.",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        network();
    }
}
