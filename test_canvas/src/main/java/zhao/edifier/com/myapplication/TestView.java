package zhao.edifier.com.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tech57 on 2016/9/18.
 */
public class TestView extends View{

    private int w,h;
    private int ww,hh;
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(!changed)return;
        w = getWidth();
        h = getHeight();
        ww = w/16;
        hh = h/4;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPss(canvas);

    }


    public void drawPss(Canvas canvas){
        int r=ww/2;//圆角的半径
        int color=Color.parseColor("#eced85");
        int bottom= Color.parseColor("#8dc98c");
        GradientDrawable mDrawable;
        mDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {color,bottom});
        mDrawable.setShape(GradientDrawable.RECTANGLE);//设置形状为矩形
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        setCornerRadii(mDrawable, r, r, 0, 0);//设置4角的圆角半径值
        Rect mRect = new Rect(w/4,w/4,w/4+ww,w/4+hh);

        mDrawable.setBounds(mRect);//设置位置大小
        mDrawable.draw(canvas);//绘制到canvas上

    }

    static void setCornerRadii(GradientDrawable drawable, float r0, float r1,
                               float r2, float r3) {
        drawable.setCornerRadii(new float[]{r0, r0, r1, r1, r2, r2, r3, r3});
    }
}
