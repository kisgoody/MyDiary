package zhao.edifier.com.mynotepaper;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhao.edifier.com.mynotepaper.Adapter.ImagesViewPagerAdapter;
import zhao.edifier.com.mynotepaper.Utile.PhotoViewPager;

public class ImagePageActivity extends AppCompatActivity implements View.OnClickListener{


    private PhotoViewPager vp_viewpager;
    private ImagesViewPagerAdapter adapter;
    private ImageView ib_back;
    private TextView tv_title,tv_ok,tv_choice;
    private List<String> list,list_check;
    private List<Integer> list_position;
    private String Tag;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);
        ib_back = (ImageView) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_choice = (TextView) findViewById(R.id.tv_choice);
        tv_ok.setOnClickListener(this);
        tv_choice.setOnClickListener(this);


        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("allImagesPath");
        list_check = intent.getStringArrayListExtra("checkedImagesPath");
        list_position = intent.getIntegerArrayListExtra("positions");
        final int  currentPosition  = intent.getIntExtra("currentPosition", 0);
        Tag = intent.getStringExtra("tag");

        tv_title.setText((currentPosition+1)+"/"+list.size());
        adapter = new ImagesViewPagerAdapter(list,list_check,list_position);
        vp_viewpager = (PhotoViewPager) findViewById(R.id.vp_viewpager);
        vp_viewpager.setOnClickListener(this);
        vp_viewpager.setAdapter(adapter);
        vp_viewpager.setCurrentItem(currentPosition);

        if(Tag!=null){
            findViewById(R.id.rl_top).setVisibility(View.GONE);
            findViewById(R.id.rl_bottom).setVisibility(View.GONE);
        }

        vp_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(Tag==null)return;
                tv_title.setText((position + 1) + "/" + list.size());
                selectMap(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vp_viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void selectMap(int position){
        if(list_position==null)return;
        if(list_position.contains(position)){
            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_box,null);
            tv_choice.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }else{
            Drawable drawable = getResources().getDrawable(R.drawable.ic_check_box_outline,null);
            tv_choice.setCompoundDrawablesWithIntrinsicBounds(drawable,null, null, null);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.vp_viewpager:

                break;
            case R.id.tv_choice:
               int position = vp_viewpager.getCurrentItem();

                if(!list_position.contains(position)){
                    list_check.add(list.get(position));
                    list_position.add(position);
                }else{
                    list_check.remove(list.get(position));
                    list_position.remove((Object)position);
                }
                selectMap(position);
                break;
            case R.id.tv_ok:
                Intent intent_over = getIntent();
                intent_over.putIntegerArrayListExtra("positions", (ArrayList) list_position);
                intent_over.putStringArrayListExtra("allImagesPath", (ArrayList) list);
                intent_over.putStringArrayListExtra("checkedImagesPath", (ArrayList) list_check);
                setResult(32, intent_over);
                finish();
                break;

            case R.id.ib_back:
                onBackPressed();
                break;
        }
    }



    @Override
    public void onBackPressed() {
        Intent intent_back = getIntent();
        intent_back.putIntegerArrayListExtra("positions", (ArrayList) list_position);
        intent_back.putStringArrayListExtra("allImagesPath", (ArrayList) list);
        intent_back.putStringArrayListExtra("checkedImagesPath", (ArrayList) list_check);
        setResult(31, intent_back);
        super.onBackPressed();
    }
}
