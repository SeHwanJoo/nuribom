package streaming.test.org.togethertrip.ui;

import android.graphics.drawable.Drawable;

/**
 * Created by taehyung on 2017-09-05.
 */

public class TouristSpotListView {
    Drawable iv_bigImg;
    Drawable iv_profileImg;
    Drawable ib_bigImgHeart;

    String spotName;
    String spotAddr;
    int heartCount;
    int commentCount;
    public TouristSpotListView(String spotName, String spotAddr){
        this.spotName = spotName;
        this.spotAddr = spotAddr;
    }

    public void setIv_bigImg(Drawable iv_bigImg) {
        this.iv_bigImg = iv_bigImg;
    }

    public void setIv_profileImg(Drawable iv_profileImg) {
        this.iv_profileImg = iv_profileImg;
    }

    public void setIb_bigImgHeart(Drawable ib_bigImgHeart) {
        this.ib_bigImgHeart = ib_bigImgHeart;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public void setSpotAddr(String spotAddr) {
        this.spotAddr = spotAddr;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Drawable getIv_bigImg() {

        return iv_bigImg;
    }

    public Drawable getIv_profileImg() {
        return iv_profileImg;
    }

    public Drawable getIb_bigImgHeart() {
        return ib_bigImgHeart;
    }

    public String getSpotName() {
        return spotName;
    }

    public String getSpotAddr() {
        return spotAddr;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public int getCommentCount() {
        return commentCount;
    }



}
