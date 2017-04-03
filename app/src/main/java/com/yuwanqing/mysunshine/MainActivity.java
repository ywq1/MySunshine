package com.yuwanqing.mysunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.Utility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CityBase.dbHelper = new MyDatabaseHelper(this, "Cities.db", null, 1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            final Weather weather1 = Utility.handleWeatherResponse(weatherString);
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("weather_id", weather1.basic.city);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
