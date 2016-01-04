package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_user;        //用户名
    private EditText et_password;    //密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView actionbar_title = (TextView) findViewById(R.id.actionbar_title);
        actionbar_title.setText("登录");

        et_user = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.tv_reset_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case  R.id.back:
                finish();
                break;
            case R.id.btn_submit:
                if(checkForm()) {
                    //showInnerDialog("正在登录...", Inner_RefreshDialog.LOADING);
                    queryUser();
                }
                break;
            case R.id.tv_reset_password:
                //Intent intent = new Intent(getActivity(), ResetInputIdCardActivity.class);
                //startActivity(intent);
                break;
        }
    }

    private boolean checkForm() {
        String user = et_user.getText().toString();
        if (TextUtils.isEmpty(user)) {
            SystemUtils.showToast(mContext, "请输入邮箱/手机号");
            return false;
        }

        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            SystemUtils.showToast(mContext, "请输入密码");
            return false;
        }

        return true;
    }

    private void login(String username){
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(et_password.getText().toString().trim());
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                SystemUtils.showToast(mContext, "登录成功");
                jumpToActivity(MainActivity.class);
            }

            @Override
            public void onFailure(int code, String msg) {
                SystemUtils.showToast(mContext, "账号/密码错误" + msg);
            }
        });
    }

    private void queryUser(){
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("email", et_user.getText().toString().trim());
        query.findObjects(this, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> object) {
                login(object.get(0).getUsername());
            }
            @Override
            public void onError(int code, String msg) {
                SystemUtils.showToast(mContext, "邮箱未注册:" + msg);
            }
        });
    }

}
