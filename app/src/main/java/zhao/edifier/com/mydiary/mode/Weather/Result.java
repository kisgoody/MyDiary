package zhao.edifier.com.mydiary.mode.Weather;

/**
 * Created by tech57 on 2016/8/31.
 */
public class Result {

    public LocationInfo location;
    public NowWeatherInfo now;
    public String last_update="2016.8.31";

    public LocationInfo getLocation() {
        return location;
    }

    public void setLocation(LocationInfo location) {
        this.location = location;
    }

    public NowWeatherInfo getNow() {
        return now;
    }

    public void setNow(NowWeatherInfo now) {
        this.now = now;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }
}
