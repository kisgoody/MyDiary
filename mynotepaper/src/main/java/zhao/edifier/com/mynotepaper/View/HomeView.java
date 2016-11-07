package zhao.edifier.com.mynotepaper.View;

import android.os.Bundle;

import java.util.List;

import zhao.edifier.com.mynotepaper.Mode.Obj.ContentObject;
import zhao.edifier.com.mynotepaper.Mode.Obj.NotePaperObj;

/**
 * Created by tech57 on 2016/9/26.
 */
public interface HomeView {

    List<Object> readData();
    void deleteDate(int position);
    void addData();
    void itemClick(Bundle bundle,NotePaperObj contentObject);
}
