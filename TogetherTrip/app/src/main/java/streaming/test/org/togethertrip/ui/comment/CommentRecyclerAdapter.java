package streaming.test.org.togethertrip.ui.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.w3c.dom.Comment;

import java.sql.Array;
import java.util.ArrayList;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.comment.CommentDatas;
import streaming.test.org.togethertrip.datas.comment.CommentInfo;
import streaming.test.org.togethertrip.datas.comment.CommentWriteDatas;
import streaming.test.org.togethertrip.network.NetworkService;
import streaming.test.org.togethertrip.ui.TimeUtil;

/**
 * Created by user on 2017-10-29.
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public NetworkService service;
    private ArrayList<CommentDatas> commentListDatas;
    Context context;

    public  CommentRecyclerAdapter(ArrayList<CommentDatas> commentListDatas, Context context){
        this.commentListDatas = commentListDatas;
        this.context = context;
    }

    public  void setAdapter(ArrayList<CommentDatas> commentListDatas){
        this.commentListDatas = commentListDatas;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      CommentViewHolder commentViewHolder = (CommentViewHolder)holder;
      if(commentListDatas.get(position).image!=null && !commentListDatas.get(position).image.equals("")){
          //프로필 사진이 있는 경우
          Glide.with(holder.itemView.getContext())
                  .load(commentListDatas.get(position).image)
                  .into(commentViewHolder.VH_comment_profileInage);
      }
      else {
          commentViewHolder.VH_comment_profileInage.setImageResource(R.drawable.people50);
      }
      commentViewHolder.VH_comment_userid.setText(commentListDatas.get(position).userid);
      commentViewHolder.VH_comment_content.setText(commentListDatas.get(position).content);
      commentViewHolder.VH_review_date.setText(commentListDatas.get(position).date);
    }

    @Override
    public int getItemCount() {
        return commentListDatas != null?commentListDatas.size():0;    }
}
