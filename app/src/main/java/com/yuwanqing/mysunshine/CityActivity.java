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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {
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
                        //Toast.makeText(CityActivity.this, "查询", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(CityActivity.this, WeatherActivity.class);
                        intent.putExtra("weather_id", city);
                        startActivity(intent);
                        CityActivity.this.finish();
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

}
