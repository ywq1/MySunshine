package com.yuwanqing.mysunshine;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView share;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        share = (TextView) findViewById(R.id.share_text);
        Intent intent2 = getIntent();
        data = intent2.getStringExtra("weather_text");
        share.setText(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
        return true;
    }
    private  Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, data + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
}
