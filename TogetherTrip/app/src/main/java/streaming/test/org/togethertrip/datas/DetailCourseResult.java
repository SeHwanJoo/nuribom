package streaming.test.org.togethertrip.datas;

import java.util.ArrayList;

/**
 * Created by user on 2017-10-25.
 */

public class DetailCourseResult {
    public  Result result;
    public ArrayList<DetailCourseDatas> detailCourseDatas;
    public String message;

    public class Result{
        int coureid;
        String title;
    }
}
