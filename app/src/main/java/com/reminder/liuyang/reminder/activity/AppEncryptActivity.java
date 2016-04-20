package com.reminder.liuyang.reminder.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reminder.liuyang.reminder.LeoApplication;
import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.Constant;

/**
 * Created by Administrator on 2016/4/19.
 */
public class AppEncryptActivity extends Activity implements View.OnClickListener {

    private TextView tv_enable_password;
    private TextView tv_change_password;
    private View rl_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_encrypt);

        tv_enable_password = (TextView) findViewById(R.id.tv_enable_password);
        tv_change_password = (TextView) findViewById(R.id.tv_change_password);
        rl_change_password = findViewById(R.id.rl_change_password);

        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.rl_enable_password).setOnClickListener(this);
        rl_change_password.setOnClickListener(this);

        setViewByEncryptStatus();
    }

    private void setViewByEncryptStatus() {
        if(LeoApplication.getInstance().encryptEnable){
            tv_enable_password.setText(getString(R.string.disable_password));
            rl_change_password.setClickable(true);
            tv_change_password.setTextColor(Color.BLACK);
        } else {
            tv_enable_password.setText(getString(R.string.enable_password));
            rl_change_password.setClickable(false);
            tv_change_password.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_enable_password:
                intent = new Intent(this, EncryptPasswordActivity.class);
                if(LeoApplication.getInstance().encryptEnable) {
                    intent.putExtra("intent", EncryptPasswordActivity.CHANGE_PASSWORD);
                } else {
                    intent.putExtra("intent", EncryptPasswordActivity.ENABLE_PASSWORD);
                }
                startActivity(intent);
                break;
            case R.id.rl_change_password:
                intent = new Intent(this, EncryptPasswordActivity.class);
                intent.putExtra("intent", EncryptPasswordActivity.DISABLE_PASSWORD);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewByEncryptStatus();
    }
}
