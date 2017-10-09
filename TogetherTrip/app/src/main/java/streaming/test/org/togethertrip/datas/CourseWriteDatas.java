package streaming.test.org.togethertrip.datas;

import java.util.ArrayList;

/**
 * Created by Dayoung on 2017-09-29.
 */

public class CourseWriteDatas {
    public Main main;
    public ArrayList<Page> page;

    public class Main{
        public String userid;
        public String title;
        public String overview;
    }
    public class Page{
        public String title;
        public String content;
    }
}
