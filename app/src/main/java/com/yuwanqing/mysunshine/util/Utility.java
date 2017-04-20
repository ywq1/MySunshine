package com.yuwanqing.mysunshine.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yuwanqing.mysunshine.CityBase;
import com.yuwanqing.mysunshine.R;
import com.yuwanqing.mysunshine.db.City;
import com.yuwanqing.mysunshine.db.County;
import com.yuwanqing.mysunshine.db.Province;
import com.yuwanqing.mysunshine.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuwanqing on 2017-03-29.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();//调用save（）方法将数据存储到数据库中
                }
                return true;
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();//调用save（）方法将数据存储到数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();//调用save（）方法将数据存储到数据库中
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getCities() {
        final String[] city;
        city = new String[3181];
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();;
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City_China", null, null, null, null, null, null);
        cursor.moveToFirst();
        for(int i=0;i<3181;i++,cursor.moveToNext()) {
            //遍历Cursor对象，取出数据并打印
            String id = cursor.getString(cursor.getColumnIndex("city_id"));
            city[i] = id;
        }
        return city;
    }
    public static String[] getForeignCities() {
        final String[] city;
        city = new String[1700];
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
        Cursor cursor = db.query("ForeignCity", null, null, null, null, null, null);
        cursor.moveToFirst();
        for(int i = 0;i<1700;i++,cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("foreigncity_id"));
            city[i] = id;
        }
        return city;
    }

    //判断输入的城市id是否在可查询的城市数组里
    public static boolean isCities(String city_info, String[] cityfc) {
        int flag = 0;
        for (int j = 0; j < cityfc.length; j++) {
            if (city_info.equals(cityfc[j])) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            return true;
        } else {
            return false;
        }
    }
}
