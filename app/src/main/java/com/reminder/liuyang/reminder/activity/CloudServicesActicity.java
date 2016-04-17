package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.bean.Remind;
import com.reminder.liuyang.reminder.utils.DBUtils;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/4/8.
 */
public class CloudServicesActicity extends BaseActivity implements View.OnClickListener{

    private View sv_un_sign_in;
    private View sv_sign_in;
    private TextView tv_phone_number;
    private TextView tv_cloud_remind_number;
    private TextView tv_local_remind_number;
    private View rl_cloud_to_local;
    private View rl_local_to_cloud;

    private DBUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_cloud_services);

        sv_un_sign_in = findViewById(R.id.sv_un_sign_in);
        sv_sign_in = findViewById(R.id.sv_sign_in);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        tv_cloud_remind_number = (TextView) findViewById(R.id.tv_cloud_remind_number);
        tv_local_remind_number = (TextView) findViewById(R.id.tv_local_remind_number);
        rl_cloud_to_local = findViewById(R.id.rl_cloud_to_local);
        rl_local_to_cloud = findViewById(R.id.rl_local_to_cloud);

        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.tv_sign_up).setOnClickListener(this);
        findViewById(R.id.tv_sign_in).setOnClickListener(this);
        findViewById(R.id.tv_sign_out).setOnClickListener(this);
        rl_cloud_to_local.setOnClickListener(this);
        rl_local_to_cloud.setOnClickListener(this);

        showViewByStatus();

        dbUtils = new DBUtils(this);
    }

    private void showViewByStatus() {
        BmobUser currentUser = BmobUser.getCurrentUser(this);
        if(currentUser == null){
            sv_un_sign_in.setVisibility(View.VISIBLE);
            sv_sign_in.setVisibility(View.GONE);
        }else{
            sv_un_sign_in.setVisibility(View.GONE);
            sv_sign_in.setVisibility(View.VISIBLE);
            tv_phone_number.setText(currentUser.getMobilePhoneNumber());
            tv_cloud_remind_number.setText("1");
            tv_local_remind_number.setText("1");
        }
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
                intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sign_out:
                signOut();
                break;
            case R.id.rl_cloud_to_local:
                cloudToLocal();
                break;
            case R.id.rl_local_to_cloud:
                localToCloud();
                break;
        }
    }

    private void signOut(){
        BmobUser.logOut(this);   //清除缓存用户对象
        showViewByStatus();
    }

    private void cloudToLocal(){
        BmobUser user = BmobUser.getCurrentUser(this);
        BmobQuery<Remind> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.findObjects(this, new FindListener<Remind>() {
            @Override
            public void onSuccess(List<Remind> list) {
                if(dbUtils.batchSave(list)){
                    SystemUtils.showToast(mContext, "成功");
                }
            }

            @Override
            public void onError(int i, String s) {
                SystemUtils.showToast(mContext, "失败");
            }
        });
    }

    private void localToCloud(){
        BmobUser user = BmobUser.getCurrentUser(this);
        List<Remind> remindList = dbUtils.query();
        List<BmobObject> remindTempData = new ArrayList<>();
        if(remindList != null && remindList.size() > 0){
            for(Remind remind : remindList){
                remind.user = user;
                remindTempData.add(remind);
            }
            new BmobObject().insertBatch(this, remindTempData, new SaveListener() {
                @Override
                public void onSuccess() {
                    SystemUtils.showToast(mContext, "成功");
                }
                @Override
                public void onFailure(int code, String msg) {

                }
            });
        } else {
            SystemUtils.showToast(mContext, "本地污数据");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showViewByStatus();
    }
}
