package com.reminder.liuyang.reminder.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/3/26.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static String dbName = "db_reminder";
    private static int version = 1;

    public DBHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + DBUtils.TB_REMAINDER+ "("
                + DBUtils.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBUtils.COL_CONTENT + " TEXT, "
                + DBUtils.COL_WRITETIME + " INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
