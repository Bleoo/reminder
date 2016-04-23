package com.reminder.liuyang.reminder;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.reminder.liuyang.reminder.utils.Constant;

import cn.bmob.v3.Bmob;

/**
 * Created by liuyang on 15/12/31.
 */
public class LeoApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static LeoApplication mInstance = null;
    private SharedPreferences sharedPreferences;

    private int activityCount;
    public boolean isForegrund;
    public boolean encryptEnable;
    public String encryptPassword = "";

    public static LeoApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Bmob.initialize(this, Constant.BMOB_APPID);

        AppStatusTracker appStatusTracker = new AppStatusTracker();
        registerActivityLifecycleCallbacks(appStatusTracker); // 注册生命周期回调(判断应用是否置于前台)

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        getEncryptInfo();

        //设置Thread Exception Handler
        //Thread.setDefaultUncaughtExceptionHandler(this);

    }

    public void getEncryptInfo() {
        encryptEnable = sharedPreferences.getBoolean(Constant.ENCRYPT_ENABLE, false);
        if (encryptEnable) {
            encryptPassword = sharedPreferences.getString(Constant.ENCRYPT_PASSWORD, encryptPassword);
        }
    }

    public void setEncryptInfo(boolean enable, String password) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(Constant.ENCRYPT_ENABLE, enable);
        if (enable) {
            edit.putString(Constant.ENCRYPT_PASSWORD, password);
        }
        edit.commit();
        getEncryptInfo();
    }

    private class AppStatusTracker implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            activityCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            isForegrund = true;
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityCount--;
            if (activityCount == 0) {
                isForegrund = false;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.exit(0);
    }
}
