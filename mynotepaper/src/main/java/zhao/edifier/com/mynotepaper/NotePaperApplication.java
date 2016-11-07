package zhao.edifier.com.mynotepaper;

import android.app.Application;

import org.xutils.x;

import cn.bmob.v3.Bmob;

/**
 * Created by tech57 on 2016/9/26.
 */
public class NotePaperApplication extends Application{
    private static final String appId = "a2c96530a81288a53ddccc599346e724";

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        Bmob.initialize(this, appId);
    }


}
