package streaming.test.org.togethertrip.datas;

import java.io.Serializable;

/**
 * Created by taehyung on 2017-09-04.
 */

//DetailWithTour 정보를 담을 객체
public class DetailWithTour implements Serializable {
    public String parking;
    public String route;
    public String wheelchair;
    public String elevator;
    public String restroom;
    public String handicapetc;
    public String braileblock; // 점자블록 유무
}
