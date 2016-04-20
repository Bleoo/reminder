package com.reminder.liuyang.reminder.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reminder.liuyang.reminder.LeoApplication;
import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.SystemUtils;

/**
 * Created by Administrator on 2016/4/20.
 */
public class EncryptPasswordActivity extends BaseActivity implements View.OnClickListener {

    public static final int ENABLE_PASSWORD = 1;
    public static final int CHANGE_PASSWORD = 2;
    public static final int DISABLE_PASSWORD = 3;

    private TextView tv_title;
    private TextView tv_password_tip;

    private String password = "";
    private String confirmPassword = "";
    private String currentPassword = "";
    private int intentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_password);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_password_tip = (TextView) findViewById(R.id.tv_password_tip);

        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_del).setOnClickListener(this);

        if(getIntent().hasExtra("intent")){
            intentMode = getIntent().getIntExtra("intent", 0);
        }
        switch (intentMode){
            case ENABLE_PASSWORD:
                tv_title.setText(getString(R.string.set_password));
                tv_password_tip.setText(getString(R.string.set_password));
                break;
            case CHANGE_PASSWORD:
            case DISABLE_PASSWORD:
                tv_title.setText(getString(R.string.input_current_password));
                tv_password_tip.setText(getString(R.string.current_password));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_0:
                append((String) (v.getTag()));
                break;
            case R.id.btn_del:
                backspace();
                break;
            case R.id.rl_back:
                finish();
                break;
        }
    }

    private void append(String tag){
        switch (intentMode){
            case ENABLE_PASSWORD:
                if(password.length() == 4){
                    confirmPassword += tag;
                    SystemUtils.showToast(mContext, "confirmPassword" + confirmPassword);
                    if(confirmPassword.length() == 4){
                        if(confirmPassword.equals(password)) {
                            LeoApplication.getInstance().setEncryptInfo(true, confirmPassword);
                            SystemUtils.showToast(mContext, "设置成功");
                            finish();
                            return;
                        } else {
                            confirmPassword = "";
                            SystemUtils.showToast(mContext, "密码不匹配");
                        }
                    }
                } else {
                    password += tag;
                    if(password.length() == 4){
                        tv_password_tip.setText(getString(R.string.confirm_password));
                    }
                    SystemUtils.showToast(mContext, "password" + password);
                }
                break;
            case CHANGE_PASSWORD:

                break;
        }
    }

    private void backspace(){
        switch (intentMode) {
            case ENABLE_PASSWORD:
                if (password.length() == 4) {
                    if (confirmPassword.length() > 0) {
                        confirmPassword = confirmPassword.substring(0, confirmPassword.length() - 1);
                        SystemUtils.showToast(mContext, "confirmPassword" + confirmPassword);
                    }
                } else if (password.length() > 0) {
                    password = password.substring(0, password.length() - 1);
                    SystemUtils.showToast(mContext, "password" + password);
                }
                break;
        }
    }

}
