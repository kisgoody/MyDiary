package zhao.edifier.com.mynotepaper.Utile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
import zhao.edifier.com.mynotepaper.R;

public class SkinInflaterFactory implements LayoutInflaterFactory {

    public static final String NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String ATTR_SKIN_TYPE = "theme_type";
    public static final String BackGround = "BackGround";
    public static final String TextColor = "TextColor";
    public static final String BackGround_Primary = "BackGround|Primary";
    public static final String ColorAccent_Shep = "ColorAccent|Shep";
    public static final String ColorAccent = "ColorAccent";

    private static String TAG = "SkinInflaterFactory";
    private List<View> views = new ArrayList<>();
    private Context context;
    private String APPTHEME = "APPTHEME";
    private int currrentTheme;


        public SkinInflaterFactory(String APPTHEME){
            this.APPTHEME = APPTHEME;
        }

    public int getCurrrentTheme() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                APPTHEME, context.MODE_PRIVATE);
        currrentTheme = sharedPreferences.getInt("theme_type", 3);
        return currrentTheme;
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
        View view = createView(context, name, attrs);
        if (view == null) {//没有找到这个View
            return null;
        }
        view.setTag(changeType);
        views.add(view);
        updateItemTheme(view,context);
        return view;
    }

    /**
     * 通过name去实例化一个View
     *
     * @param context
     * @param name    要被实例化View的全名.
     * @param attrs   View在布局文件中的XML的属性
     * @return View
     */
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

    private void updateItemTheme(View v,Context con){
        int[] colors = getThemeColors();
        updateItemView(v,colors,con);
    }



    private int[] getThemeColors(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                APPTHEME, context.MODE_PRIVATE);
        Log.d("SkinInflaterFactory",APPTHEME);
        currrentTheme = sharedPreferences.getInt("theme_type", 3);
        int[] colors = null;
        if(APPTHEME.equals(U.APPTHEME)) {
            switch (currrentTheme) {
                case 1:
                    colors = context.getResources().getIntArray(R.array.style_1);
                    break;
                case 2:
                    colors = context.getResources().getIntArray(R.array.style_2);
                    break;
                case 3:
                    colors = context.getResources().getIntArray(R.array.style_3);
                    break;
                case 4:
                    colors = context.getResources().getIntArray(R.array.style_4);
                    break;
                case 5:
                    colors = context.getResources().getIntArray(R.array.style_5);
                    break;
                case 6:
                    colors = context.getResources().getIntArray(R.array.style_6);
                    break;
            }
        }else if(APPTHEME.equals(U.PageTheme)) {
            switch (currrentTheme) {
                case 1:
                    colors = context.getResources().getIntArray(R.array.style_11);
                    break;
                case 2:
                    colors = context.getResources().getIntArray(R.array.style_22);
                    break;
                case 3:
                    colors = context.getResources().getIntArray(R.array.style_33);
                    break;
                case 4:
                    colors = context.getResources().getIntArray(R.array.style_44);
                    break;
                case 5:
                    colors = context.getResources().getIntArray(R.array.style_55);
                    break;
            }
        }
        return colors;
    }

    public int[] updateStatusBar(){
        int[] colors = getThemeColors();
        StatusBarCompat.compat((Activity) context, colors[1]);
        ((Activity)context).getWindow().setBackgroundDrawable(new ColorDrawable(colors[3]));
        return colors;
    }


    public void updateTheme(){
        int[] colors = updateStatusBar();

        for(View view:views){
            updateItemView(view,colors,context);
        }

    }

    @SuppressLint("NewApi")
    private void updateItemView(View v,int[]colors,Context con){
        String type_color = (String) v.getTag();
        if(type_color.equals(BackGround)){
            if (v instanceof CardView) {//这里对CardView特殊处理下
                CardView cardView = (CardView) v;
                cardView.setCardBackgroundColor(colors[1]);
            } else {
                v.setBackgroundColor(colors[1]);
            }

        }
        if(type_color.equals(TextColor)){
            if(v instanceof TextView){
               ((TextView) v).setTextColor(colors[2]);
            }else if (v instanceof EditText){
                ((EditText) v).setTextColor(colors[2]);
            }

        }
        if(type_color.equals(BackGround_Primary)){

            if(v instanceof Toolbar){
                Toolbar toolbar = (Toolbar) v;
                toolbar.setBackground(new ColorDrawable(colors[0]));
            }else {
                v.setBackgroundColor(colors[0]);
            }

        }

//        if(type_color.equals(BackGround_WindowBg)){
//            v.setBackgroundColor(colors[3]);
//        }
        if(type_color.equals(ColorAccent)){
            v.setBackgroundColor(colors[2]);
//                drawRound(colors[2],v);
        }
        if(type_color.equals(ColorAccent_Shep)){
            drawRound(colors[2],v);
        }

    }

    private void drawRound(int color,View v){
        Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        RoundedBitmapDrawable drawable2 = RoundedBitmapDrawableFactory.create(context.getResources(),bitmap);
        drawable2.setCircular(true);
        v.setBackground(drawable2);

    }


    public void setCurrrentTheme(int theme_type){
            currrentTheme = theme_type;
            SharedPreferences sharedPreferences = context.getSharedPreferences(APPTHEME, context.MODE_PRIVATE);
            sharedPreferences.edit().putInt("theme_type",theme_type).commit();
            updateTheme();
    }


}
