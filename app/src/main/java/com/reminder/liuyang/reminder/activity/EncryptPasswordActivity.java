package com.reminder.liuyang.reminder.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reminder.liuyang.reminder.LeoApplication;
import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.utils.SystemUtils;
import com.reminder.liuyang.reminder.view.EncryptPasswordView;

/**
 * Created by Administrator on 2016/4/20.
 */
public class EncryptPasswordActivity extends BaseActivity implements View.OnClickListener {

    public static final int ENABLE_PASSWORD = 1;
    public static final int CHANGE_PASSWORD = 2;
    public static final int DISABLE_PASSWORD = 3;

    private final int EDITING_NEW_PASSWORD = 1;
    private final int EDITING_CONFIRM_PASSWORD = 2;
    private final int EDITING_CURRENT_PASSWORD = 3;

    private TextView tv_title;
    private TextView tv_password_tip;
    private EncryptPasswordView passwordView;

    private String newPassword = "";
    private String confirmPassword = "";
    private String currentPassword = "";
    private int intentMode;     // 意图模式
    private int editingStatus;  // 编辑状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_password);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_password_tip = (TextView) findViewById(R.id.tv_password_tip);
        passwordView = (EncryptPasswordView) findViewById(R.id.view_password);

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
                editingStatus = EDITING_NEW_PASSWORD;
                break;
            case CHANGE_PASSWORD:
            case DISABLE_PASSWORD:
                tv_title.setText(getString(R.string.input_current_password));
                tv_password_tip.setText(getString(R.string.current_password));
                editingStatus = EDITING_CURRENT_PASSWORD;
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

    /**
     * 根据不同编辑状态对相应的密码字符串进行追加
     * @param tag
     */
    private void append(String tag) {
        switch (editingStatus){
            case EDITING_NEW_PASSWORD:
                newPassword += tag;
                passwordView.refreshViewByLength(newPassword.length());
                if(newPassword.length() == 4){
                    tv_password_tip.setText(getString(R.string.confirm_password));
                    passwordView.refreshViewByLength(0);
                    editingStatus = EDITING_CONFIRM_PASSWORD;
                }
                break;
            case EDITING_CONFIRM_PASSWORD:
                confirmPassword += tag;
                passwordView.refreshViewByLength(confirmPassword.length());
                if(confirmPassword.length() == 4){
                    if(confirmPassword.equals(newPassword)) {
                        LeoApplication.getInstance().setEncryptInfo(true, confirmPassword);
                        SystemUtils.showToast(mContext, getString(R.string.set_success));
                        finish();
                    } else {
                        confirmPassword = "";
                        passwordView.refreshViewByLength(0);
                        SystemUtils.showToast(mContext, getString(R.string.password_not_match));
                    }
                }
                break;
            case EDITING_CURRENT_PASSWORD:
                currentPassword += tag;
                passwordView.refreshViewByLength(currentPassword.length());
                if(currentPassword.length() == 4){
                    if(currentPassword.equals(LeoApplication.getInstance().encryptPassword)) {
                        if(intentMode == DISABLE_PASSWORD){
                            LeoApplication.getInstance().setEncryptInfo(false, null);
                            finish();
                        } else if(intentMode == CHANGE_PASSWORD){
                            tv_title.setText(getString(R.string.input_new_password));
                            tv_password_tip.setText(getString(R.string.new_password));
                            passwordView.refreshViewByLength(0);
                            editingStatus = EDITING_NEW_PASSWORD;
                        }
                    } else {
                        currentPassword = "";
                        passwordView.refreshViewByLength(0);
                        SystemUtils.showToast(mContext, getString(R.string.password_error));
                    }
                }
                break;
        }
    }

    /**
     * 根据不同编辑状态对相应的密码字符串进行退格
     */
    private void backspace(){
        switch (editingStatus){
            case EDITING_NEW_PASSWORD:
                if (newPassword.length() > 0) {
                    newPassword = newPassword.substring(0, newPassword.length() - 1);
                    passwordView.refreshViewByLength(newPassword.length());
                }
                break;
            case EDITING_CONFIRM_PASSWORD:
                if (confirmPassword.length() > 0) {
                    confirmPassword = confirmPassword.substring(0, confirmPassword.length() - 1);
                    passwordView.refreshViewByLength(confirmPassword.length());
                }
                break;
            case EDITING_CURRENT_PASSWORD:
                if (currentPassword.length() > 0) {
                    currentPassword = currentPassword.substring(0, currentPassword.length() - 1);
                    passwordView.refreshViewByLength(currentPassword.length());
                }
                break;
        }
    }

}
