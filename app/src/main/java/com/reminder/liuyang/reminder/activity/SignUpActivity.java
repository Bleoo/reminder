package com.reminder.liuyang.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.Constant;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

/**
 * Created by Administrator on 2016/4/9.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TEMPLATE_SIGN_UP = "SignUp";

    private EditText et_phone_number;
    private EditText et_phone_smscode;
    private TextView tv_get_smscode;
    private TextView tv_next;
    private View iv_sign_error;
    private View tv_smscode_tip;

    private CountDownTimer countDownTimer;
    private int countDownSecond = 60;
    private boolean isMsgSending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_phone_smscode = (EditText) findViewById(R.id.et_phone_smscode);
        tv_get_smscode = (TextView) findViewById(R.id.tv_get_smscode);
        tv_next = (TextView) findViewById(R.id.tv_next);
        iv_sign_error = findViewById(R.id.iv_sign_error);
        tv_smscode_tip = findViewById(R.id.tv_smscode_tip);

        findViewById(R.id.rl_back).setOnClickListener(this);
        tv_get_smscode.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        et_phone_number.setOnFocusChangeListener(this);

        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iv_sign_error.setVisibility(View.INVISIBLE);
                setSendBtnClickable(checkPhoneNumber() && !isMsgSending);
                setNextBtnClickable(checkPhoneNumber() && checkPhoneSMSCode());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_phone_smscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkPhoneSMSCode()) {
                    tv_get_smscode.setVisibility(View.GONE);
                    tv_next.setVisibility(View.VISIBLE);
                    setNextBtnClickable(checkPhoneNumber());
                } else {
                    tv_get_smscode.setVisibility(View.VISIBLE);
                    tv_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setSendBtnClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_get_smscode:
                sendSMSCode();
                break;
            case R.id.tv_next:
                verifySmsCode();
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

    private boolean checkPhoneSMSCode() {
        int smscode = et_phone_smscode.getText().length();
        return smscode > 5;
    }

    private void setSendBtnClickable(boolean clickable) {
        tv_get_smscode.setClickable(clickable);
        tv_get_smscode.setAlpha(clickable ? 1 : 0.5f);
    }

    private void setNextBtnClickable(boolean clickable) {
        tv_next.setClickable(clickable);
        tv_next.setAlpha(clickable ? 1 : 0.5f);
    }

    private void startCountdown() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(countDownSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv_get_smscode.setText(getString(R.string.resend_phone_smscode) + "(" + countDownSecond-- + ")");
                }

                @Override
                public void onFinish() {
                    tv_get_smscode.setText(getResources().getString(R.string.resend_phone_smscode));
                    countDownSecond = 60;
                    isMsgSending = false;
                    setSendBtnClickable(checkPhoneNumber());
                    tv_smscode_tip.setVisibility(View.INVISIBLE);
                }
            };
        }
        setSendBtnClickable(false);
        countDownTimer.start();
        isMsgSending = true;
        tv_smscode_tip.setVisibility(View.VISIBLE);
    }

    private void sendSMSCode() {
        BmobSMS.requestSMSCode(this, et_phone_number.getText().toString(), TEMPLATE_SIGN_UP, new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {
                    startCountdown();
                } else {
                    SystemUtils.showToast(mContext, "获取失败!请检查网络");
                }
            }
        });
    }

    private void verifySmsCode() {
        final String phoneNumber = et_phone_number.getText().toString();
        BmobSMS.verifySmsCode(this, phoneNumber, et_phone_smscode.getText().toString(), new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                if (ex == null) {//短信验证码已验证成功
                    Intent intent = new Intent(mContext, SetPasswordActivity.class);
                    intent.putExtra("phone_number", phoneNumber);
                    startActivity(intent);
                    finish();
                } else {
                    SystemUtils.showToast(mContext, "验证码错误");
                }
            }
        });
    }
}
