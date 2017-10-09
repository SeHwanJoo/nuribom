package streaming.test.org.togethertrip.ui;

/**
 * Created by minseung on 2017-10-07.
 */

public class CourseListViewitem {
    private int course_image;
    private String title;
    private String course_date;
    private String type;
    private int heartCount;
    private int commentCount;

    public int getCourse_image() {
        return course_image;
    }

    public void setCourse_image(int course_image) {
        this.course_image = course_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourse_date() {
        return course_date;
    }

    public void setCourse_date(String course_date) {
        this.course_date = course_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public CourseListViewitem(int course_image, String title, String course_date, String type, int heartCount, int commentCount) {
        this.course_image = course_image;
        this.title = title;
        this.course_date = course_date;
        this.type = type;
        this.heartCount = heartCount;
        this.commentCount = commentCount;
    }
}
