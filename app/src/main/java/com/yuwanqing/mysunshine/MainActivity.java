package com.yuwanqing.mysunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.HttpUtil;
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
            String city1 = weather1.basic.city;
            if(isCities(city1, CityBase.cities1) == 1) {
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("weather_id", weather1.basic.city);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(this, WeatherForeignActivity.class);
                intent.putExtra("weather_id", weather1.basic.city);
                startActivity(intent);
                finish();
            }
        }
        else {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            finish();
        }
    }
    //判断输入的城市id是否在可查询的城市数组里
    public int isCities(String city_info, String[] cityfc) {
        int flag = 0;
        for(int j=0;j<cityfc.length;j++){
            if(city_info.equals(cityfc[j])){
                flag = 1;
                break;
            }
        }
        if(flag==1) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
