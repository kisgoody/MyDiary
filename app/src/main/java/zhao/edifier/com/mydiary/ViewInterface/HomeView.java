package zhao.edifier.com.mydiary.ViewInterface;

import java.util.List;

import zhao.edifier.com.mydiary.mode.DiaryObj;
import zhao.edifier.com.mydiary.mode.Weather.LocationInfo;
import zhao.edifier.com.mydiary.mode.Weather.NowWeatherInfo;

/**
 * Created by tech57 on 2016/8/31.
 */
public interface HomeView {

    void weatherInfo(LocationInfo locationInfo, NowWeatherInfo weatherInfo, String lastUpdate);
    List<Object> searchAllDiary();

}
