package streaming.test.org.togethertrip.ui;

/**
 * Created by minseung on 2017-10-29.
 */


public class TimeUtil {
    static public String getTimeToPastString(long writtenTime) {
        long remainTimeMill = System.currentTimeMillis()-writtenTime;
        int remainTimeSec = (int) (remainTimeMill / 1000);
        String remainTime;
        if (remainTimeSec < 10) {
            remainTime = "방금";
        } else if (remainTimeSec < 60) {
            remainTime = remainTimeSec + "초";
        } else if (remainTimeSec < 60 * 60) {
            remainTime = (remainTimeSec / 60) + "분";
        } else if (remainTimeSec < 60 * 60 * 24) {
            remainTime = (remainTimeSec / (60 * 60)) + "시간";
        } else {
            remainTime = (remainTimeSec / (60 * 60 * 24)) + "일";
        }

        return remainTime;
    }
}
