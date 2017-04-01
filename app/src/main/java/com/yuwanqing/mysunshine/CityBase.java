package com.yuwanqing.mysunshine;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yuwanqing on 2017-03-31.
 */
public class CityBase {
    public static MyDatabaseHelper dbHelper;
    /*
    public void CreateBase(Context context) {
        dbHelper = new MyDatabaseHelper(context, "Cities.db", null, 1);
        dbHelper.getWritableDatabase();
    }
    public void addBase(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("city_name", name);
        values.clear();
    }
    public void deleteBase(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("City", "city_name = ?", new String[] { name });
    }
    public String[] queryBase() {
        String data[];
        data = new String[100];
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询Book表中的所有的数据
        Cursor cursor = db.query("City", null, null, null, null, null, null);
        int i=0;
        if(cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.getColumnIndex("city_name"));;
                data[i++]=name;
            }while(cursor.moveToNext());
        }
        cursor.close();
        return data;
    }
    */
}
