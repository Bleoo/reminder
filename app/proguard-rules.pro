# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/liuyang/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-ignorewarnings

-optimizationpasses 7  #指定代码的压缩级别 0 - 7
-dontpreverify  #混淆时是否做预校验（可去掉加快混淆速度
-verbose #混淆时是否记录日志（混淆后生产映射文件 map 类名 -> 转化后类名的映射

#所有activity的子类不要去混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.preference.Preference

-keepattributes Signature  #过滤泛型（不写可能会出现类型转换错误，一般情况把这个加上就是了）

# 不混淆BmobSDK
-keep class cn.bmob.v3.** {*;}

# 保证继承自BmobObject、BmobUser类的JavaBean不被混淆
-keep class * extends cn.bmob.v3.BmobObject {*;}

# 如果你使用了okhttp、okio的包，请添加以下混淆代码
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**

# 如果你使用了support v4包，请添加如下混淆代码
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
