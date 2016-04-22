package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by Administrator on 2016/4/2.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tv_username = (TextView) findViewById(R.id.tv_username);

        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.rl_cloud_services).setOnClickListener(this);
        findViewById(R.id.rl_app_encrypt).setOnClickListener(this);
        findViewById(R.id.rl_share_app).setOnClickListener(this);
        findViewById(R.id.rl_check_update).setOnClickListener(this);

        ((TextView) findViewById(R.id.tv_version)).setText("V" + SystemUtils.getVersionName(this));
        setUsernameText();
    }

    private void setUsernameText() {
        BmobUser currentUser = BmobUser.getCurrentUser(this);
        if(currentUser != null){
            tv_username.setText(currentUser.getMobilePhoneNumber());
        } else {
            tv_username.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.rl_cloud_services:
                intent = new Intent(this, CloudServicesActicity.class);
                startActivity(intent);
                break;
            case R.id.rl_app_encrypt:
                intent = new Intent(this, AppEncryptActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_share_app:
                shareApp();
                break;
            case R.id.rl_check_update:
                //初始化建表操作，一旦AppVersion表在后台创建成功，建议屏蔽或删除此方法，否则会生成多行记录。
                //BmobUpdateAgent.initAppVersion(this);
                BmobUpdateAgent.forceUpdate(mContext);
                break;
            case R.id.rl_back:
                finish();
                break;
        }
    }

    private void shareApp() {
        Intent shareIntent = new Intent();
        shareIntent.setType("text/plain");
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_tip));
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUsernameText();
    }
}
