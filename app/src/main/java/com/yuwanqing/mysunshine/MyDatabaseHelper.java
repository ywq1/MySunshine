package com.yuwanqing.mysunshine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by yuwanqing on 2017-03-31.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_id text, "
            + "city_name text, "
            + "city_weather text)";
    public static final String CREATE_CITY1 = "create table City_China ("
            + "id integer primary key autoincrement, "
            + "city_en text, "
            + "city_name text, "
            + "city_leader text, "
            + "city_id text, "
            + "city_name_country text)";
    public static final String CREATE_CITY2 = "create table ForeignCity ("
            + "id integer primary key autoincrement, "
            + "foreigncity_id text, "
            + "foreigncity_en text, "
            + "foreigncity_name text, "
            + "foreigncity_country_en text, "
            + "foreigncity_country text, "
            + "foreigncity_name_country text)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_CITY1);
        db.execSQL(CREATE_CITY2);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
