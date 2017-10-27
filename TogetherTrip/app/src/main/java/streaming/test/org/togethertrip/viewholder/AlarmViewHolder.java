package streaming.test.org.togethertrip.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;


/**
 * Created by f on 2017-10-05.
 */

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    TextView date;
    TextView content;
    ImageView image;
    LinearLayout alarm_item;
    Context context;

    public AlarmViewHolder(View itemView, Context context) {
        super(itemView);

        alarm_item = (LinearLayout)itemView.findViewById(R.id.alarm_item);
        date = (TextView)itemView.findViewById(R.id.date);
        content = (TextView)itemView.findViewById(R.id.content);
        image = (ImageView)itemView.findViewById(R.id.image);
        this.context = context;
    }
}
