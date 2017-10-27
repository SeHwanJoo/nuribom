package streaming.test.org.togethertrip.datas;

import java.util.ArrayList;

/**
 * Created by user on 2017-10-25.
 */

public class DetailCourseDatas {
    public Result result;
    public ArrayList<Page> page;
    public String message;

    public static class Page{
        public String courseid;
        public int pageid;
        public String image;
        public String title;
        public String content;
        public String contentid;
        public String contenttypeid;
    }

    public static class Result{
        public int courseid;
        public int likecount;
        public int viewcount;
        public String commentcount;
        public String userid;
        public String image;
        public String overview;
        public String title;
        public String date;
        public String category;

    }
}
