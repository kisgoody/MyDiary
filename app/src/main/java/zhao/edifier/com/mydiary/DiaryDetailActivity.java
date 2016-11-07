package zhao.edifier.com.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import zhao.edifier.com.mydiary.mode.DiaryObj;
import zhao.edifier.com.mydiary.mode.music.Audio;
import zhao.edifier.com.mydiary.mode.music.MediaUtils;

public class DiaryDetailActivity extends ModeActivity implements View.OnClickListener{


    private TextInputEditText tiet_content;
    private TextInputLayout til_hint;
    private TextView tv_count,tv_music;
    private DiaryObj diaryObj;
    private int weatherImageId;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbar_layout;
    private boolean isChange;
    private ImageView iv_music;
    private Animation operatingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        tiet_content = (TextInputEditText) findViewById(R.id.tiet_content);
        til_hint = (TextInputLayout) findViewById(R.id.til_hint);
        tv_count = (TextView) findViewById(R.id.tv_count);
        iv_music = (ImageView) findViewById(R.id.iv_music);
        tv_music = (TextView) findViewById(R.id.tv_music);
        iv_music.setOnClickListener(this);
        MediaUtils.getAudioList(this);
        animation();
//        iv_music.setAnimation(operatingAnim);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar_layout.setTitle(getString(R.string.drawer_diary));

        Intent intent = getIntent();
        diaryObj = (DiaryObj) intent.getSerializableExtra("diaryObject");
        if(diaryObj == null)diaryObj = new DiaryObj();
        else{
            tiet_content.setText(diaryObj.getContent());
            tv_count.setText(diaryObj.getContent().length()+"");
        }
        diaryObj.setLocation(intent.getStringExtra("city"));
        diaryObj.setTemp(intent.getStringExtra("temp"));
        diaryObj.setWeatherInfo(intent.getStringExtra("weather"));
        weatherImageId = intent.getIntExtra("weatherImageId",0);

        tiet_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int countN = s.toString().length();
                tv_count.setText( countN+"" );
                isChange = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.exit_dialog_21blow_layout,null);
//                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diary_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                break;
            case R.id.music:

                Intent intent = new Intent(this,MusicSelectActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.pic:


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==101){
            Audio song = (Audio) data.getSerializableExtra("song");
            diaryObj.setMusicURL(song.getTitle());
            dbManager.saveObj(diaryObj);
            tv_music.setText(song.getComposer());
        }

    }

    @Override
    public void onBackPressed() {
        if(tiet_content.getText().toString().trim().length()>0){
            diaryObj.setContent(tiet_content.getText().toString().trim());
            if(isChange)diaryObj.setCreateTime(System.currentTimeMillis());
            dbManager.saveObj(diaryObj);
        }

        super.onBackPressed();
    }



    public void animation(){
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.music_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

    }
    boolean isStartAnimation;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_music:
                if(isStartAnimation)
                    iv_music.startAnimation(operatingAnim);
                else iv_music.clearAnimation();
                isStartAnimation = !isStartAnimation;
                break;

        }
    }
}
