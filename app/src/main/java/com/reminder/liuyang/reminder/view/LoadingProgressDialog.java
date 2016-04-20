package com.reminder.liuyang.reminder.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.reminder.liuyang.reminder.R;

/**
 * Created by Administrator on 2016/4/19.
 */
public class LoadingProgressDialog extends Dialog {

    private ImageView iv_loadImage;
    private TextView tv_message;
    private Animation rotateAnimation;

    public LoadingProgressDialog(Context context, String message) {
        this(context, R.style.loading_dialog, message);
    }

    public LoadingProgressDialog(Context context, int theme, String message) {
        super(context, theme);
        this.setContentView(R.layout.dialog_loading);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;

        iv_loadImage = (ImageView) findViewById(R.id.iv_loadImage);
        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.loading);
        iv_loadImage.startAnimation(rotateAnimation);

        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_message.setText(message);
    }

    @Override
    public void show() {
        super.show();
        iv_loadImage.startAnimation(rotateAnimation);
    }

    public void setMessage(String message){
        tv_message.setText(message);
    }

}
