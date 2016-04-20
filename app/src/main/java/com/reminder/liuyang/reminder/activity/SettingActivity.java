package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.SystemUtils;

/**
 * Created by Administrator on 2016/4/2.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private View rl_cloud_sync;
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        rl_cloud_sync = findViewById(R.id.rl_cloud_sync);
        tv_version = (TextView) findViewById(R.id.tv_version);

        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.rl_cloud_services).setOnClickListener(this);
        rl_cloud_sync.setOnClickListener(this);
        findViewById(R.id.rl_app_encrypt).setOnClickListener(this);
        findViewById(R.id.rl_share_app).setOnClickListener(this);
        findViewById(R.id.rl_check_update).setOnClickListener(this);

        tv_version.setText("V" + SystemUtils.getVersionName(this));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.rl_cloud_services:
                intent = new Intent(this, CloudServicesActicity.class);
                startActivity(intent);
                break;
            case R.id.rl_cloud_sync:
                break;
            case R.id.rl_app_encrypt:
                intent = new Intent(this, AppEncryptActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_share_app:
                break;
            case R.id.rl_check_update:
                break;
            case R.id.rl_back:
                finish();
                break;
        }
    }
}
