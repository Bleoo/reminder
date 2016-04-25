package com.reminder.liuyang.reminder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.reminder.liuyang.reminder.LeoApplication;
import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.SystemUtils;
import com.reminder.liuyang.reminder.view.CheckPasswordDialog;
import com.reminder.liuyang.reminder.view.EncryptPasswordView;

/**
 * Created by Administrator on 2016/4/20.
 */
public class DecryptPasswordActivity extends Activity implements View.OnClickListener {

    private EncryptPasswordView passwordView;
    private Button btn_forget_password;
    private CheckPasswordDialog checkPasswordDialog;

    private String currentPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_password);

        passwordView = (EncryptPasswordView) findViewById(R.id.view_password);
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);

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
        btn_forget_password.setOnClickListener(this);

        findViewById(R.id.rl_title_bar).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tv_password_tip)).setText(getString(R.string.please_enter_password));
        btn_forget_password.setText(getString(R.string.forget_password));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.btn_forget_password:
                showCheckPasswordDialog();
                break;
        }
    }

    private void append(String tag) {
        currentPassword += tag;
        passwordView.refreshViewByLength(currentPassword.length());
        if (currentPassword.length() == 4) {
            if (currentPassword.equals(LeoApplication.getInstance().encryptPassword)) {
                finish();
            } else {
                currentPassword = "";
                passwordView.refreshViewByLength(0);
                SystemUtils.showToast(this, getString(R.string.password_error));
            }
        }
    }

    private void backspace() {
        if (currentPassword.length() > 0) {
            currentPassword = currentPassword.substring(0, currentPassword.length() - 1);
            passwordView.refreshViewByLength(currentPassword.length());
        }
    }

    private void showCheckPasswordDialog() {
        if (checkPasswordDialog == null) {
            checkPasswordDialog = new CheckPasswordDialog(this);
        }
        checkPasswordDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkPasswordDialog != null) {
            checkPasswordDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
