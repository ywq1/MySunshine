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
import com.yuwanqing.mysunshine.util.HttpUtil;

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
    private int j;
    private int k;

    private Button add;
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
        citys = getCities();
        foreigncities = new String[1678];
        for(int h=0;h<1678;h++) {
            foreigncities[h] = CityBase.fcity[h][0];
        }
        //foreigncities = getForeignCities();
        //cityLayout = (ListView) findViewById(R.id.city);
        add = (Button) findViewById(R.id.add_city);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, SecondActivity.class);
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
                                    if(isCities(id, citys)){
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

    public String[] getCities() {
        final String[] city;
        j = 0;
        city = new String[3181];
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();;
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City_id", null, null, null, null, null, null);
        cursor.moveToFirst();
        for(int i=0;i<3181;i++,cursor.moveToNext()) {
            //遍历Cursor对象，取出数据并打印
            String id = cursor.getString(cursor.getColumnIndex("city_id"));
            city[i] = id;
        }
        return city;
    }

    //获取所有城市的id和name
    public String[] getForeignCities() {
        final String[] city;
        k = 0;
        city = new String[4572];
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();;
        //查询Book表中的所有的数据
        Cursor cursor2 = db.query("ForeignCity", null, null, null, null, null, null);
        cursor2.moveToFirst();
        for(int j=0;j<4572;j++,cursor2.moveToNext()){
            String name_country = cursor2.getString(cursor2.getColumnIndex("foreigncity_name_country"));
            String name = cursor2.getString(cursor2.getColumnIndex("foreigncity_name"));
            String id = cursor2.getString(cursor2.getColumnIndex("foreigncity_id"));
            city[j] = id;
        }
        return city;
    }

    //判断输入的城市id是否在可查询的城市数组里
    public boolean isCities(String city_info, String[] cityfc) {
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "City Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.yuwanqing.mysunshine/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "City Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.yuwanqing.mysunshine/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}
