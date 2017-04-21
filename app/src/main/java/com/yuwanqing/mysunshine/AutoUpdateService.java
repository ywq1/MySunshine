package com.yuwanqing.mysunshine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.SystemClock;

import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.HttpUtil;
import com.yuwanqing.mysunshine.util.Utility;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.Callback;

public class AutoUpdateService extends Service {
    private SQLiteDatabase db;
    private String id;
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1 * 60 * 60 * 1000;//这是1个小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
    //更新天气信息
    private void updateWeather(){
        db = CityBase.dbHelper.getWritableDatabase();
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                id = cursor.getString(cursor.getColumnIndex("city_id"));;
                String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" +
                        id + "&key=07b2cafa9b3442cd9bc9d6c666bc9402";
                HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText = response.body().string();
                        Weather weather = Utility.handleWeatherResponse(responseText);
                        if (weather != null && "ok".equals(weather.status)) {
                            ContentValues values = new ContentValues();
                            values.put("city_weather", responseText);
                            db.update("City", values, "city_id = ?", new String[] { id });
                        }
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                });
            }while(cursor.moveToNext());
        }
    }
}
