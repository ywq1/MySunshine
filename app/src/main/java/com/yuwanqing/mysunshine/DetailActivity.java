package com.yuwanqing.mysunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView cityDetail;
    private TextView dateDetail;
    private TextView aqiDetail;
    private TextView qltyDetail;
    private TextView pm25Detail;
    private TextView pm10Detail;
    private TextView no2Detail;
    private TextView so2Detail;
    private TextView coDetail;
    private TextView o3Detail;
    private ImageView chahao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cityDetail = (TextView) findViewById(R.id.city_detail);
        dateDetail = (TextView) findViewById(R.id.date_detail);
        aqiDetail = (TextView) findViewById(R.id.aqi_detail);
        qltyDetail = (TextView) findViewById(R.id.qlty_detail);
        pm25Detail = (TextView) findViewById(R.id.pm25_detail);
        pm10Detail = (TextView) findViewById(R.id.pm10_detail);
        no2Detail = (TextView) findViewById(R.id.no2_detail);
        so2Detail = (TextView) findViewById(R.id.so2_detail);
        coDetail = (TextView) findViewById(R.id.co_detail);
        o3Detail = (TextView) findViewById(R.id.o3_detail);
        chahao = (ImageView) findViewById(R.id.chahao);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        cityDetail.setText(bundle.getString("city"));
        dateDetail.setText(bundle.getString("date")+ "发布");
        aqiDetail.setText(bundle.getString("aqi1"));
        qltyDetail.setText(bundle.getString("qlty"));
        pm25Detail.setText(bundle.getString("pm25"));
        pm10Detail.setText(bundle.getString("pm10"));
        no2Detail.setText(bundle.getString("no2"));
        so2Detail.setText(bundle.getString("so2"));
        coDetail.setText(bundle.getString("co"));
        o3Detail.setText(bundle.getString("o3"));

        chahao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
