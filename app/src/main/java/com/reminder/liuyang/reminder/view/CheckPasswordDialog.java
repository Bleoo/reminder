package com.reminder.liuyang.reminder.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.reminder.liuyang.reminder.LeoApplication;
import com.reminder.liuyang.reminder.R;
import com.reminder.liuyang.reminder.activity.DecryptPasswordActivity;
import com.reminder.liuyang.reminder.utils.Constant;
import com.reminder.liuyang.reminder.utils.SystemUtils;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/4/19.
 */
public class CheckPasswordDialog extends Dialog {

    private TextView et_phone_number;
    private EditText et_password;
    private Button btn_done;

    public CheckPasswordDialog(Context context) {
        this(context, R.style.common_dialog);
    }

    public CheckPasswordDialog(final Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_check_password);
        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);

        et_phone_number = (TextView) findViewById(R.id.et_phone_number);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_done = (Button) findViewById(R.id.btn_done);

        BmobUser currentUser = BmobUser.getCurrentUser(context);
        String phoneNumber = currentUser.getMobilePhoneNumber();
        phoneNumber = phoneNumber.replaceFirst(phoneNumber.substring(3, 7), "****");
        et_phone_number.setText(context.getString(R.string.current_account) + phoneNumber);

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setDoneBtnClickable(et_password.getText().length() > 5);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                String passwordMD5 = sharedPreferences.getString(Constant.SP_PASSWORD, "");
                String password = et_password.getText().toString();
                if (passwordMD5.equals(SystemUtils.getPasswordMD5(password))) {
                    dismiss();
                    LeoApplication.getInstance().setEncryptInfo(false, null);
                    ((DecryptPasswordActivity) context).finish();
                } else {
                    SystemUtils.showToast(context, context.getString(R.string.password_error));
                }
            }
        });

        setDoneBtnClickable(false);
    }

    private void setDoneBtnClickable(boolean clickable) {
        btn_done.setClickable(clickable);
        btn_done.setAlpha(clickable ? 1 : 0.5f);
    }

}
