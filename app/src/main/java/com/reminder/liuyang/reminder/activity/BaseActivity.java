package com.reminder.liuyang.reminder.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.reminder.liuyang.reminder.LeoApplication;
import com.reminder.liuyang.reminder.view.LoadingProgressDialog;

/**
 * Created by liuyang on 15/12/29.
 */
public class BaseActivity extends Activity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onResume() {
        if(!LeoApplication.getInstance().isForegrund){
            startActivity(new Intent(this, AppEncryptActivity.class));
        }
        super.onResume();
    }

    /**
     * activity间无返回跳转
     *
     * @param cls
     */
    public void jumpToActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

}
