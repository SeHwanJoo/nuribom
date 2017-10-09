package streaming.test.org.togethertrip.datas;

/**
 * Created by taehyung on 2017-10-08.
 */

public class MapPoint {
    private String name;
    private double latitude;
    private double longitude;

    public MapPoint(){
        super();
    }

    public MapPoint(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
