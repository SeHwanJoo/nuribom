package streaming.test.org.togethertrip.ui.notice;

import android.view.View;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;

/**
 * Created by user on 2017-04-15.
 */

public class myViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
    TextView recycler_title;
    TextView recycler_content;

    public myViewHolder(View itemView){
        super(itemView);

        recycler_title = (TextView)itemView.findViewById(R.id.textview_notice_title);
        recycler_content = (TextView)itemView.findViewById(R.id.textview_notice_content);
    }
}
