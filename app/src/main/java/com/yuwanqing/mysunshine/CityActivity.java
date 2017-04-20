package com.yuwanqing.mysunshine;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog.Builder;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yuwanqing.mysunshine.db.City;
import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.HttpUtil;
import com.yuwanqing.mysunshine.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CityActivity extends AppCompatActivity {
    private String[] citys;
    private String[] foreigncities;
    private String recentcity;
    private int j;
    private int k;

    private Button add;
    private Button delete;
    private ListView cityLayout;
    private TextView cityText;
    private ListView cities;
    private String[] data1;
    private List<String> cityList = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        citys = new String[3181];
        citys = Utility.getCities();
        foreigncities = new String[1700];
        foreigncities = Utility.getForeignCities();
        recentcity = getIntent().getStringExtra("weather_id");
        add = (Button) findViewById(R.id.add_city);
        delete = (Button) findViewById(R.id.fanhui_city);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            int flag = 0;
            @Override
            public void onClick(View v){
                Weather weather = Utility.handleWeatherResponse(recentcity);
                SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
                Cursor cursor = db.query("City", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    do{
                        String id = cursor.getString(cursor.getColumnIndex("city_id"));
                        if(weather.basic.id.equals(id)){
                            if(Utility.isCities(weather.basic.id, citys)) {
                                intent = new Intent(CityActivity.this, WeatherActivity.class);
                                intent.putExtra("weather_id", recentcity);
                            }else{
                                intent = new Intent(CityActivity.this, WeatherForeignActivity.class);
                                intent.putExtra("weather_id", recentcity);
                            }
                            flag = 1;
                        }
                    }while(cursor.moveToNext());
                }
                if(flag == 0) {
                    if (cursor.moveToFirst()) {
                        String id = cursor.getString(cursor.getColumnIndex("city_id"));
                        if(Utility.isCities(id, citys)) {
                            intent = new Intent(CityActivity.this, WeatherActivity.class);
                            intent.putExtra("weather_id", id);
                        }else{
                            intent = new Intent(CityActivity.this, WeatherForeignActivity.class);
                            intent.putExtra("weather_id", id);
                        }
                        flag = 2;
                    } else {
                        flag = 3;
                        intent = new Intent(CityActivity.this, SecondActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        });
        equry();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                CityActivity.this, android.R.layout.simple_list_item_1, cityList);
        final ListView listView = (ListView) findViewById(R.id.city);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String city = cityList.get(position);
                Builder builder = new Builder(CityActivity.this);
                builder.setMessage("请选择一个操作");
                builder.setTitle("编辑");
                builder.setPositiveButton("查询", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
                        //查询Book表中的所有的数据
                        Cursor cursor = db.query("City", null, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            do {
                                //遍历Cursor对象，取出数据并打印
                                String name = cursor.getString(cursor.getColumnIndex("city_name"));
                                String id = cursor.getString(cursor.getColumnIndex("city_id"));
                                if (name.equals(city)) {
                                    if(Utility.isCities(id, citys)){
                                        intent = new Intent(CityActivity.this, WeatherActivity.class);
                                        intent.putExtra("weather_id", id);
                                        break;
                                    }
                                    else {
                                        intent = new Intent(CityActivity.this, WeatherForeignActivity.class);
                                        intent.putExtra("weather_id", id);
                                        break;
                                    }
                                }
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        startActivity(intent);
                        CityActivity.this.finish();
                    }
                });
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
                        db.delete("City", "city_name = ?", new String[]{city});
                        cityList.remove(position);
                        listView.setAdapter(adapter);
                    }
                });
                builder.create().show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void equry() {
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.getColumnIndex("city_name"));
                cityList.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
