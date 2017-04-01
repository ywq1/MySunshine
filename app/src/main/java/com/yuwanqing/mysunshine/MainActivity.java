package com.yuwanqing.mysunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText editCity;
    private String cityname;
    private Button beijing;
    private Button tianjin;
    private Button shanghai;
    private Button nanjing;
    private Button shijiazhuang;
    private Button guangzhou;
    private Button shenzhen;
    private Button zhuhai;
    private Button taiyuan;
    private Button suzhou;
    private Button hangzhou;
    private Button jinan;
    private Button datong;
    private Button changsha;
    private Button xian;
    private Button chongqing;
    private Button chengdu;
    private Button wuhan;
    private Button xiamen;
    private Button nanning;
    private Button qingdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CityBase.dbHelper = new MyDatabaseHelper(this, "Cities.db", null, 1);
        button = (Button) findViewById(R.id.tianjia);
        editCity = (EditText) findViewById(R.id.edit_city);
        click();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityBase.dbHelper.getWritableDatabase();
                cityname = editCity.getText().toString();
                //Intent intent = new Intent(MainActivity.this, ChooseAreaActivity.class);
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", cityname);
                startActivity(intent);
                finish();
            }
        });
    }
    public void click(){
        beijing = (Button) findViewById(R.id.beijing);
        tianjin = (Button) findViewById(R.id.tianjin);
        shanghai = (Button) findViewById(R.id.shanghai);
        nanjing = (Button) findViewById(R.id.nanjing);
        shijiazhuang = (Button) findViewById(R.id.shijiazhuang);
        guangzhou = (Button) findViewById(R.id.guangzhou);
        shenzhen = (Button) findViewById(R.id.shenzhen);
        zhuhai = (Button) findViewById(R.id.zhuhai);
        taiyuan = (Button) findViewById(R.id.taiyuan);
        suzhou = (Button) findViewById(R.id.suzhou);
        hangzhou = (Button) findViewById(R.id.hangzhou);
        jinan = (Button) findViewById(R.id.jinan);
        datong = (Button) findViewById(R.id.datong);
        changsha = (Button) findViewById(R.id.changsha);
        xian = (Button) findViewById(R.id.xian);
        chongqing = (Button) findViewById(R.id.chongqing);
        chengdu = (Button) findViewById(R.id.chengdu);
        wuhan = (Button) findViewById(R.id.wuhan);
        xiamen = (Button) findViewById(R.id.xiamen);
        nanning = (Button) findViewById(R.id.nanning);
        qingdao = (Button) findViewById(R.id.qingdao);

        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", beijing.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", tianjin.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", shanghai.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        nanjing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", nanjing.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        shijiazhuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", shijiazhuang.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        guangzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", guangzhou.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        shenzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", shenzhen.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        zhuhai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", zhuhai.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        taiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", taiyuan.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        suzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", suzhou.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        hangzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", hangzhou.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        jinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", jinan.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        datong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", datong.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        changsha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", changsha.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        xian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", xian.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        chongqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", chongqing.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        chengdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id",chengdu.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        wuhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", wuhan.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        xiamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", xiamen.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        nanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", nanning.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        qingdao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", qingdao.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }
}
