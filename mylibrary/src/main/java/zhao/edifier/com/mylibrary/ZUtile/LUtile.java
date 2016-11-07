package zhao.edifier.com.mylibrary.ZUtile;

import android.content.Context;
import android.content.res.Resources;

import java.text.SimpleDateFormat;

/**
 * Created by tech57 on 2016/8/31.
 */
public class LUtile {

    /**
     *
     * @param mills
     * @param format like this"yyyy-MM-dd"
     * @return
     */
    public static String millis2Data(long mills,String format){

        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String dataStr = sdf.format(mills);
        return dataStr;
    }

    public static int getDrawResourceID(String resourceName, Context context) {
        Resources res=context.getResources();
        int picid = res.getIdentifier(resourceName, "mipmap", context.getPackageName());
        return picid;
    }

}
