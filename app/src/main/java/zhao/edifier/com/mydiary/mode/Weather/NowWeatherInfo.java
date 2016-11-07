package zhao.edifier.com.mydiary.mode.Weather;

/**
 * Created by tech57 on 2016/8/31.
 */
public class NowWeatherInfo {

    public String text;
    public String code;
    public String temperature;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
