package com.reminder.liuyang.reminder.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.reminder.liuyang.reminder.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by albert on 8/8/14.
 */
public class SystemUtils {

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     * @param gravity
     */
    private static Toast toast;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);

    public static void showToast(Context context, String message) {
        if (context == null || TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    public static String formatTime(long timeMillis) {
        Date date = new Date(timeMillis);
        return dateFormat.format(date);
    }

    public static void showNotification(Context context, String title, String content, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.notes_launcher);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setOngoing(false);
        builder.setAutoCancel(true);
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int notifyId = random.nextInt();
        notificationManager.notify(notifyId, builder.build());
    }

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

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPasswordMD5(String password) {
        return getMD5(getMD5(password) + Constant.APP_AUTHOR);
    }

    public static void setListViewHeight(ListView listView) {
        int listViewHeight = 0;
        int adaptCount = listView.getCount();
        for (int i = 0; i < adaptCount; i++) {
            View temp = listView.getAdapter().getView(i, null, listView);
            temp.measure(0, 0);
            listViewHeight += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = listViewHeight;
        listView.setLayoutParams(layoutParams);
    }


    public static SpannableStringBuilder getStyleText(String sourceText, String matchText) {
        SpannableStringBuilder style = new SpannableStringBuilder(sourceText);
        int length = sourceText.length();
        int index = -1;
        for (int i = 0; i < length; i++) {
            index = sourceText.indexOf(matchText, index + 1);
            if (index != -1) {
                style.setSpan(new ForegroundColorSpan(Color.RED), index, index + matchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return style;
    }

}
