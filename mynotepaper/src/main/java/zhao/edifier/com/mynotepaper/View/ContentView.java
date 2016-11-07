package zhao.edifier.com.mynotepaper.View;

import zhao.edifier.com.mynotepaper.Mode.Obj.NotePaperObj;

/**
 * Created by tech57 on 2016/9/27.
 */
public interface ContentView {

    void choiceImages(String[] choicedImages);
    void choiceImagesResult(String[] resultImages);
    void readTodayData();
    void saveData(NotePaperObj contentObject);

}
