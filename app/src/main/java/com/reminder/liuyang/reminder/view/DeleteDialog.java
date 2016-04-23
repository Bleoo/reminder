package com.reminder.liuyang.reminder.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.reminder.liuyang.reminder.R;

/**
 * Created by Administrator on 2016/4/19.
 */
public class DeleteDialog extends Dialog {

    private Button btn_delete;

    public DeleteDialog(Context context) {
        this(context, R.style.delete_dialog);
    }

    public DeleteDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_confirm_delete);
        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_delete = (Button) findViewById(R.id.btn_delete);
    }

    public void setDeleteClickListener(View.OnClickListener deleteClickListener) {
        btn_delete.setOnClickListener(deleteClickListener);
    }

}
