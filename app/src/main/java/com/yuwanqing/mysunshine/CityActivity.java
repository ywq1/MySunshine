package com.yuwanqing.mysunshine;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        citys = new String[6362];
        citys = getCities();
        foreigncities = new String[9144];
        foreigncities = getForeignCities();
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
                //Toast.makeText(CityActivity.this, city, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new Builder(CityActivity.this);
                builder.setMessage("请选择一个操作");
                builder.setTitle("编辑");
                builder.setPositiveButton("查询", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(isCities(city, citys) == 1) {
                            Intent intent = new Intent(CityActivity.this, WeatherActivity.class);
                            intent.putExtra("weather_id", city);
                            startActivity(intent);
                            CityActivity.this.finish();
                        }
                        if(isCities(city, foreigncities) == 1){
                            Intent intent = new Intent(CityActivity.this, WeatherForeignActivity.class);
                            intent.putExtra("weather_id", city);
                            startActivity(intent);
                            CityActivity.this.finish();
                        }
                    }
                });
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(CityActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
                        db.delete("City", "city_name = ?", new String[] { city });
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
    }
    public void equry(){
        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.getColumnIndex("city_name"));
                cityList.add(name);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
    public String[] getCities() {
        final String[] city;
        j=0;
        city = new String[6362];
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
                        city[j++] = aa;
                        city[j++] = bb;
                        //currentPosition.append(" ").append(resultsObject.getString("cityEn")).append(resultsObject.getString("cityZh"));
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
                        Toast.makeText(CityActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return city;
    }
    //获取所有城市的id和name
    public String[] getForeignCities() {
        final String[] city;
        k=0;
        city = new String[9144];
        String weatherUrl = "https://cdn.heweather.com/world-top-city-list.json";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                try{
                    JSONArray jsonArray = new JSONArray(responseText);
                    int a = jsonArray.length();
                    int b=1;
                    for(int i=0;i<a;i++){
                        JSONObject resultsObject = jsonArray.getJSONObject(i);
                        String aa = resultsObject.getString("cityEn");
                        String bb = resultsObject.getString("cityZh");
                        city[k++] = aa;
                        city[k++] = bb;
                        //currentPosition.append(" ").append(resultsObject.getString("cityEn")).append(resultsObject.getString("cityZh"));
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
                        Toast.makeText(CityActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return city;
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
