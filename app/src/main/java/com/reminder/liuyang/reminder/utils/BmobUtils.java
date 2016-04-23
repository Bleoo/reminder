package com.reminder.liuyang.reminder.utils;

import android.content.Context;

import com.reminder.liuyang.reminder.R;

/**
 * Created by Administrator on 2016/4/16.
 */
public class BmobUtils {

    public static String getErrorMsg(int code, Context context) {
        switch (code) {
            case 202:
                return context.getString(R.string.error_202);
            case 205:
                return context.getString(R.string.error_205);
            case 207:
                return context.getString(R.string.error_207);
            case 209:
                return context.getString(R.string.error_209);
            case 9010:
                return context.getString(R.string.error_9010);
            case 9016:
                return context.getString(R.string.error_9016);
            default:
                return context.getString(R.string.error_default);
        }
    }
}
