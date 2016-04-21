package com.reminder.liuyang.reminder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.reminder.liuyang.reminder.LeoApplication;

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
        LeoApplication.getInstance().getEncryptInfo();
        if(!LeoApplication.getInstance().isForegrund && LeoApplication.getInstance().encryptEnable){
            Intent intent = new Intent(this, DecryptPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
