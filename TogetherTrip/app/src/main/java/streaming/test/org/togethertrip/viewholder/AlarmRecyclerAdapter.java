package streaming.test.org.togethertrip.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.AlarmDatas;

/**
 * Created by f on 2017-10-05.
 */

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    public AlarmRecyclerAdapter(ArrayList<AlarmDatas.AlarmResult> alarmResults, Context context) {
        this.alarmResults = alarmResults;
        this.context = context;
    }



    ArrayList<AlarmDatas.AlarmResult> alarmResults;
    Context context;

    public void setadapter(ArrayList<AlarmDatas.AlarmResult> alarmResults, Context context){
        this.alarmResults = alarmResults;
        this.context = context;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        AlarmViewHolder myViewHolder = new AlarmViewHolder(view,context);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, final int position) {
        if(alarmResults.get(position).like_comment == 1) holder.content.setText(alarmResults.get(position).writeuser
                + "님이 " + alarmResults.get(position).title + "에 좋아요를 눌렀습니다.");
        else if(alarmResults.get(position).like_comment == 0) holder.content.setText(alarmResults.get(position).writeuser
                + "님이 " + alarmResults.get(position).title + "에 댓글을 달았습니다.");
        holder.date.setText(alarmResults.get(position).date);
        if(alarmResults.get(position).write_profile != null)
            Glide.with(context)
                .load(alarmResults.get(position).write_profile)
                .into(holder.image);
//        holder.alarm_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,DetailActivity);
//                intent.putExtra("courseid",alarmResults.get(position).courseid);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return alarmResults != null ? alarmResults.size() : 0;
    }
}
