package com.reminder.liuyang.reminder.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by albert on 8/8/14.
 */
public class SystemUtils {
    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        SystemUtils.showToast(context, message, Gravity.BOTTOM);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     * @param gravity
     */
    private static Toast toast;

    public static void showToast(Context context, String message, int gravity) {
        if (context == null || TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

//    /**
//     * 显示顶部dialog
//     */
//    public static Inner_RefreshDialog inner_refreshDialog;
//
//    public static Inner_RefreshDialog showInnerDialog(final Context context, String message, int showStatus) {
//
//        if (inner_refreshDialog == null) {
//            inner_refreshDialog = new Inner_RefreshDialog(context);
//            inner_refreshDialog.show(context, message);
//        }
//
//        if (!inner_refreshDialog.isShowing()) {
//            inner_refreshDialog.show(context, message);
//        }
//
//        switch (showStatus) {
//            case 0:
//                inner_refreshDialog.setStatus(Inner_RefreshDialog.LOADING, message);
//                break;
//            case 1:
//                inner_refreshDialog.setStatus(Inner_RefreshDialog.LOADINGFAIL, message);
//                break;
//            case 2:
//                inner_refreshDialog.setStatus(Inner_RefreshDialog.LOADINGSUCCESS, message);
//                break;
//        }
//
//        return inner_refreshDialog;
//    }
//
//    static WindProgressDialog progressDialog;

//    /**
//     * 显示dialog
//     *
//     * @param context
//     * @param message
//     * @param
//     */
//
//    public static void showDialog(Context context, String message, boolean showStatus) {
//
//        if (showStatus) {
//            progressDialog = WindProgressDialog.show(context, message, true, null);
//            progressDialog.show();
//        } else {
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//        }
//    }

//
//    public static void showNotification(Context context, String title, String content, PendingIntent pendingIntent) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setSmallIcon(R.drawable.notification_icon);
//        builder.setContentTitle(title);
//        builder.setContentText(content);
//        builder.setOngoing(false);
//        builder.setAutoCancel(true);
//        if (pendingIntent != null) {
//            builder.setContentIntent(pendingIntent);
//        }
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Random random = new Random();
//        int notifyId = random.nextInt();
//        notificationManager.notify(notifyId, builder.build());
//    }

    /**
     * 检查是否存在相应的Intent
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    /**
     * dp转为px
     *
     * @param dp
     * @return
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return
     */
    public static Point displaySize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    /**
     * 显示或隐藏软键盘
     *
     * @param context
     * @param view
     * @param shouldShow
     */
    public static void toggleSoftInput(Context context, View view, boolean shouldShow) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (shouldShow) {
            im.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } else {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * @param context
     * @param tel
     */
    public static void makeCall(Context context, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            SystemUtils.showToast(context, "请检查您的手机是否有拨号应用");
        }
    }

    /**
     * 创建快捷方式
     *
     * @param activity
     * @param iconResId
     * @param appnameResId
     */
    public static void createShortCut(Activity activity, int iconResId,
                                      int appnameResId) {

        // com.android.launcher.permission.INSTALL_SHORTCUT

        Intent shortcutintent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        shortcutintent.putExtra("duplicate", false);

        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                activity.getString(appnameResId));

        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                activity.getApplicationContext(), iconResId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(activity.getApplicationContext(), activity.getClass()));

        activity.sendBroadcast(shortcutintent);
    }

    /**
     * 将Uri对应的内容保存到File中
     *
     * @param context
     * @param uri
     * @param filePath
     */
    public static void saveToFile(Context context, Uri uri, String filePath) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(context.getContentResolver().openInputStream(uri));
            bos = new BufferedOutputStream(new FileOutputStream(filePath, false));
            byte[] buffer = new byte[1024];

            bis.read(buffer);
            do {
                bos.write(buffer);
            } while (bis.read(buffer) != -1);
        } catch (IOException ioe) {

        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {

            }
        }
    }

//    public static String getDeviceInfo(Context context) {
//        if (context == null) {
//            return "";
//        }
//
//        return Build.BRAND + ";" +
//                Build.MODEL + ";" +
//                "Android " + Build.VERSION.RELEASE + ";" +
//                (Util.getNetWorkStatus(context) == Util.NetworkType.NETWORK_TYPE_WIFI.ordinal() ? "WI-FI" : Util.getNetworkType(context)) + ";" +
//                (NetworkUtils.getSimOperatorName(context)) + ";" +
//                (NetworkUtils.getIPAddress(true));
//    }

    /**
     * 唤醒手机
     *
     * @param context
     */
    public static void wakePhone(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wakePhone");
        wl.acquire();
        new CountDownTimer(10000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                wl.release();
            }
        }.start();
    }

    /**
     * 震动手机
     *
     * @param context
     * @param milliseconds
     */
    public static void vibrate(Context context, long milliseconds) {
        if (context == null) {
            return;
        }

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    public static void locationToMap(Context context, String address) {
        if (context == null) {
            return;
        }

        Uri uri = Uri.parse("geo:0,0?q=" + address);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (SystemUtils.isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            SystemUtils.showToast(context, "没有安装地图应用");
        }
    }

    public static void sendSMS(Context context, String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra("sms_body", "");
        if (SystemUtils.isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            SystemUtils.showToast(context, "未安装短信应用，请安装后重试");
        }
    }

    public interface OnLoadFailListener {
        void loadFail();
    }

    /**
     * 判断Activity是否正在运行
     *
     * @param mContext
     * @return
     */
    public static boolean isActivityRunning(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            if (component.getClassName().contains(activityClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 订单号每4位空,“XXXX XXXX XXXX”
     * @param str
     * @return
     */
    public static String insertTabToString(String str) {

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            result.append(str.charAt(i));
            if (i != 0 && i % 4 == 3) {
                result.append(' ');    //每4个字符增加一个空格
            }
        }
        return result.toString();
    }

}
