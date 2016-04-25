package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.Constant;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private final String passwordReg = "^[0-9A-Za-z]{6,16}$";

    private EditText et_password;
    private EditText et_confirm_password;
    private View tv_done;
    private View iv_password_error;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        if (getIntent().hasExtra("phone_number")) {
            phoneNumber = getIntent().getStringExtra("phone_number");
        } else {
            finish();
            return;
        }

        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        tv_done = findViewById(R.id.tv_done);
        iv_password_error = findViewById(R.id.iv_password_error);

        findViewById(R.id.rl_back).setOnClickListener(this);
        tv_done.setOnClickListener(this);

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iv_password_error.setVisibility(View.INVISIBLE);
                } else {
                    String password = et_password.getText().toString();
                    if (password.matches(passwordReg)) {
                        iv_password_error.setVisibility(View.INVISIBLE);
                        setDoneBtnClickable(true);
                    } else {
                        iv_password_error.setVisibility(View.VISIBLE);
                        setDoneBtnClickable(false);
                        SystemUtils.showToast(mContext, getString(R.string.password_reg_tip));
                    }
                }
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = et_password.getText().toString();
                if (password.matches(passwordReg)) {
                    setDoneBtnClickable(true);
                } else {
                    setDoneBtnClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setDoneBtnClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_done:
                if (checkConfirmPassword()) {
                    signUp();
                } else {
                    SystemUtils.showToast(mContext, getString(R.string.password_diff_tip));
                }
                break;
        }
    }

    private void setDoneBtnClickable(boolean clickable) {
        tv_done.setClickable(clickable);
        tv_done.setAlpha(clickable ? 1 : 0.5f);
    }

    private boolean checkConfirmPassword() {
        return et_password.getText().toString().equals(et_confirm_password.getText().toString());
    }

    private void signUp() {
        String password = et_password.getText().toString();
        BmobUser bu = new BmobUser();
        bu.setUsername(phoneNumber);
        bu.setMobilePhoneNumber(phoneNumber);
        bu.setMobilePhoneNumberVerified(true);
        bu.setPassword(SystemUtils.getPasswordMD5(password));
        //注意：不能用save方法进行注册
        bu.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                SystemUtils.showToast(mContext, getString(R.string.sign_up_success));
                signIn();
            }

            @Override
            public void onFailure(int code, String msg) {
                SystemUtils.showToast(mContext, getString(R.string.sign_up_fail));
            }
        });
    }

    private void signIn() {
        final String password = et_password.getText().toString();
        final String passwordMD5 = SystemUtils.getPasswordMD5(password);
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(phoneNumber);
        bu2.setPassword(passwordMD5);
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                savePassword(passwordMD5);
                Intent intent = new Intent(mContext, CloudServicesActicity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int code, String msg) {
                Intent intent = new Intent(mContext, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void savePassword(String passwordMD5) {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.SP_PASSWORD, passwordMD5);
        editor.apply();
    }
}
