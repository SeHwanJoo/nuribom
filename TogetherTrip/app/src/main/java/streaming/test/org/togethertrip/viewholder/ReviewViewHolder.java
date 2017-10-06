package streaming.test.org.togethertrip.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;

/**
 * Created by minseung on 2017-09-23.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    public TextView VH_review_userid;
    public TextView VH_review_star;
    public ImageView VH_review_image1;
    public ImageView VH_review_image2;
    public ImageView VH_review_image3;
    public TextView VH_review_heart;
    public TextView VH_review_picture;
    public TextView VH_review_date; //작성시간
    public TextView VH_review_content;



    public ReviewViewHolder(View itemView) {
        super(itemView);
        VH_review_userid = (TextView)itemView.findViewById(R.id.textview_trvi_userid); //작성자
        VH_review_date =(TextView)itemView.findViewById(R.id.textview_trvi_date); //작성시간
        VH_review_content = (TextView)itemView.findViewById(R.id.textview_trvi_content); //내용
        VH_review_image1 = (ImageView)itemView.findViewById(R.id.imageView1); //이미지1
        VH_review_image2 = (ImageView)itemView.findViewById(R.id.imageView2); //이미지2
        VH_review_image3 = (ImageView)itemView.findViewById(R.id.imageView3); //이미지3
        VH_review_star = (TextView)itemView.findViewById(R.id.textview_trvi_star); //별점
        VH_review_heart = (TextView)itemView.findViewById(R.id.textview_trvi_heart); //좋아요
        VH_review_picture = (TextView)itemView.findViewById(R.id.textview_trvi_picture); //사진개수
        ////////////////메인 리스트////////////////
    }


}

