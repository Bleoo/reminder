package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.reminder.liuyang.reminder.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class CloudServicesActicity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_cloud_services);

        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.tv_sign_up).setOnClickListener(this);
        findViewById(R.id.tv_sign_in).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_sign_up:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sign_in:
                break;
        }
    }
}
