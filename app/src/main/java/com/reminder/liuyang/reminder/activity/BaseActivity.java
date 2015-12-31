package com.reminder.liuyang.reminder.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.reminder.liuyang.reminder.utils.SharedPrefsUtil;

/**
 * Created by liuyang on 15/12/29.
 */
public class BaseActivity extends Activity {
    protected SharedPrefsUtil mSharedPrefs;

    private ClipboardManager mClipboardManager;
    private ProgressDialog mProgressDialog;
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mSharedPrefs = new SharedPrefsUtil(getApplicationContext(), Constant.SharedPrefrence.SHARED_NAME);
        mContext = this;
    }

    protected Activity getActivity() {
        return this;
    }


//    /**
//     * 传送数据提示框
//     *
//     * @param msg
//     */
//    public void showProgressDialog(String msg, boolean showStatus) {
//        SystemUtils.showDialog(getActivity(), msg, showStatus);
//    }


//    /**
//     * 传送数据提示框
//     *
//     * @param msg
//     */
//    private Inner_RefreshDialog inner_refreshDialog;
//
//    public void showInnerDialog(String msg, int showStatus) {
//        try {
//            inner_refreshDialog = SystemUtils.showInnerDialog(getActivity(), msg, showStatus);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * activity间无返回跳转
     *
     * @param cls
     */
    public void jumpToActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    /**
     * 复制文字到剪贴板
     */
    public void copyToClipBoard(String s) {
        if (mClipboardManager == null) {
            mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        mClipboardManager.setPrimaryClip(ClipData.newPlainText("plain text", s));
    }

}
