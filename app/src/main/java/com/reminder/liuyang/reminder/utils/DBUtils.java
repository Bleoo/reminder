package com.reminder.liuyang.reminder.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.reminder.liuyang.reminder.bean.Remind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/25.
 */
public class DBUtils {

    public final static String TB_REMAINDER = "reminder";
    public final static String COL_ID = "id";
    public final static String COL_CONTENT = "content";
    public final static String COL_WRITETIME = "write_time";

    private DBHelper dbHelper;

    public DBUtils(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long save(Remind remind) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cValue = new ContentValues();
        cValue.put(COL_CONTENT, remind.content);
        cValue.put(COL_WRITETIME, remind.writeTime);
        return database.insert(TB_REMAINDER, null, cValue);
    }

    public boolean batchSave(List<Remind> list) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            for (Remind remind : list) {
                ContentValues cValue = new ContentValues();
                cValue.put(COL_CONTENT, remind.content);
                cValue.put(COL_WRITETIME, remind.writeTime);
                database.insert(TB_REMAINDER, null, cValue);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            database.endTransaction();
            return true;
        }
    }

    public int delete(Remind remind) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String whereClause = COL_ID + "=?";
        String[] whereArgs = {String.valueOf(remind.id)};
        return database.delete(TB_REMAINDER, whereClause, whereArgs);
    }

    public boolean batchDelete() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            database.execSQL("delete from " + TB_REMAINDER + ";"); //清空数据
            database.execSQL("update sqlite_sequence SET seq = 0 where name ='" + TB_REMAINDER + "';"); //自增长ID为0
            database.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            database.endTransaction();
            return true;
        }
    }

    public int update(Remind remind) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues cValue = new ContentValues();
        cValue.put(COL_CONTENT, remind.content);
        cValue.put(COL_WRITETIME, remind.writeTime);
        String whereClause = COL_ID + "=?";
        String[] whereArgs = {String.valueOf(remind.id)};
        return database.update(TB_REMAINDER, cValue, whereClause, whereArgs);
    }

    public List<Remind> query() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(TB_REMAINDER, null, null, null, null, null, COL_ID + " desc");
        List<Remind> remindList = null;
        if (cursor != null) {
            remindList = new ArrayList<>();
            Remind remind;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                remind = new Remind();
                remind.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                remind.content = cursor.getString(cursor.getColumnIndex(COL_CONTENT));
                remind.writeTime = cursor.getLong(cursor.getColumnIndex(COL_WRITETIME));
                remindList.add(remind);
            }
        }
        return remindList;
    }

    public List<Remind> search(String searchText) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(TB_REMAINDER, null, COL_CONTENT + " like '%" + searchText + "%'", null, null, null, COL_ID + " desc");
        List<Remind> searchList = null;
        if (cursor != null) {
            searchList = new ArrayList<>();
            Remind remind;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                remind = new Remind();
                remind.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                remind.content = cursor.getString(cursor.getColumnIndex(COL_CONTENT));
                remind.writeTime = cursor.getLong(cursor.getColumnIndex(COL_WRITETIME));
                searchList.add(remind);
            }
        }
        return searchList;
    }

    public long getCount() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("select count(*) from " + TB_REMAINDER + ";", null);
        cursor.moveToFirst();
        return cursor.getLong(0);
    }

}
