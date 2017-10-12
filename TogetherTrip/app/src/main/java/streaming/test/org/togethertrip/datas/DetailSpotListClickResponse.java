package streaming.test.org.togethertrip.datas;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by taehyung on 2017-10-06.
 */

public class DetailSpotListClickResponse {
    public DetailCommon detailCommon;
    public DetailIntro detailIntro;
    public ArrayList<DetailInfo> detailInfo;
    public DetailWithTour detailWithTour;
    public ArrayList<DetailImage> detailImage;

    //객체 통째로 intent에 putExtra 하기 위해 Serializable 인터페이스를 implement.
    public class DetailCommon implements Serializable{
        public String title;
        public String telname;
        public String tel;
        public String overview;
        public String mapx;
        public String mapy;
    }
    public class DetailIntro implements Serializable{
        public String accomcountlodging;
        public String checkintime;
        public String chkcooking;
        public String infocenterlodging;
        public String parkinglodging;
        public String pickup;
        public String roomcount;
        public String reservationlodging;
        public String reservationurl;
        public String roomtype;
        public String scalelodging;
    }
    public class DetailInfo implements Serializable{
        public String infoname;
        public String infotext;
    }
    public class DetailImage{
        public String originimgurl;
    }

}
