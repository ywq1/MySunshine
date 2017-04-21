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
import com.yuwanqing.mysunshine.util.Utility;

import java.io.IOException;
import java.security.AuthProvider;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView xiangyou;
    private TextView xiangzuo;
    private String city_you;
    private String recentcity;
    private String[] citys;

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
    private TextView qltyText;
    private TextView aqiText;
    private TextView pm10Text;
    private TextView date_loc;
    private TextView detail;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView fluText;
    private TextView drsgText;
    private TextView travText;
    private TextView uvText;
    private String data[];
    private String city;
    private String date;
    private String aqi1=null;
    private String qlty=null;
    private String pm25=null;
    private String pm10=null;
    private String no2=null;
    private String so2=null;
    private String co=null;
    private String o3=null;
    private String responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = new String[3];
        citys = new String[3181];
        citys = Utility.getCities();
        //初始化各控件
        xiangyou = (TextView) findViewById(R.id.xiangyou);
        xiangzuo = (TextView) findViewById(R.id.xiangzuo);
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
        pm10Text = (TextView) findViewById(R.id.pm10_text);
        date_loc = (TextView) findViewById(R.id.date);
        detail = (TextView) findViewById(R.id.detail);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        fluText = (TextView) findViewById(R.id.flu_text);
        travText = (TextView) findViewById(R.id.trav_text);
        drsgText = (TextView) findViewById(R.id.drsg_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
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
                    }else if(name.equals(mWeatherId1)) {
                        flag = 1;
                        break;
                    }
                }while(cursor.moveToNext());
            }
            if(flag == 1) {
                responseText = city_weather;
                recentcity = city_weather;
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
                Intent intent = new Intent(WeatherActivity.this, CityActivity.class);
                intent.putExtra("weather_id", recentcity);
                startActivity(intent);
                finish();
            }
        }
        //自动刷新
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
                requestWeather(mWeatherId1);
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                requestWeather(mWeatherId1);
            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                bundle.putString("date", date);
                bundle.putString("aqi1", aqi1);
                bundle.putString("qlty", qlty);
                bundle.putString("pm25", pm25);
                bundle.putString("pm10", pm10);
                bundle.putString("no2", no2);
                bundle.putString("so2", so2);
                bundle.putString("co", co);
                bundle.putString("o3", o3);
                intent.putExtras(bundle);
                startActivity(intent);
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
                                    intent = new Intent(WeatherActivity.this, WeatherActivity.class);
                                    intent.putExtra("weather_id", id);
                                    finish();
                                }else {
                                    xiangyou.setVisibility(View.GONE);
                                    intent = new Intent(WeatherActivity.this, WeatherForeignActivity.class);
                                    intent.putExtra("weather_id", id);
                                    finish();
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
                                    intent = new Intent(WeatherActivity.this, WeatherActivity.class);
                                    intent.putExtra("weather_id", id);
                                    finish();
                                }else {
                                    xiangzuo.setVisibility(View.GONE);
                                    intent = new Intent(WeatherActivity.this, WeatherForeignActivity.class);
                                    intent.putExtra("weather_id", id);
                                    finish();
                                }
                                flag = 1;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Weather weather1 = Utility.handleWeatherResponse(recentcity);
        showWeatherInfo(weather1);
    }
    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" +
                weatherId + "&key=07b2cafa9b3442cd9bc9d6c666bc9402";
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
                            Toast.makeText(WeatherActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "刷新天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }
    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
        editor.putString("weather", responseText);
        editor.apply();
        int temp_flag = 1;
        if(!(CityBase.temp_unit.equals("摄氏度℃"))){
            temp_flag = 0;
        }

        String cityName = weather.basic.city;
        city = cityName;
        date = weather.basic.update.loc;
        String[] c=date.split(" ");
        String cityId = weather.basic.id;
        //String degree = weather.now.tmp + "℃";
        String degree = Utility.format(temp_flag, weather.now.tmp);
        String weatherInfo = weather.now.cond.txt;
        String winddir = weather.now.wind.dir;
        String windsc = weather.now.wind.sc + " 级";
        String hum = weather.now.hum + "%";
        //String fl = weather.now.fl + "℃";
        String fl = Utility.format(temp_flag, weather.now.fl);

        titleCity.setText(cityName);

        int flag=0;
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        Cursor cursor2 = db.query("City", null, null, null, null, null, null);
        Cursor cursor3 = db.query("City", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.getColumnIndex("city_name"));
                String id = cursor.getString(cursor.getColumnIndex("city_id"));
                if(name.equals(cityName)){
                    if(!id.equals(cityId)) {
                        cityName = cityName + "1";
                        if(cursor2.moveToFirst()) {
                            do{
                                String name2 = cursor2.getString(cursor2.getColumnIndex("city_name"));
                                String id2 = cursor2.getString(cursor2.getColumnIndex("city_id"));
                                if(name2.equals(cityName)) {
                                    if(!id2.equals(cityId)) {
                                        cityName = cityName + "1";
                                        if(cursor3.moveToFirst()) {
                                            do{
                                                String name3 = cursor3.getString(cursor3.getColumnIndex("city_name"));
                                                if(name3.equals(cityName)) {
                                                    flag = 1;
                                                    break;
                                                }
                                            }while(cursor3.moveToNext());
                                        }
                                        break;
                                    }
                                    else {
                                        flag = 1;
                                        break;
                                    }
                                }
                            }while(cursor2.moveToNext());
                        }
                        break;
                    }
                    else {
                        flag = 1;
                        break;
                    }
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        cursor2.close();
        cursor3.close();
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
            db.update("City", values, "city_id = ?", new String[] { cityId });
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
            //maxText.setText(forecast.tmp.max);
            //minText.setText(forecast.tmp.min);
            maxText.setText(Utility.format(temp_flag, forecast.tmp.max));
            minText.setText(Utility.format(temp_flag, forecast.tmp.min));
            //data[i++] = forecast.cond.txt_d + "，最高气温:" + forecast.tmp.max + "℃，" + "最低气温:" + forecast.tmp.min + "℃";
            data[i++] = forecast.cond.txt_d + "，最高气温:" + Utility.format(temp_flag, forecast.tmp.max)
                    + "最低气温:" + Utility.format(temp_flag, forecast.tmp.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi != null) {
            qltyText.setText(weather.aqi.city.qlty);
            aqiText.setText(weather.aqi.city.aqi);
            pm10Text.setText(weather.aqi.city.pm10);
            date_loc.setText(c[1] + "发布 ");
            aqi1 = weather.aqi.city.aqi;
            qlty = weather.aqi.city.qlty;
            pm25 = weather.aqi.city.pm25;
            pm10 = weather.aqi.city.pm10;
            no2 = weather.aqi.city.no2;
            so2 = weather.aqi.city.so2;
            co = weather.aqi.city.co;
            o3 = weather.aqi.city.o3;
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
                intent.putExtra("weather_id", recentcity);
                startActivity(intent);
                finish();
                break;
            case R.id.share_item:
                Intent intent1 = new Intent(WeatherActivity.this, ShareActivity.class);
                intent1.putExtra("weather_text", city + ": \n"
                        + "今天: " + data[0] + "\n"
                        + "明天：" + data[1] + "\n"
                        + "后天：" + data[2] + "\n" + date + "发布");
                startActivity(intent1);
                break;
            case R.id.finish_item:
                Intent intent3 = new Intent(WeatherActivity.this, SettingActivity.class);
                startActivity(intent3);
                break;
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
