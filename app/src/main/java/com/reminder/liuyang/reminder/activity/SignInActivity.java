package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.BmobUtils;
import com.reminder.liuyang.reminder.utils.Constant;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/4/15.
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText et_phone_number;
    private EditText et_password;
    private View iv_sign_error;
    private View tv_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_sign_error = findViewById(R.id.iv_sign_error);
        tv_sign_in = findViewById(R.id.tv_sign_in);

        findViewById(R.id.rl_back).setOnClickListener(this);
        tv_sign_in.setOnClickListener(this);

        et_phone_number.setOnFocusChangeListener(this);

        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iv_sign_error.setVisibility(View.INVISIBLE);
                setSignInBtnClickable(checkPhoneNumber() && checkPassword());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSignInBtnClickable(checkPhoneNumber() && checkPassword());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setSignInBtnClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_sign_in:
                signIn();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && !checkPhoneNumber()) {
            iv_sign_error.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkPhoneNumber() {
        String tel = et_phone_number.getText().toString();
        return tel.matches(Constant.TEL_REG);
    }

    private boolean checkPassword() {
        int password = et_password.getText().length();
        return password > 5;
    }

    private void setSignInBtnClickable(boolean clickable) {
        tv_sign_in.setClickable(clickable);
        tv_sign_in.setAlpha(clickable ? 1 : 0.5f);
    }

    private void signIn() {
        final String password = et_password.getText().toString();
        final String passwordMD5 = SystemUtils.getPasswordMD5(password);
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(et_phone_number.getText().toString());
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
                String errorMsg = BmobUtils.getErrorMsg(code, mContext);
                if (errorMsg.equals(getString(R.string.error_default))) {
                    errorMsg = getString(R.string.sign_in_fail);
                }
                SystemUtils.showToast(mContext, errorMsg);
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
