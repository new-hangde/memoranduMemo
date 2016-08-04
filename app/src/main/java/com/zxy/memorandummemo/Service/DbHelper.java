package com.zxy.memorandummemo.Service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

/**
 * Created by zxy on 2016/7/30.
 */
public class DbHelper extends SQLiteOpenHelper{
    SimpleCursorAdapter adapter;
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE  TABLE pic1 (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , content VARCHAR, date VARCHAR)";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
