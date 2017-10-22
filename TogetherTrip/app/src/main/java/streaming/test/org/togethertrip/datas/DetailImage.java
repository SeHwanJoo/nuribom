package streaming.test.org.togethertrip.datas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taehyung on 2017-10-18.
 */

public class DetailImage implements Parcelable {
    public String originimgurl;

    public DetailImage(){}
    public DetailImage(String originImgUrl){
        this.originimgurl = originImgUrl;
    }

    public DetailImage(Parcel in) {
        originimgurl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originimgurl);
    }

    @SuppressWarnings("rawtypes")
    public static final Creator<DetailImage> CREATOR = new Creator<DetailImage>() {
        @Override
        public DetailImage createFromParcel(Parcel in) {
            return new DetailImage(in);
        }

        @Override
        public DetailImage[] newArray(int size) {
            return new DetailImage[size];
        }
    };
}
