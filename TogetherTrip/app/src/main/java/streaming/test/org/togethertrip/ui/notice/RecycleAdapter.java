package streaming.test.org.togethertrip.ui.notice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import streaming.test.org.togethertrip.R;

/**
 * Created by user on 2017-04-15.
 */

public class RecycleAdapter extends RecyclerView.Adapter<myViewHolder>
{       ArrayList<itemData>itemDatas;
        View.OnClickListener clickListener;
    private int openedIndex = -1;

    public RecycleAdapter(ArrayList<itemData> itemDatas, View.OnClickListener clickListener) {
        this.itemDatas = itemDatas;
        this.clickListener = clickListener;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notice_item, parent, false);
        myViewHolder myViewHolder = new myViewHolder(view);
        view.setOnClickListener(clickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {
        holder.recycler_title.setText(itemDatas.get(position).title);
        holder.recycler_content.setText(itemDatas.get(position).content);
        if(position==openedIndex){
            holder.recycler_content.setVisibility(View.VISIBLE);
        }else{
            holder.recycler_content.setVisibility(View.GONE);
        }
        holder.recycler_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==openedIndex){
                    openedIndex=-1;
                }else {
                    openedIndex = position;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {// 몇개의 항목을 뿌려줄 것인지
        return itemDatas != null ? itemDatas.size() :0;
    }
}
