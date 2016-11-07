package zhao.edifier.com.mydiary.utile;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import zhao.edifier.com.mydiary.mode.Weather.LocationInfo;
import zhao.edifier.com.mydiary.mode.Weather.NowWeatherInfo;
import zhao.edifier.com.mydiary.mode.Weather.WeatherObj;

/**
 * Created by tech57 on 2016/8/30.
 */
public class MyLocation {
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private Context context;
    private static final String TAG = "MyLocation";
    private WeatherListener weatherListener;

    public interface WeatherListener{

        void onWeatherListener(LocationInfo locationInfo,NowWeatherInfo weatherInfo,String lastUpdate);

    }

    public void setOnWeatherListener(WeatherListener weatherListener){
        this.weatherListener = weatherListener;
    }

    public MyLocation(Context context) {
        this.context = context;
        mLocationClient = new LocationClient(context);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mLocationClient.start();//注册监听函数
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }



    private void getWeatherInfo(String url){

        RequestParams params = new RequestParams(url);
        Log.d(TAG, "params.getCharset()="+params.getCharset());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("MyLaction",result.toString());
                WeatherObj mWeatherObj =new Gson().fromJson(result, WeatherObj.class);

                LocationInfo info = mWeatherObj.getResults()[0].getLocation();
                NowWeatherInfo weatherInfo = mWeatherObj.getResults()[0].getNow();
                String last_update = mWeatherObj.getResults()[0].getLast_update();
                weatherListener.onWeatherListener(info,weatherInfo,last_update);
                Log.d(TAG, mWeatherObj.getResults()[0].getLocation().getName());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("MyLaction","onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("MyLaction","onCancelled");
            }

            @Override
            public void onFinished() {
                Log.d("MyLaction","onFinished");
            }
        });

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Double Longitude = location.getLongitude();//经
            Double Latitude = location.getLatitude();//纬
            String url = "https://api.thinkpage.cn/v3/weather/now.json?key=K0FSFH0ZF7&location="+Latitude+":"+Longitude+"&language=zh-Hans&unit=c";
            Log.d(TAG,url);
            getWeatherInfo(url);
        }
}



}
