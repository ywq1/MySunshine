package com.yuwanqing.mysunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    private Spinner spinner;
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner.getSelectedItem();
        spinner1.getSelectedItem();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();
                if(result.equals("请选择")){
                }
                if(result.equals("摄氏度℃")){
                    CityBase.temp_unit = result;
                }
                if(result.equals("华氏度℉")){
                    CityBase.temp_unit = result;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();
                if(result.equals("请选择")){
                }
                if(result.equals("默认背景")){
                    SettingParame.flag0 = 1;
                }
                if(result.equals("实景")){
                    SettingParame.flag0 = 2;
                }
                if(result.equals("卡通")){
                    SettingParame.flag0 = 3;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
