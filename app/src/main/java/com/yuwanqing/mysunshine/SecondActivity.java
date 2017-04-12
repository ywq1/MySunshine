package com.yuwanqing.mysunshine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yuwanqing.mysunshine.gson.Weather;
import com.yuwanqing.mysunshine.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {

    private LocationClient mLocationClient;//LocationClient定位服务的客户端
    private StringBuilder currentPosition;
    private Button dingwei1;
    private String locationinfo;
    private String[] ary;
    private String[][] ary2;
    private String[][] ary1;
    private String[][] ary3;
    private int k;

    private Button button;
    private AutoCompleteTextView editCity;
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
    private Button foshan;
    private Button changsha;
    private Button zhengzhou;
    private Button chongqing;
    private Button chengdu;
    private Button wuhan;
    private Button xiamen;
    private Button nanning;
    private Button qingdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext，注意该方法要再setContentView方法之前实现
        mLocationClient = new LocationClient(getApplicationContext());
        //注册一个定位监听器，当获取到位置信息的时候，就会回调这个定位监听器
        mLocationClient.registerLocationListener(new SecondActivity.MyLocationListener());
        setContentView(R.layout.activity_second);

        SQLiteDatabase db = CityBase.dbHelper.getWritableDatabase();
        ary = new String[4859];//3181+1678
        ary2 = new String[3181][3];
        ary1 = new String[3181][2];
        ary3 = new String[1678][2];
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City_id", null, null, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < 3181; i++, cursor.moveToNext()) {
            //遍历Cursor对象，取出数据并打印
            String name_country = cursor.getString(cursor.getColumnIndex("city_name_country"));
            String leader = cursor.getString(cursor.getColumnIndex("city_leader"));
            String id = cursor.getString(cursor.getColumnIndex("city_id"));
            String name = cursor.getString(cursor.getColumnIndex("city_name"));
            ary[i] = name_country;
            ary2[i][0] = name;
            ary2[i][1] = leader;
            ary2[i][2] = id;
            ary1[i][0] = id;
            ary1[i][1] = name_country;
        }
        for (int j = 0; j < 1678; j++) {
            ary[3181 + j] = CityBase.fcity[j][2] + "-" + CityBase.fcity[j][4];
            ary3[j][0] = CityBase.fcity[j][0];
            ary3[j][1] = CityBase.fcity[j][2] + "-" + CityBase.fcity[j][4];
        }
        cursor.close();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ary);

        button = (Button) findViewById(R.id.tianjia);
        editCity = (AutoCompleteTextView) findViewById(R.id.edit_city);
        editCity.setAdapter(adapter);

        dingwei1 = (Button) findViewById(R.id.dingwei);
        dingwei1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] city_qu = new String[3181];
                for (int i = 0; i < 3181; i++) {
                    city_qu[i] = ary2[i][0] + "区" + ary2[i][1] + "市";
                }
                List<String> permissionList = new ArrayList<>();
                //判断ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION（是同一个权限组）,READ_PHONE_STATE,WRITE_EXTERNAL_STORAGE这4个
                //权限有没有被授权，如果没被授权就添加到List集合中
                if (ContextCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (ContextCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
                }
                if (ContextCompat.checkSelfPermission(SecondActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!permissionList.isEmpty()) {
                    String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                    ActivityCompat.requestPermissions(SecondActivity.this, permissions, 1);
                    Toast.makeText(SecondActivity.this, "没有获得定位权限", Toast.LENGTH_SHORT).show();
                } else {
                    requestLocation();
                    if(locationinfo == null) {
                        Toast.makeText(SecondActivity.this, "请再点击一次", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int m = 0;
                        for (m = 0; m < 3181; m++) {
                            if (locationinfo.equals(city_qu[m]))
                                break;
                        }
                        Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                        intent.putExtra("weather_id", ary2[m][2]);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        click();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityBase.dbHelper.getWritableDatabase();
                cityname = editCity.getText().toString();
                int flag = 0;
                int i = 0;
                int j = 0;
                for (i = 0; i < ary1.length; i++) {
                    if (cityname.equals(ary1[i][1])) {
                        flag = 1;
                        break;
                    }
                }
                for (j = 0; j < ary3.length; j++) {
                    if (cityname.equals(ary3[j][1])) {
                        flag = 2;
                        break;
                    }
                }
                if (flag == 1) {
                    Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", ary1[i][0]);
                    startActivity(intent);
                    finish();
                } else if (flag == 2) {
                    Intent intent = new Intent(SecondActivity.this, WeatherForeignActivity.class);
                    intent.putExtra("weather_id", ary3[j][0]);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SecondActivity.this, "城市输入不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();//LocationClientOption类，该类用来设置定位SDK的定位方式
        //配置定位SDK各配置参数，比如定位模式、定位时间间隔、坐标系类型等
        option.setScanSpan(5000);//每5秒定位一次
        ////可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        ////可选，设置是否需要地址信息，默认不需要
        mLocationClient.setLocOption(option);//设置定位参数
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    public class MyLocationListener implements BDLocationListener {
        //接收异步返回的定位结果
        @Override
        public void onReceiveLocation(BDLocation location) {
            //String 对象是不可改变的。每次使用 System.String 类中的方法之一时，都要在内存中创建一个新的字符串对象，
            // 这就需要为该新对象分配新的空间。在需要对字符串执行重复修改的情况下，与创建新的 String 对象相关的系统开销可能会非常昂贵。
            // 如果要修改字符串而不创建新的对象，则可以使用 System.Text.StringBuilder 类。例如，当在一个循环中将许多字符串连接在一起时，
            // 使用 StringBuilder 类可以提升性能。
            currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
            currentPosition.append("国家：").append(location.getCountry()).append("\n");
            currentPosition.append("省：").append(location.getProvince()).append("\n");
            currentPosition.append("市：").append(location.getCity()).append("\n");
            currentPosition.append("区：").append(location.getDistrict()).append("\n");

            locationinfo = location.getDistrict() + location.getCity();

            currentPosition.append("街道：").append(location.getStreet()).append("\n");
            currentPosition.append("定位方式：");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                currentPosition.append("网络");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    //热门城市的点击
    public void click() {
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
        foshan = (Button) findViewById(R.id.foshan);
        changsha = (Button) findViewById(R.id.changsha);
        zhengzhou = (Button) findViewById(R.id.zhengzhou);
        chongqing = (Button) findViewById(R.id.chongqing);
        chengdu = (Button) findViewById(R.id.chengdu);
        wuhan = (Button) findViewById(R.id.wuhan);
        xiamen = (Button) findViewById(R.id.xiamen);
        nanning = (Button) findViewById(R.id.nanning);
        qingdao = (Button) findViewById(R.id.qingdao);

        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", beijing.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        tianjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", tianjin.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        shanghai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", shanghai.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        nanjing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", nanjing.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        shijiazhuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", shijiazhuang.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        guangzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", guangzhou.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        shenzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", shenzhen.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        zhuhai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", zhuhai.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        taiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", taiyuan.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        suzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", suzhou.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        hangzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", hangzhou.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        jinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", jinan.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        foshan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", foshan.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        changsha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", changsha.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        zhengzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", zhengzhou.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        chongqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", chongqing.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        chengdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", chengdu.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        wuhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", wuhan.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        xiamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", xiamen.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        nanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", nanning.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        qingdao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", qingdao.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

}
