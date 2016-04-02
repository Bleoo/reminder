package com.reminder.liuyang.reminder;

import android.app.Application;

import com.reminder.liuyang.reminder.utils.Constant;

import cn.bmob.v3.Bmob;

/**
 * Created by liuyang on 15/12/31.
 */
public class LeoApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static LeoApplication mInstance = null;

    public static LeoApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Bmob.initialize(this, Constant.BMOB_APPID);

        //mSharedPrefsUtil = new SharedPrefsUtil(this, Constant.SharedPrefrence.SHARED_NAME);

        //设置Thread Exception Handler
        //Thread.setDefaultUncaughtExceptionHandler(this);

    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.exit(0);
    }
}
