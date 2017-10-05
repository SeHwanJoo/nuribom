package streaming.test.org.togethertrip.datas;

import java.util.ArrayList;

/**
 * Created by taehyung on 2017-10-06.
 */

public class DetailSpotListClickResult {
    public DetailSpotListClickResponse result;

    class DetailSpotListClickResponse{
        public DetailCommon detailCommon;
        public DetailIntro detailIntro;
        public ArrayList<DetailInfo> detailInfo;
        public DetailWithTour detailWithTour;
        public ArrayList<DetailImage> detailImage;
        public OtherInfo otherInfo;

        class DetailCommon{
            String title;
            String telname;
            String tel;
            String overview;
        }
        class DetailIntro{
            String accomcountlodging;
            String checkintime;
            String chkcooking;
            String infocenterlodging;
            String parkinglodging;
            String pickup;
            String roomcount;
            String reservationlodging;
            String reservationurl;
            String roomtype;
            String scalelodging;
        }
        class DetailInfo{
            String infoname;
            String infotext;
        }
        class DetailImage{
            String originimgurl;
        }
        class OtherInfo{
            int likecount;
            int commentcount;
            int message;
        }

    }
}
