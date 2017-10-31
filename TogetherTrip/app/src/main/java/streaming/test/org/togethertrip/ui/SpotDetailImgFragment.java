package streaming.test.org.togethertrip.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import streaming.test.org.togethertrip.R;

/**
 * Created by taehyung on 2017-10-18.
 */

public class SpotDetailImgFragment extends Fragment{
    Context context;
    Activity activity;

    Intent intent;

    public ImageView iv_detailImg;

    String firstImgUri;

    public SpotDetailImgFragment() {}
    public SpotDetailImgFragment(Activity activity){
        this.activity = activity;
        context = activity;
    }
    public SpotDetailImgFragment(Activity activity, String firstImgUri){
        this.activity = activity;
        context = activity;
        this.firstImgUri = firstImgUri;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tourist_spot_detail_img, container, false);

        iv_detailImg = (ImageView) view.findViewById(R.id.iv_detailImg);

        Glide.with(this)
                .load(firstImgUri)
                .into(iv_detailImg);

        if(firstImgUri == null || firstImgUri.equals("")){
            Glide.with(this)
                    .load(R.drawable.default_image)
                    .into(iv_detailImg);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public ImageView getImageView(){
        return iv_detailImg;
    }
}
