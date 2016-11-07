package zhao.edifier.com.mynotepaper.Utile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhao.edifier.com.mylibrary.ZUtile.StatusBarCompat;


/**
 * Created by tech57 on 2016/10/13.
 */
public class MyInflaterFactory implements LayoutInflaterFactory {

    public static final String TAG = "MyInflaterFactory";
    public static final String NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String ATTR_SKIN_TYPE = "theme_type";
    public static final String BackGround = "BackGround";
    public static final String TextColor = "TextColor";
    public static final String BackGround_Primary = "BackGround|Primary";
    public static final String ColorAccent = "ColorAccent";
    private Context context;
    private List<View> views = new ArrayList<>();
    int[] colors = {0x03a9f4,0x038dcc,0xffffff,0x84ffff};//colorPrimary,colorPrimaryDark,TextColor,windowBg

    public MyInflaterFactory(){
        updateStatusBar();
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        this.context = context;
        // 检测当前View是否有更换皮肤的需求
//        boolean isSkinEnable = attrs.getAttributeBooleanValue(NAMESPACE, ATTR_SKIN_ENABLE, false);
        String changeType = attrs.getAttributeValue(NAMESPACE, ATTR_SKIN_TYPE);
        if (TextUtils.isEmpty(changeType)) {
            return null;//返回空就使用默认的InflaterFactory
        }
        View view = createView(context,name, attrs);
        if (view == null) {//没有找到这个View
            return null;
        }
        view.setTag(changeType);
        views.add(view);//将有标记的控件存入数组，以便换肤
        updateItemView(view,colors);//更新默认皮肤
        return view;
    }

    public void setNewTheme(int[] colors){//外部换肤调用
        this.colors = colors;
        for(View view:views){
            updateItemView(view,this.colors);
        }

    }

    public int[] getThemeColors() {
        return colors;
    }

    public int[] updateStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ((Activity)context).getWindow().setStatusBarColor(colors[1]);//android版本号>=21改变状态栏

        }
        ((Activity)context).getWindow().setBackgroundDrawable(new ColorDrawable(colors[3]));//改变Activity背景颜色
        return colors;
    }

    private View createView(Context context, String name, AttributeSet attrs) {
        Log.i(TAG, "createView:" + name);
        View view = null;
        try {
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }

        } catch (Exception e) {
            Log.e(TAG, "error while create 【" + name + "】 : " + e.getMessage());
            view = null;
        }

        return view;
    }

    @SuppressLint("NewApi")
    private void updateItemView(View v,int[]colors){
        String type_color = (String) v.getTag();
        if(type_color.equals(BackGround)){
            if (v instanceof CardView) {//这里对CardView特殊处理下
                CardView cardView = (CardView) v;
                cardView.setCardBackgroundColor(colors[1]);
            } else {
                v.setBackgroundColor(colors[1]);
            }

        }
        else if(type_color.equals(TextColor)){
            if(v instanceof TextView){
                ((TextView) v).setTextColor(colors[2]);
            }else if (v instanceof EditText){
                ((EditText) v).setTextColor(colors[2]);
            }

        }
        else if(type_color.equals(BackGround_Primary)){

                v.setBackgroundColor(colors[0]);

        }

        if(type_color.equals(ColorAccent)){
            v.setBackgroundColor(colors[2]);
        }

    }
}
