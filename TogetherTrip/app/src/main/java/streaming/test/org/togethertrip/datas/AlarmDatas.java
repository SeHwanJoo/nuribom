package streaming.test.org.togethertrip.datas;

import java.util.ArrayList;

/**
 * Created by f on 2017-10-26.
 */

public class AlarmDatas {
    public String message;
    public ArrayList<AlarmResult> result;
    public class AlarmResult{
        public int courseid;
        public int wheatherread;
        public String userid;
        public String writeuser;
        public int like_comment;
        public String title;
        public String write_profile;
        public String date;
    }
}
