package com.reminder.liuyang.reminder.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;
import java.util.Set;

/**
 * Created by jason on 5/17/13.
 * 存储简单数据的功能类
 * 第一个参数都是key,第二个参数是value(set)或者default Value(get)
 * 第一个参数有两种形式，一种是直接传递String进来，另外一种是将key值定义在String.xml中，然后传递int型的id进来。
 */
public class SharedPrefsUtil {
    private static SharedPrefsUtil singleton;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    /**
     * 构造函数
     *
     * @param context    context对象
     * @param spFileName SharedPreferences的文件名
     */
    public SharedPrefsUtil(Context context, String spFileName) {
        mContext = context;
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    /**
     * 存int值
     *
     * @param key
     * @param value
     */
    public void setIntSP(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * 存int值
     *
     * @param key
     * @param value
     */
    public void setIntSP(int key, int value) {
        mEditor.putInt(mContext.getResources().getString(key), value);
        mEditor.commit();
    }

    /**
     * 取int值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getIntSP(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 取int值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getIntSP(int key, int defaultValue) {
        return mSharedPreferences.getInt(mContext.getResources().getString(key), defaultValue);
    }




    /**
     * 存string值
     *
     * @param key
     * @param value
     */
    public void setStringSP(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * 追加string值
     *
     * @param key
     * @param value
     * @param divider
     */
    public void appendStringSP(String key, String value, String divider) {
        value = getStringSP(key, "") + value + divider;
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * 存string值
     *
     * @param key
     * @param value
     */
    public void setStringSP(int key, String value) {
        mEditor.putString(mContext.getResources().getString(key), value);
        mEditor.commit();
    }

    /**
     * 取string值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringSP(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * 取string值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringSP(int key, String defaultValue) {
        return mSharedPreferences.getString(mContext.getResources().getString(key), defaultValue);
    }


    /**
     * 存boolean值
     *
     * @param key
     * @param value
     */
    public void setBooleanSP(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * 存boolean值
     *
     * @param key
     * @param value
     */
    public void setBooleanSP(int key, boolean value) {
        mEditor.putBoolean(mContext.getResources().getString(key), value);
        mEditor.commit();
    }

    /**
     * 删除boolean值
     *
     * @param key
     */
    public void deleteBooleanSP(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    /**
     * 取boolean值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBooleanSP(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 取boolean值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBooleanSP(int key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(mContext.getResources().getString(key), defaultValue);
    }


    //对Float值的处理

    /**
     * 存float值
     *
     * @param key
     * @param value
     */
    public void setFloatSP(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    /**
     * 存float值
     *
     * @param key
     * @param value
     */
    public void setFloatSP(int key, float value) {
        mEditor.putFloat(mContext.getResources().getString(key), value);
        mEditor.commit();
    }

    /**
     * 取float值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloatSP(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * 取float值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloatSP(int key, float defaultValue) {
        return mSharedPreferences.getFloat(mContext.getResources().getString(key), defaultValue);
    }


    //对Long值的处理

    /**
     * 存long值
     *
     * @param key
     * @param value
     */
    public void setLongSP(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * 存long值
     *
     * @param key
     * @param value
     */
    public void setLongSP(int key, long value) {
        mEditor.putLong(mContext.getResources().getString(key), value);
        mEditor.commit();
    }

    /**
     * 取long值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLongSP(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 取long值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLongSP(int key, long defaultValue) {
        return mSharedPreferences.getLong(mContext.getResources().getString(key), defaultValue);
    }

    /** 删除long值
    *
    * @param key
    */
    public void deleteLongSP(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    /**
     * 清空所有数据
     * @return
     */
    public boolean clearAll() {
        mEditor.clear();
        return mEditor.commit();
    }


    //对Set<String>的处理

    /**
     * 存Set<String>值，只适用于sdk>=11
     *
     * @param key
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setStringSetSP(String key, Set<String> value) {
        mEditor.putStringSet(key, value);
        mEditor.commit();
    }

    /**
     * 存Set<String>值，只适用于sdk>=11
     *
     * @param key
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setStringSetSP(int key, Set<String> value) {
        mEditor.putStringSet(mContext.getResources().getString(key), value);
        mEditor.commit();
    }

    /**
     * 取Set<String>值，只适用于sdk>=11
     *
     * @param key
     * @param defaultValue
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSetSP(String key, Set<String> defaultValue) {
        return mSharedPreferences.getStringSet(key, defaultValue);
    }
//
//    /**
//     * 取Set<String>值，只适用于sdk>=11
//     *
//     * @param key
//     * @param defaultValue
//     * @return
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public Set<String> getStringSetSP(int key, Set<String> defaultValue) {
//        return mSharedPreferences.getStringSet(mContext.getResources().getString(key), defaultValue);
//    }
//
//    public static void saveStatID(Context context, String id) {
//        if (context == null)
//            return;
//        SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil(context, GlobalConstant.SHARE_PREFS_NTBASE);
//        sharedPrefsUtil.setStringSP(GlobalConstant.SHARE_PREFS_STATID, id);
//    }
//
//    public static String getStatID(Context context) {
//        if (context == null)
//            return null;
//        SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil(context, GlobalConstant.SHARE_PREFS_NTBASE);
//        return sharedPrefsUtil.getStringSP(GlobalConstant.SHARE_PREFS_STATID, null);
//    }
//
//    public static void saveTheme(Context context, int id) {
//        if (context == null)
//            return;
//        SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil(context, GlobalConstant.SHARE_PREFS_NTBASE);
//        sharedPrefsUtil.setIntSP(GlobalConstant.SHARE_PREFS_THEME, id);
//    }
//
//    public static int getTheme(Context context) {
//        if (context == null)
//            return -1;
//        SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil(context, GlobalConstant
//                .SHARE_PREFS_NTBASE);
//        return sharedPrefsUtil.getIntSP(GlobalConstant.SHARE_PREFS_THEME, -1);
//    }

}
