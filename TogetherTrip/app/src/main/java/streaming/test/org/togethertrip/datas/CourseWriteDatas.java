package streaming.test.org.togethertrip.datas;

import java.util.ArrayList;

/**
 * Created by Dayoung on 2017-09-29.
 */

public class CourseWriteDatas {
    public Main main;
    public ArrayList<Page> page;

    public static class Main{
        public String userid;
        public String title;
        public String date;
        public String category;
    }
    public static class Page{
        public String content;
        public String contentid;
        public String contenttypeid;
        public Page(){};
        public Page(String content){
            this.content = content;
        }
//        public static class Page(String content){
//            this.content = content;
//        }
    }
}
