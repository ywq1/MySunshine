package com.yuwanqing.mysunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yuwanqing.mysunshine.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {
    private LocationClient mLocationClient;//LocationClient定位服务的客户端
    private StringBuilder currentPosition;
    private Button dingwei1;
    private String locationinfo;
    private String[] cities;
    private int j;

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
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext，注意该方法要再setContentView方法之前实现
        mLocationClient = new LocationClient(getApplicationContext());
        //注册一个定位监听器，当获取到位置信息的时候，就会回调这个定位监听器
        mLocationClient.registerLocationListener(new SecondActivity.MyLocationListener());
        setContentView(R.layout.activity_second);
        button = (Button) findViewById(R.id.tianjia);
        editCity = (EditText) findViewById(R.id.edit_city);
        dingwei1 = (Button) findViewById(R.id.dingwei);
        cities = new String[6362];
        cities = getCities();

        requestLocation();
        dingwei1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tongzhou = "通州";
                String tongzhouqu = tongzhou + "区";
                if (locationinfo.equals(tongzhouqu)) {
                    Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", tongzhou);
                    startActivity(intent);
                    finish();
                }
            }
        });
        click();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityBase.dbHelper.getWritableDatabase();
                cityname = editCity.getText().toString();
                if(isCities(cityname)) {
                    //Intent intent = new Intent(MainActivity.this, ChooseAreaActivity.class);
                    Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", cityname);
                    startActivity(intent);
                    finish();
                }
                else {
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

            locationinfo = location.getDistrict();

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
        datong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", datong.getText().toString());
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
        xian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, WeatherActivity.class);
                intent.putExtra("weather_id", xian.getText().toString());
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
                intent.putExtra("weather_id",chengdu.getText().toString());
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
    //获取所有城市的id和name
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
                        Toast.makeText(SecondActivity.this, "请检查网络情况", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return city;
    }
    //判断输入的城市id是否在可查询的城市数组里
    public boolean isCities(String city_info) {
        int flag = 0;
        for(int j=0;j<6362;j++){
            if(city_info.equals(cities[j])){
                flag = 1;
            }
        }
        if(flag==1) {
            return true;
        }
        else {
            return false;
        }
    }
}
