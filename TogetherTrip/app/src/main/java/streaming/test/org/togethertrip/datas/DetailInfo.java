package streaming.test.org.togethertrip.datas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taehyung on 2017-10-18.
 */

public class DetailInfo implements Parcelable {
    public String infoname;
    public String infotext;

    public DetailInfo(){}
    public DetailInfo(String infoName, String infoText){
        this.infoname = infoName;
        this.infotext = infoText;
    }

    public DetailInfo(Parcel in) {
        infoname = in.readString();
        infotext = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(infoname);
        dest.writeString(infotext);
    }

    @SuppressWarnings("rawtypes")
    public static final Creator<DetailInfo> CREATOR = new Creator<DetailInfo>() {
        @Override
        public DetailInfo createFromParcel(Parcel in) {
            return new DetailInfo(in);
        }

        @Override
        public DetailInfo[] newArray(int size) {
            return new DetailInfo[size];
        }
    };
}
