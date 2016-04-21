package com.reminder.liuyang.reminder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reminder.liuyang.reminder.R;

/**
 * Created by Administrator on 2016/4/20.
 */
public class EncryptPasswordView extends LinearLayout {

    private ImageView iv_password_1;
    private ImageView iv_password_2;
    private ImageView iv_password_3;
    private ImageView iv_password_4;

    public EncryptPasswordView(Context context) {
        this(context, null);
    }

    public EncryptPasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EncryptPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.include_password_view, null);
        addView(view);
        iv_password_1 = (ImageView) view.findViewById(R.id.iv_password_1);
        iv_password_2 = (ImageView) view.findViewById(R.id.iv_password_2);
        iv_password_3 = (ImageView) view.findViewById(R.id.iv_password_3);
        iv_password_4 = (ImageView) view.findViewById(R.id.iv_password_4);
    }

    public void refreshViewByLength(int length){
        switch (length){
            case 0:
                iv_password_1.setImageResource(R.drawable.easy_password_dot_empty);
                iv_password_2.setImageResource(R.drawable.easy_password_dot_empty);
                iv_password_3.setImageResource(R.drawable.easy_password_dot_empty);
                iv_password_4.setImageResource(R.drawable.easy_password_dot_empty);
                break;
            case 1:
                iv_password_1.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_2.setImageResource(R.drawable.easy_password_dot_empty);
                iv_password_3.setImageResource(R.drawable.easy_password_dot_empty);
                iv_password_4.setImageResource(R.drawable.easy_password_dot_empty);
                break;
            case 2:
                iv_password_1.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_2.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_3.setImageResource(R.drawable.easy_password_dot_empty);
                iv_password_4.setImageResource(R.drawable.easy_password_dot_empty);
                break;
            case 3:
                iv_password_1.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_2.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_3.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_4.setImageResource(R.drawable.easy_password_dot_empty);
                break;
            case 4:
                iv_password_1.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_2.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_3.setImageResource(R.drawable.easy_password_dot_full);
                iv_password_4.setImageResource(R.drawable.easy_password_dot_full);
                break;
        }
    }

}
