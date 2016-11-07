package zhao.edifier.com.mydiary;

import android.app.Application;

import org.xutils.x;

/**
 * Created by tech57 on 2016/8/26.
 */
public class DiaryApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
