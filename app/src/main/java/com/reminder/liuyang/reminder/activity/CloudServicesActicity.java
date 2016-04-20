package com.reminder.liuyang.reminder.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.bean.Remind;
import com.reminder.liuyang.reminder.utils.DBUtils;
import com.reminder.liuyang.reminder.utils.SystemUtils;
import com.reminder.liuyang.reminder.view.LoadingProgressDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/4/8.
 */
public class CloudServicesActicity extends BaseActivity implements View.OnClickListener{

    private final int COUNT_CLOUD = 1;
    private final int COUNT_LOCAL = 2;
    private final int COUNT_CLOUD_FAIL = 3;
    private final int COUNT_LOCAL_FAIL = 4;
    private final int SUCCESS_CLOUD_TO_LOCAL = 5;
    private final int FAIL_CLOUD_TO_LOCAL = 6;

    private View sv_un_sign_in;
    private View sv_sign_in;
    private TextView tv_phone_number;
    private TextView tv_cloud_remind_number;
    private TextView tv_local_remind_number;
    private Dialog dialog;

    private DBUtils dbUtils;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_cloud_services);

        sv_un_sign_in = findViewById(R.id.sv_un_sign_in);
        sv_sign_in = findViewById(R.id.sv_sign_in);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        tv_cloud_remind_number = (TextView) findViewById(R.id.tv_cloud_remind_number);
        tv_local_remind_number = (TextView) findViewById(R.id.tv_local_remind_number);

        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.tv_sign_up).setOnClickListener(this);
        findViewById(R.id.tv_sign_in).setOnClickListener(this);
        findViewById(R.id.tv_sign_out).setOnClickListener(this);
        findViewById(R.id.rl_cloud_to_local).setOnClickListener(this);
        findViewById(R.id.rl_local_to_cloud).setOnClickListener(this);

        dbUtils = new DBUtils(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case COUNT_CLOUD:
                        tv_cloud_remind_number.setText((int)msg.obj+"");
                        break;
                    case COUNT_LOCAL:
                        tv_local_remind_number.setText((long)msg.obj+"");
                        break;
                    case COUNT_CLOUD_FAIL:
                        tv_cloud_remind_number.setText(getString(R.string.load_fail));
                        break;
                    case COUNT_LOCAL_FAIL:
                        tv_local_remind_number.setText(getString(R.string.load_fail));
                        break;
                    case SUCCESS_CLOUD_TO_LOCAL:
                        dialog.dismiss();
                        SystemUtils.showToast(mContext, getString(R.string.sync_success));
                        break;
                    case FAIL_CLOUD_TO_LOCAL:
                        dialog.dismiss();
                        SystemUtils.showToast(mContext, getString(R.string.sync_fail));
                        break;
                }
            }
        };

        showViewByStatus();
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
            tv_cloud_remind_number.setText(getString(R.string.loading));
            tv_local_remind_number.setText(getString(R.string.loading));
            new Thread() {
                @Override
                public void run() {
                    cloudDataCount();
                    localDataCount();
                }
            }.start();
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
                showProgressDialog();
                handler.postDelayed(new Runnable() { // 延时1秒操作
                    @Override
                    public void run() {
                        cloudToLocal();
                    }
                }, 1000);
                break;
            case R.id.rl_local_to_cloud:
                showProgressDialog();
                handler.postDelayed(new Runnable() { // 延时1秒操作
                    @Override
                    public void run() {
                        localToCloud();
                    }
                }, 1000);
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
        query.addWhereEqualTo("user", user); // 标记所属用户
        query.order("-writeTime"); // 按创建时间倒序
        query.findObjects(this, new FindListener<Remind>() {
            @Override
            public void onSuccess(final List<Remind> list) {
                new Thread() {
                    @Override
                    public void run() {
                        if (dbUtils.batchDelete() && dbUtils.batchSave(list)) {
                            Message.obtain(handler, SUCCESS_CLOUD_TO_LOCAL).sendToTarget();
                        } else {
                            Message.obtain(handler, FAIL_CLOUD_TO_LOCAL).sendToTarget();
                        }
                    }
                }.start();
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                SystemUtils.showToast(mContext, getString(R.string.sync_fail));
            }
        });
    }

    private void localToCloud(){
        long localCount = dbUtils.getCount();
        if(localCount > 0){
            cloudSelectAll();
        } else {
            SystemUtils.showToast(mContext, getString(R.string.local_no_data));
        }
    }

    /**
     * 查询云端所有该用户的数据
     */
    private void cloudSelectAll(){
        BmobUser user = BmobUser.getCurrentUser(this);
        BmobQuery<Remind> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user); // 查询该用户所有数据
        query.findObjects(this, new FindListener<Remind>() {
            @Override
            public void onSuccess(List<Remind> list) {
                cloudDeleteAll(list);
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                SystemUtils.showToast(mContext, getString(R.string.sync_fail));
            }
        });
    }

    /**
     * 删除云端所有该用户的数据
     * @param list
     */
    private void cloudDeleteAll(List<Remind> list){
        if(list != null && list.size() > 0){
            List<BmobObject> remindTempData = new ArrayList<>();
            for(Remind remind : list){
                remindTempData.add(remind);
            }
            new BmobObject().deleteBatch(this, remindTempData, new DeleteListener() {
                @Override
                public void onSuccess() {
                    cloudSaveBatch();
                }
                @Override
                public void onFailure(int code, String msg) {
                    dialog.dismiss();
                    SystemUtils.showToast(mContext, getString(R.string.sync_fail));
                }
            });
        }
    }

    /**
     * 将本地数据上传保存
     */
    private void cloudSaveBatch() {
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
                    dialog.dismiss();
                    SystemUtils.showToast(mContext, getString(R.string.sync_success));
                    cloudDataCount();
                }
                @Override
                public void onFailure(int code, String msg) {
                    dialog.dismiss();
                    SystemUtils.showToast(mContext, getString(R.string.sync_fail));
                }
            });
        } else {
            SystemUtils.showToast(mContext, getString(R.string.local_no_data));
        }
    }

    private void cloudDataCount(){
        BmobUser user = BmobUser.getCurrentUser(this);
        BmobQuery<Remind> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.count(this, Remind.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                Message.obtain(handler , COUNT_CLOUD, count).sendToTarget();
            }

            @Override
            public void onFailure(int code, String msg) {
                Message.obtain(handler , COUNT_CLOUD_FAIL).sendToTarget();
            }
        });
    }

    private void localDataCount(){
        try {
            Message.obtain(handler, COUNT_LOCAL, dbUtils.getCount()).sendToTarget();
        } catch (Exception e){
            Message.obtain(handler, COUNT_LOCAL_FAIL).sendToTarget();
        }
    }

    private void showProgressDialog(){
        if(dialog == null) {
            dialog = new LoadingProgressDialog(mContext, getString(R.string.sync_ing));
        }
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showViewByStatus();
    }
}
