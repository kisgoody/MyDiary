package zhao.edifier.com.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zhao.edifier.com.mydiary.mode.DiaryObj;
import zhao.edifier.com.mydiary.mode.Weather.LocationInfo;
import zhao.edifier.com.mydiary.mode.Weather.NowWeatherInfo;
import zhao.edifier.com.mydiary.ViewInterface.HomeView;
import zhao.edifier.com.mydiary.fragment.DiaryFragment;
import zhao.edifier.com.mydiary.utile.MyLocation;
import zhao.edifier.com.mylibrary.ZUtile.LUtile;


public class MainActivity extends ModeActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyLocation.WeatherListener,HomeView {

    private Toolbar toolbar;
    private DiaryFragment diaryFragment;

    private TextView tv_city,tv_temp,tv_weather;
    private ImageView iv_weather;
    private static final String TAG = "MainActivity";
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private int weatherImageId;
    private String city,temp,weather;
    private static final int REQUESTDIARYDETAIL = 10;
    private CoordinatorLayout coordinatorLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }
    public void init() {
        layout.setForbidSilding();
        MyLocation location = new MyLocation(getApplicationContext());
        location.setOnWeatherListener(this);

        diaryFragment = new DiaryFragment();
        replaceFragment(R.id.frameLayout,diaryFragment);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        changeTitle(toolbar, getString(R.string.drawer_diary));
        setSupportActionBar(toolbar);

        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        iv_weather = (ImageView) findViewById(R.id.iv_weather);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent2DiaryDetail(null);
            }
        });

    }

    public void intent2DiaryDetail(DiaryObj obj){
        Intent intent = new Intent(getApplicationContext(),DiaryDetailActivity.class);
        intent.putExtra("city",city);
        intent.putExtra("temp",temp);
        intent.putExtra("weather", weather);
        intent.putExtra("weatherImageId", weatherImageId);
        if(obj!=null)intent.putExtra("diaryObject",obj);
        startActivityForResult(intent,REQUESTDIARYDETAIL);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUESTDIARYDETAIL){
            diaryFragment.flashList();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitApp(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            mCollapsingToolbarLayout.setTitle(getString(R.string.drawer_diary));
        } else if (id == R.id.nav_pic) {
            mCollapsingToolbarLayout.setTitle(getString(R.string.drawer_pic));
        } else if (id == R.id.nav_weather) {
            mCollapsingToolbarLayout.setTitle(getString(R.string.drawer_weather));
        } else if (id == R.id.nav_calender) {
            mCollapsingToolbarLayout.setTitle(getString(R.string.drawer_calender));
        } else if (id == R.id.nav_setting) {
            mCollapsingToolbarLayout.setTitle(getString(R.string.drawer_setting));
        } else if (id == R.id.nav_exit) {
            exitApp(toolbar);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onWeatherListener(LocationInfo locationInfo, NowWeatherInfo weatherInfo, String lastUpdate) {
        weatherInfo(locationInfo, weatherInfo, lastUpdate);

    }

    @Override
    public void weatherInfo(LocationInfo locationInfo, NowWeatherInfo weatherInfo, String lastUpdate) {
        city = locationInfo.getName();
        temp = weatherInfo.getTemperature()+"℃";
        weather = weatherInfo.getText();
        tv_city.setText(city);
        tv_temp.setText(temp);
        tv_weather.setText(weather);

        weatherImageId = LUtile.getDrawResourceID("w" + weatherInfo.getCode(), getApplicationContext());
        iv_weather.setImageResource(weatherImageId);

    }

    @Override
    public List<Object> searchAllDiary() {
        return dbManager.findAllESC(DiaryObj.class,"createTime");
    }



}
