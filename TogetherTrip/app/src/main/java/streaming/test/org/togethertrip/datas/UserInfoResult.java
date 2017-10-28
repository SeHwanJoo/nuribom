package streaming.test.org.togethertrip.datas;

/**
 * Created by taehyung on 2017-10-19.
 */

public class UserInfoResult {
    public String message;
    public Info result;
    public UserData userdata;

    public class Info {
        public int triplike;
        public int courselike;
        public int coursecomment;
        public int tripreviews;
        public int course;
    }
    public class UserData{
        public String img;
        public String email;
        public String password;
        public String userid;
    }
}
