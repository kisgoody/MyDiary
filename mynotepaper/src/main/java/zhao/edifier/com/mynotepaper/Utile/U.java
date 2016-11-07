package zhao.edifier.com.mynotepaper.Utile;

import android.media.Image;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zhao.edifier.com.mynotepaper.Mode.Obj.ImageObj;

/**
 * Created by tech57 on 2016/9/29.
 */
public class U {

    public static String PageTheme = "PageTheme";
    public static String APPTHEME = "APPTHEME";
    public static String ThemeName = APPTHEME;


    public static List<ImageObj> string2List(String imagesStr){
        if(TextUtils.isEmpty(imagesStr))return null;
        List<ImageObj> images = new ArrayList();
        while(imagesStr.length()>0){
            int i1 = imagesStr.indexOf("{");
            int i2 = imagesStr.indexOf("}");
            String image = imagesStr.substring(i1 + 1, i2);
            ImageObj imageObj = new ImageObj();
            if(image.indexOf("http")!=-1){
                imageObj.setIsupload(false);
            }else{
                imageObj.setIsupload(true);
            }
            imageObj.setUrl(image);
            Log.d("U","file:"+image);
            if(new File(image).exists()) {
                images.add(imageObj);
            }
            imagesStr = imagesStr.substring(i2 + 1);
        }
        return images;
    }
    public static String[] string2Array(String str){
        if(TextUtils.isEmpty(str))return null;
        String imagesStr = str;
        List images = new ArrayList();
        while(imagesStr.length()>0){
            int i1 = imagesStr.indexOf("{");
            int i2 = imagesStr.indexOf("}");
            String image = imagesStr.substring(i1+1,i2);
            Log.d("U","file:"+image);
            if(new File(image).exists()) {
                images.add(image);
            }
            imagesStr = imagesStr.substring(i2 + 1);
        }
        String imagesStrs[] = new String[images.size()];
        images.toArray(imagesStrs);
        return imagesStrs;
    }

    public static String array2String(String[] images){
        if(images==null||images.length==0)return "";
        String str = "";
        for(int i=0;i<images.length;i++){

            String itemStr = images[i];
            str = str+"{"+itemStr+"}";
        }

        return str;
    }
}
