package com.yuwanqing.mysunshine;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuwanqing.mysunshine.gson.Forecast;
import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.HttpUtil;
import com.yuwanqing.mysunshine.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private FrameLayout mBackground;
    public SwipeRefreshLayout swipeRefresh;//下拉刷新
    private String mWeatherId;//
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView weatherInfoText;
    private TextView dirText;
    private TextView scText;
    private TextView humText;
    private TextView flText;
    private LinearLayout forecastLayout;
    private TextView qltyText;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView fluText;
    private TextView drsgText;
    private TextView travText;
    private TextView uvText;
    private String data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = new String[3];//
        //初始化各控件
        mBackground = (FrameLayout) findViewById(R.id.main_bg);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.city_text);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        dirText = (TextView) findViewById(R.id.dir_text);
        scText = (TextView) findViewById(R.id.sc_text);
        humText = (TextView) findViewById(R.id.hum_text);
        flText = (TextView) findViewById(R.id.fl_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        qltyText = (TextView) findViewById(R.id.qlty_text);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        fluText = (TextView) findViewById(R.id.flu_text);
        travText = (TextView) findViewById(R.id.trav_text);
        drsgText = (TextView) findViewById(R.id.drsg_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);//
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//
        mWeatherId = getIntent().getStringExtra("weather_id");//

        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(mWeatherId);//
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                requestWeather(mWeatherId);
            }
        });
    }
    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" +
                weatherId + "&key=07b2cafa9b3442cd9bc9d6c666bc9402";
        //https://free-api.heweather.com/v5/weather?city=beijing&key=07b2cafa9b3442cd9bc9d6c666bc9402
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {//从当前线程切换到主线程进行判断
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)) {
                            //SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            //editor.putString("weather", responseText);
                            //editor.apply();
                            showWeatherInfo(weather);
                        }
                        else {
                            Toast.makeText(WeatherActivity.this, "请检查网络或者城市名称输入是否不正确", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);//
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);//
                    }
                });
            }
        });
    }
    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.city;
        String degree = weather.now.tmp + "℃";
        String weatherInfo = weather.now.cond.txt;
        String winddir = weather.now.wind.dir;
        String windsc = weather.now.wind.sc + " 级";
        String hum = weather.now.hum + "%";
        String fl = weather.now.fl + "℃";

        titleCity.setText(cityName);

        int flag=0;
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.getColumnIndex("city_name"));
                if(name.equals(cityName))
                    flag=1;
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(flag==0) {
            ContentValues values = new ContentValues();
            values.put("city_name", cityName);
            db.insert("City", null, values);
            values.clear();
        }

        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        dirText.setText(winddir);
        scText.setText(windsc);
        humText.setText(hum);
        flText.setText(fl);
        setBackground(weatherInfo);
        forecastLayout.removeAllViews();
        int i=0;
        for(Forecast forecast : weather.daily_forecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.indo_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.cond.txt_d);
            maxText.setText(forecast.tmp.max);
            minText.setText(forecast.tmp.min);
            data[i++] = cityName + "今天" + forecast.cond.txt_d + "最高气温:" + forecast.tmp.max + " / " + "最低气温:" + forecast.tmp.min;
            forecastLayout.addView(view);
        }
        if(weather.aqi != null) {
            qltyText.setText(weather.aqi.city.qlty);
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度:" + weather.suggestion.comf.txt;
        String carWash = "洗车指数:" + weather.suggestion.cw.txt;
        String sport = "运动指数:" + weather.suggestion.sport.txt;
        String drsg = "穿衣指数:" + weather.suggestion.drsg.txt;
        String flu = "感冒指数:" + weather.suggestion.flu.txt;
        String uv = "紫外线指数:" + weather.suggestion.uv.txt;
        String trav = "旅游指数:" + weather.suggestion.trav.txt;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        drsgText.setText(drsg);
        fluText.setText(flu);
        uvText.setText(uv);
        travText.setText(trav);
        weatherLayout.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Intent intent = new Intent(WeatherActivity.this, CityActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.share_item:
                Intent intent1 = new Intent(WeatherActivity.this, ShareActivity.class);
                intent1.putExtra("weather_text", data[0]);
                startActivity(intent1);
                break;
            //case R.id.finish_item:
            //    finish();
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setBackground(String weatherinfo) {
        if(weatherinfo.equals("晴")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunny));
        }
        if(weatherinfo.equals("多云")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudy));
        }
        if(weatherinfo.equals("少云")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.few_clouds));
        }
        if(weatherinfo.equals("晴间多云")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.partly_cloudy));
        }
        if(weatherinfo.equals("阴")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.overcast));
        }
        if(weatherinfo.equals("有风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.windy));
        }
        if(weatherinfo.equals("平静")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.calm));
        }
        if(weatherinfo.equals("微风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_windy));
        }
        if(weatherinfo.equals("和风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_windy));
        }
        if(weatherinfo.equals("清风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_windy));
        }
        if(weatherinfo.equals("强风/劲风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.strong_windy));
        }
        if(weatherinfo.equals("疾风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.strong_windy));
        }
        if(weatherinfo.equals("大风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.strong_windy));
        }
        if(weatherinfo.equals("烈风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.strong_windy));
        }
        if(weatherinfo.equals("风暴")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.storm));
        }
        if(weatherinfo.equals("狂爆风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.storm));
        }
        if(weatherinfo.equals("飓风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.storm));
        }
        if(weatherinfo.equals("龙卷风")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.tornado));
        }
        if(weatherinfo.equals("热带风暴")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.tropical_storm));
        }
        if(weatherinfo.equals("阵雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.rain));
        }
        if(weatherinfo.equals("强阵雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.rain));
        }
        if(weatherinfo.equals("雷阵雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.thundershower));
        }
        if(weatherinfo.equals("强雷阵雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.thundershower));
        }
        if(weatherinfo.equals("雷阵雨伴有冰雹")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.thundershower));
        }
        if(weatherinfo.equals("小雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_rain));
        }
        if(weatherinfo.equals("中雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.rain));
        }
        if(weatherinfo.equals("大雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.rain));
        }
        if(weatherinfo.equals("极端降雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.rain));
        }
        if(weatherinfo.equals("毛毛雨/细雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_rain));
        }
        if(weatherinfo.equals("暴雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.storm_rain));
        }
        if(weatherinfo.equals("大暴雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.storm_rain));
        }
        if(weatherinfo.equals("特大暴雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.storm_rain));
        }
        if(weatherinfo.equals("冻雨")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.storm_rain));
        }
        if(weatherinfo.equals("小雪")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_snow));
        }
        if(weatherinfo.equals("中雪")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_snow));
        }
        if(weatherinfo.equals("大雪")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
        }
        if(weatherinfo.equals("暴雪")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
        }
        if(weatherinfo.equals("雨夹雪")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
        }
        if(weatherinfo.equals("雨雪天气")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
        }
        if(weatherinfo.equals("阵雨夹雪")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow));
        }
        if(weatherinfo.equals("阵雪")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.snow_flurry));
        }
        if(weatherinfo.equals("薄雾")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.foggy));
        }
        if(weatherinfo.equals("雾")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.foggy));
        }
        if(weatherinfo.equals("霾")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.haze));
        }
        if(weatherinfo.equals("扬沙")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.sand));
        }
        if(weatherinfo.equals("浮尘")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.sand));
        }
        if(weatherinfo.equals("沙尘暴")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.sand));
        }
        if(weatherinfo.equals("强沙尘暴")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.sand));
        }
        if(weatherinfo.equals("热")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.hot));
        }
        if(weatherinfo.equals("冷")) {
            mBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.cold));
        }
    }
}
