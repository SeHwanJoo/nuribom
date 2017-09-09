package streaming.test.org.togethertrip.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.datas.TripInfo;

/**
 * Created by taehyung on 2017-09-05.
 */

//ListView사용을 위한 어댑터
public class ListViewAdapter extends BaseAdapter {
    List<TripInfo> searchList;
    Context context;

    public ListViewAdapter(Context context, List<TripInfo> searchList){
        this.context = context;
        this.searchList = searchList;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public TripInfo getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tourist_spot_list_view_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iv_bigImg = (ImageView) convertView.findViewById(R.id.iv_bigImg);
        ImageButton ib_bigImgHeart = (ImageButton) convertView.findViewById(R.id.ib_bigImgHeart);
        ImageView iv_profileImg = (ImageView) convertView.findViewById(R.id.iv_profileImg);
        TextView tv_spotName = (TextView) convertView.findViewById(R.id.tv_spotName);
        TextView tv_spotAddr = (TextView) convertView.findViewById(R.id.tv_spotAddr);
        TextView tv_heartCount = (TextView) convertView.findViewById(R.id.tv_heartCount);
        TextView tv_commentCount = (TextView)convertView.findViewById(R.id.tv_commentCount);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        TripInfo touristSpotListView = getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        /*
        TODO 이미지 처리!!
             iv_bigImg = 큰 이미지 비트맵으로 그려야함
             ib_bigImgHeart = 하트버튼 클릭 시 색깔 채우고 1값 올리기
             iv_profileImg = 프로필 사진 받아와 비트맵으로 그려야함
             glide 라이브러리 참고해볼것
         */
//        iv_bigImg.setImageDrawable(touristSpotListView.tripInfo.firstimage);
//        ib_bigImgHeart.setImageDrawable(touristSpotListView.tripInfo.); // 하트버튼 스와이프 구현해야함
//        iv_profileImg.setImageDrawable(touristSpotListView.getIv_profileImg()); // 해당 글의 프로필 이미지 안가져왐
        tv_spotAddr.setText(touristSpotListView.addr1);
        tv_spotName.setText(touristSpotListView.title);
        tv_heartCount.setText(touristSpotListView.likecount);
        tv_commentCount.setText(touristSpotListView.commentcount);

        return convertView;
    }

    /**
     * Count 데이터 타입 확인
     * @param heartCount
     * @param commentCount
     */

}
