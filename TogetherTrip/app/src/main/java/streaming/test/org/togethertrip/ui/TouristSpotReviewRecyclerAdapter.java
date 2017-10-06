package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.TouristSpotReviewListData;
import streaming.test.org.togethertrip.viewholder.ReviewViewHolder;

/**
 * Created by taehyung on 2017-10-06.
 */


public class TouristSpotReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private final Context context;
    private ArrayList<TouristSpotReviewListData> TouristSpotReviewListDatas;
    private View.OnClickListener onClickListener;

    public TouristSpotReviewRecyclerAdapter(ArrayList<TouristSpotReviewListData> TouristSpotReviewListDatas, Context context, View.OnClickListener onClickListener) {
        this.TouristSpotReviewListDatas = TouristSpotReviewListDatas;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public void setAdapter(ArrayList<TouristSpotReviewListData> TouristSpotReviewListDatas) {
        this.TouristSpotReviewListDatas = TouristSpotReviewListDatas;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tourist_spot_review_list_view_item, parent, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        view.setOnClickListener(onClickListener);
        return viewHolder;

    }



    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.VH_review_userid.setText(TouristSpotReviewListDatas.get(position).userid);
        holder.VH_review_star.setText(String.valueOf(TouristSpotReviewListDatas.get(position).stars));
        holder.VH_review_content.setText(TouristSpotReviewListDatas.get(position).content);
        holder.VH_review_date.setText(TouristSpotReviewListDatas.get(position).date);
        //holder.VH_review_image1.setImageURI(Uri.parse(TouristSpotReviewListDatas.get(position).image0));//이미지1
        //holder.VH_review_image2.setImageURI(Uri.parse(TouristSpotReviewListDatas.get(position).image1));//이미지2
        if( TouristSpotReviewListDatas.get(position).image0 != null) {
            Glide.with(context).load(TouristSpotReviewListDatas.get(position).image0).into(holder.VH_review_image1);
        }
        if( TouristSpotReviewListDatas.get(position).image1 != null) {
            Glide.with(context).load(TouristSpotReviewListDatas.get(position).image1).into(holder.VH_review_image2);
        }
        if( TouristSpotReviewListDatas.get(position).image2 != null) {
            Glide.with(context).load(TouristSpotReviewListDatas.get(position).image2).into(holder.VH_review_image3);
        }


    }

    @Override
    public int getItemCount() {
        return TouristSpotReviewListDatas != null ? TouristSpotReviewListDatas.size() : 0;
    }
}
