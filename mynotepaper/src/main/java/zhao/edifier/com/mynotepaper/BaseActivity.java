package zhao.edifier.com.mynotepaper;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import zhao.edifier.com.mylibrary.ZUtile.StatusBarTextColor;
import zhao.edifier.com.mynotepaper.Utile.MyInflaterFactory;
import zhao.edifier.com.mynotepaper.Utile.SkinInflaterFactory;
import zhao.edifier.com.mynotepaper.Utile.U;

/**
 * Created by tech57 on 2016/10/8.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String APPTHEME = "AppTheme";
    SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSkinInflaterFactory = new SkinInflaterFactory(U.ThemeName);
        LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSkinInflaterFactory.updateStatusBar();
        themeCurrent(mSkinInflaterFactory.getCurrrentTheme());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
//        boolean flag  = StatusBarTextColor.FlymeSetStatusBarLightMode(getWindow(),true);
//        boolean flag2  = StatusBarTextColor.MIUISetStatusBarLightMode(getWindow(),true);
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }

    private void setBaseTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APPTHEME, MODE_PRIVATE);
        int themeType = sharedPreferences.getInt("theme_type", 1);
        int themeId = 1;
        switch (themeType) {
            case 1:
                themeId = R.style.AppTheme_MainActivity_1;
                break;
            case 2:
                themeId = R.style.AppTheme_MainActivity_2;
                break;
            case 3:
                themeId = R.style.AppTheme_MainActivity_3;
                break;
            case 4:
                themeId = R.style.AppTheme_MainActivity_4;
                break;
            case 5:
                themeId = R.style.AppTheme_MainActivity_5;
                break;
        }
        setTheme(themeId);
    }

    PopupWindow pop;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void popLayout(){
        if(pop==null){

            View v = LayoutInflater.from(this).inflate(R.layout.pop_main_layout,null);
            layoutThemesView((LinearLayout) v.findViewById(R.id.ll_theme_view));
            v.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });
            pop = new PopupWindow(v,-1,-2);
            pop.setBackgroundDrawable(new ColorDrawable());
            pop.setFocusable(true);
            pop.setAnimationStyle(R.style.pop_animator_style);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);

                }
            });

        }
        backgroundAlpha(0.6f);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.TOP, 0, 0);

    }
    ImageView iv_selected = null;
    public void layoutThemesView(LinearLayout linearLayout){
        int colors[] = null;
        if(U.ThemeName.equals(U.APPTHEME))
        colors = getResources().getIntArray(R.array.main_colors_theme);

        else if(U.ThemeName.equals(U.PageTheme))
            colors = getResources().getIntArray(R.array.page_colors_theme);
        SharedPreferences sharedPreferences = getSharedPreferences(
                U.ThemeName, MODE_PRIVATE);
        int themeType = sharedPreferences.getInt("theme_type", 1);

        for(int i=0;i<colors.length;i++){
            int color = colors[i];
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setPadding(20,20,20,20);
            iv.setTag(i);
            drawRound(color, iv, 1);
            if(themeType==i+1){
                drawRound(Color.GRAY, iv,2);
                iv_selected = iv;
            }
            linearLayout.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int theme = (int) v.getTag()+1;
                    ImageView iv = (ImageView) v;
                    iv_selected.setBackground(null);
                    iv_selected = iv;
                    drawRound(Color.GRAY, iv_selected, 2);
                    mSkinInflaterFactory.setCurrrentTheme(theme);
                    themeCurrent(theme);
                }
            });
        }

    }

    abstract void themeCurrent(int theme);

    public void drawRound(int color,ImageView iv,int type){
        Bitmap bitmap = null;
        if(type==1)bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        if(type==2)bitmap = Bitmap.createBitmap(90, 90, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        RoundedBitmapDrawable drawable2 = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        drawable2.setCircular(true);
        if(type==1)iv.setImageDrawable(drawable2);
        if(type==2)iv.setBackground(drawable2);
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
