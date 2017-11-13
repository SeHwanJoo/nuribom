package streaming.test.org.togethertrip.ui.comment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;

/**
 * Created by minseung on 2017-09-23.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public TextView VH_comment_userid; //작성자
    public ImageView VH_comment_profileInage;// 프로필 이미지
    public TextView VH_comment_content; // 댓글 내용
    public TextView VH_review_date; //작성시간



    public CommentViewHolder(View itemView) {
        super(itemView);
        VH_comment_profileInage = (ImageView)itemView.findViewById(R.id.image_home_item);
        VH_comment_userid = (TextView) itemView.findViewById(R.id.textview_worry_comment_title);
        VH_comment_content = (TextView) itemView.findViewById(R.id.textview_worry_comment_content);
        VH_review_date = (TextView) itemView.findViewById(R.id.textview_worry_comment_clock);
      }


}

