package com.yuwanqing.mysunshine;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuwanqing.mysunshine.gson.Forecast;
import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.HttpUtil;
import com.yuwanqing.mysunshine.util.StackManager;
import com.yuwanqing.mysunshine.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherForeignActivity extends AppCompatActivity {
    private TextView xiangyou;
    private TextView xiangzuo;
    private String city_you;
    private String recentcity;
    private String[] citys;
    private String[] foreigncities;

    private FrameLayout mBackground;
    private SwipeRefreshLayout swipeRefresh;//下拉刷新
    private String mWeatherId;
    private String mWeatherId1;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView weatherInfoText;
    private TextView dirText;
    private TextView scText;
    private TextView humText;
    private TextView flText;
    private LinearLayout forecastLayout;
    private String data[];
    private String city;
    private String date;

    private String responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_foreign);

        Toolbar toolbar = (Toolbar) findViewById(R.id.foreign_toolbar);
        setSupportActionBar(toolbar);
        data = new String[3];
        citys = new String[3181];
        citys = Utility.getCities();
        foreigncities = new String[1700];
        foreigncities = Utility.getForeignCities();
        //初始化各控件
        xiangyou = (TextView) findViewById(R.id.foreign_xiangyou);
        xiangzuo = (TextView) findViewById(R.id.foreign_xiangzuo);
        mBackground = (FrameLayout) findViewById(R.id.foreign_main_bg);
        weatherLayout = (ScrollView) findViewById(R.id.foreign_weather_layout);
        titleCity = (TextView) findViewById(R.id.city_text);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        dirText = (TextView) findViewById(R.id.dir_text);
        scText = (TextView) findViewById(R.id.sc_text);
        humText = (TextView) findViewById(R.id.hum_text);
        flText = (TextView) findViewById(R.id.fl_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.foreign_swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mWeatherId = getIntent().getStringExtra("weather_id");

        weatherLayout.setVisibility(View.INVISIBLE);
        if(mWeatherId.length()<15) {
            mWeatherId1 = mWeatherId;
            SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
            Cursor cursor = db.query("City", null, null, null, null, null, null);
            String city_weather = null;
            int flag = 0;
            if (cursor.moveToFirst()) {
                do{
                    String id = cursor.getString(cursor.getColumnIndex("city_id"));
                    String name = cursor.getString(cursor.getColumnIndex("city_name"));
                    city_weather = cursor.getString(cursor.getColumnIndex("city_weather"));
                    if(id.equals(mWeatherId1)){
                        flag = 1;
                        break;
                    }
                    else if(name.equals(mWeatherId1)) {
                        flag = 1;
                        break;
                    }
                }while(cursor.moveToNext());
            }
            if(flag == 1) {
                responseText = city_weather;
                Weather weather = Utility.handleWeatherResponse(city_weather);
                showWeatherInfo(weather);
            }
            else {
                requestWeather(mWeatherId1);
            }
        }
        else{
            recentcity = mWeatherId;
            responseText = mWeatherId;
            final Weather weather = Utility.handleWeatherResponse(mWeatherId);
            mWeatherId1 = weather.basic.id;
            SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
            Cursor cursor = db.query("City", null, null, null, null, null, null);
            int flag = 0;
            if (cursor.moveToFirst()) {
                do{
                    String id = cursor.getString(cursor.getColumnIndex("city_id"));
                    if(id.equals(mWeatherId1)){
                        flag = 1;
                        break;
                    }
                }while(cursor.moveToNext());
            }
            if(flag == 1) {
                showWeatherInfo(weather);
            }
            else {
                Intent intent = new Intent(WeatherForeignActivity.this, CityActivity.class);
                intent.putExtra("weather_id", recentcity);
                startActivity(intent);
                finish();
            }
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                requestWeather(mWeatherId1);
            }
        });
        xiangyou.setOnClickListener(new View.OnClickListener() {
            String name;
            String id;
            Intent intent;
            int flag=0;
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
                Cursor cursor = db.query("City", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    do{
                        name = cursor.getString(cursor.getColumnIndex("city_name"));
                        if(name.equals(city_you)){
                            if(cursor.moveToNext()) {
                                id = cursor.getString(cursor.getColumnIndex("city_id"));
                                if(Utility.isCities(id, citys)) {
                                    xiangyou.setVisibility(View.GONE);
                                    intent = new Intent(WeatherForeignActivity.this, WeatherActivity.class);
                                    intent.putExtra("weather_id", id);
                                }else {
                                    xiangyou.setVisibility(View.GONE);
                                    intent = new Intent(WeatherForeignActivity.this, WeatherForeignActivity.class);
                                    intent.putExtra("weather_id", id);
                                }
                                flag=1;
                            }
                            else{
                                xiangyou.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
                if(flag==1) {
                    startActivity(intent);
                    finish();
                }
            }
        });
        xiangzuo.setOnClickListener(new View.OnClickListener() {
            String name;
            String id;
            Intent intent;
            int flag=0;
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
                Cursor cursor = db.query("City", null, null, null, null, null, null);
                if(cursor.moveToLast()) {
                    do{
                        name = cursor.getString(cursor.getColumnIndex("city_name"));
                        if(name.equals(city_you)){
                            if(cursor.moveToPrevious()) {
                                id = cursor.getString(cursor.getColumnIndex("city_id"));
                                if(Utility.isCities(id, citys)) {
                                    xiangzuo.setVisibility(View.GONE);
                                    intent = new Intent(WeatherForeignActivity.this, WeatherActivity.class);
                                    intent.putExtra("weather_id", id);
                                }else{
                                    xiangzuo.setVisibility(View.GONE);
                                    intent = new Intent(WeatherForeignActivity.this, WeatherForeignActivity.class);
                                    intent.putExtra("weather_id", id);
                                }
                                flag=1;
                            }
                            else{
                                xiangzuo.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }while(cursor.moveToPrevious());
                }
                cursor.close();
                if(flag==1) {
                    startActivity(intent);
                    finish();
                }
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
                responseText = response.body().string();
                recentcity = responseText;
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {//从当前线程切换到主线程进行判断
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)) {
                            showWeatherInfo(weather);
                        }
                        else {
                            Toast.makeText(WeatherForeignActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WeatherForeignActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
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
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherForeignActivity.this).edit();
        editor.putString("weather", responseText);
        editor.apply();

        String cityName = weather.basic.city;
        city = cityName;
        date = weather.basic.update.loc;
        String cityId = weather.basic.id;
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
                String name = cursor.getString(cursor.getColumnIndex("city_id"));
                if(name.equals(cityId)){
                    flag=1;
                    break;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(flag==0) {
            ContentValues values = new ContentValues();
            values.put("city_name", cityName);
            values.put("city_id", cityId);
            values.put("city_weather", responseText);
            db.insert("City", null, values);
            values.clear();
        }
        else{
            ContentValues values = new ContentValues();
            values.put("city_weather", responseText);
            db.update("City", values, "city_id = ?", new String[]{cityId});
        }
        city_you = cityName;

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
            data[i++] = forecast.cond.txt_d + "，最高气温:" + forecast.tmp.max + "℃，"
                    + "最低气温:" + forecast.tmp.min + "℃";
            forecastLayout.addView(view);
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Intent intent = new Intent(WeatherForeignActivity.this, CityActivity.class);
                intent.putExtra("weather_id", recentcity);
                startActivity(intent);
                finish();
                break;
            case R.id.share_item:
                Intent intent1 = new Intent(WeatherForeignActivity.this, ShareActivity.class);
                intent1.putExtra("weather_text", city + ": \n今天：" + data[0] + "\n明天："
                        + data[1] + "\n后天：" + data[2] + "\n" + "当地时间" + date + "发布");
                startActivity(intent1);
                break;
            case R.id.finish_item:
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
