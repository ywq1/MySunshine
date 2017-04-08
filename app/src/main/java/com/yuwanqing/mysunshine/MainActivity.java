package com.yuwanqing.mysunshine;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.HttpUtil;
import com.yuwanqing.mysunshine.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String[] b = new String[3181];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CityBase.dbHelper = new MyDatabaseHelper(this, "Cities.db", null, 1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            db = CityBase.dbHelper.getWritableDatabase();
            String[] city = new String[3181];
            //查询Book表中的所有的数据
            Cursor cursor = db.query("City_id", null, null, null, null, null, null);
            cursor.moveToFirst();
            for(int j=0;j<3181;j++){
                String name = cursor.getString(cursor.getColumnIndex("city_name"));
                city[j] = name;
                cursor.moveToNext();
            }
            cursor.moveToFirst();
            cursor.close();
            final Weather weather1 = Utility.handleWeatherResponse(weatherString);
            String city1 = weather1.basic.city;
            if(isCities(city1, city) == 1) {
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("weather_id", weather1.basic.id);
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
            getCities();
            while(true) {
                if(b[3180]!=null) {
                    break;
                }
                //Log.v("MainActivity", "我们是 ");
            }
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
    //获取所有城市的id和name
    public void getCities() {
        db = CityBase.dbHelper.getWritableDatabase();
        String weatherUrl = "https://cdn.heweather.com/china-city-list.json";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                try{
                    JSONArray jsonArray = new JSONArray(responseText);
                    int a = jsonArray.length();
                    for(int i=0;i<a;i++){
                        JSONObject resultsObject = jsonArray.getJSONObject(i);
                        String aa = resultsObject.getString("cityEn");
                        String bb = resultsObject.getString("cityZh");
                        String cc = resultsObject.getString("leaderZh");
                        String dd = resultsObject.getString("provinceZh");
                        String ee = resultsObject.getString("countryZh");
                        String ff = resultsObject.getString("id");
                        ContentValues values = new ContentValues();
                        b[i] = aa;
                        values.put("city_en", aa);
                        values.put("city_name", bb);
                        values.put("city_leader", cc);
                        values.put("city_id", ff);
                        values.put("city_name_country", bb +"-" + cc + "，" + dd + "，" +ee);
                        db.insert("City_id", null, values);
                        values.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        /*
        HttpUtil.sendOkHttpRequest(weatherUrl1, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                try{
                    JSONArray jsonArray = new JSONArray(responseText);
                    int a = jsonArray.length();
                    for(int i=0;i<a;i++){
                        JSONObject resultsObject = jsonArray.getJSONObject(i);
                        String aa = resultsObject.getString("id");
                        String bb = resultsObject.getString("cityEn");
                        String cc = resultsObject.getString("cityZh");
                        String dd = resultsObject.getString("continent");
                        String ee = resultsObject.getString("countryEn");
                        ContentValues values = new ContentValues();

                        c[i] = aa;

                        values.put("foreigncity_id", aa);
                        values.put("foreigncity_en", bb);
                        values.put("foreigncity_name", cc);
                        values.put("foreigncity_continent", dd);
                        values.put("foreigncity_country", ee);
                        values.put("foreigncity_name_country", cc +"-" + ee + "，" + dd);
                        db.insert("ForeignCity", null, values);
                        values.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });//return 1;*/
    }
    //获取所有城市的id和name
    public void getForeignCities() {
        final SQLiteDatabase db1 = CityBase.dbHelper.getWritableDatabase();
        String weatherUrl = "https://cdn.heweather.com/world-top-city-list.json";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                try{
                    JSONArray jsonArray = new JSONArray(responseText);
                    int a = jsonArray.length();
                    int t=9;
                    for(int i=0;i<a;i++){
                        JSONObject resultsObject = jsonArray.getJSONObject(i);
                        String aa = resultsObject.getString("id");
                        String bb = resultsObject.getString("cityEn");
                        String cc = resultsObject.getString("cityZh");
                        String dd = resultsObject.getString("continent");
                        String ee = resultsObject.getString("countryEn");
                        ContentValues values = new ContentValues();

                        values.put("foreigncity_id", aa);
                        values.put("foreigncity_en", bb);
                        values.put("foreigncity_name", cc);
                        values.put("foreigncity_continent", dd);
                        values.put("foreigncity_country", ee);
                        values.put("foreigncity_name_country", cc +"-" + ee + "，" + dd);
                        db1.insert("ForeignCity", null, values);
                        values.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
