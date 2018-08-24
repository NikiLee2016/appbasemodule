package com.niki.appbase.global;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.niki.appbase.bean.DeviceInfoBean;
import com.niki.appbase.imageloader.imageloader.MyImageLoader;
import com.niki.appbase.utils.NetworkUtil;
import com.niki.appbase.utils.StreamUtils;
import com.niki.appbase.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class GlobalUtil {

    private static Context mContext;
    private static int mMainThreadPid;
    private static Handler mHandler;

    public static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public static DateFormat getFormatter() {
        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        }
        return formatter;
    }

    public static final void init(Context context, String crashPath) {
        //初始化context
        //pid
        //handler
        mContext = context;
        mMainThreadPid = Process.myPid();
        mHandler = new Handler();
        //初始化StreamUtil
        StreamUtils.init(crashPath);
        //初始化MyImageLoader
        MyImageLoader.init(context);
    }

    /**
     * 拿到应用的上下文环境.
     * 建议使用此context的实例, 提高性能
     *
     * @return 上下文环境context
     */
    public static Context getContext() {
        if (mContext == null) {
            throw new IllegalStateException("全局的Context为空! 请先初始化GlobalUtil!");
        }
        return mContext;
    }

    public static Resources getResource(){
        return getContext().getResources();
    }

    public static ContentResolver getContentResolver() {
        return getContext().getContentResolver();
    }

    /**
     * 拿到应用的Handler对象
     *
     * @return Handler对象
     */
    public static Handler getHandler() {
        return mHandler;
    }

    public static void postDelayed(Runnable runnable, long mills) {
        getHandler().postDelayed(runnable, mills);
    }

    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    /**
     * 拿到应用的进程的主线程id
     *
     * @return 进程的主线程id int
     */
    public static int getMainTid() {
        return mMainThreadPid;
    }

    /**
     * 拿到id对应的字符串资源
     *
     * @param id 资源id	int
     * @return 对应的字符串String
     */
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * 拿到id对应的字符串数组资源
     *
     * @param id 资源id	int
     * @return 对应的字符串数组String[]
     */
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    /**
     * 拿到id对应的Drawable资源
     *
     * @param id 资源id	int
     * @return 对应的Drawable对象
     */
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    /**
     * 拿到id对应的颜色值
     *
     * @param id 资源id	int
     * @return 对应的颜色值    int
     */
    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    /**
     * 拿到id对应的尺寸转化后的像素值
     *
     * @param id 资源id	int
     * @return 对应的尺寸像素值    int
     */
    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);//获得像素尺寸大小
    }

    /**
     * dp转px
     *
     * @param dp dp值	float
     * @return dp值乘以像素密度之后的px值(像素值)    int
     */
    public static int dp2px(float dp) {
        //获得密度
        float density = getContext().getResources().getDisplayMetrics().density;
        //加上0.5, 达到四舍五入的效果
        return (int) (dp * density + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px px值	int
     * @return dp值乘以像素密度之后的px值(像素值)    float
     */
    public static float px2dp(int px) {
        //获得密度
        float density = getContext().getResources().getDisplayMetrics().density;
        //加上0.5, 达到四舍五入的效果
        return px / density;
    }

    /**
     * 拿到id对应的布局文件转化为的View对象
     *
     * @param id 布局id
     * @return id对应的View对象
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 判断当前线程是否为主线程
     *
     * @return true代表运行在主线程    flase代表运行在子线程
     */
    public static boolean isRunOnUIThread() {
        //
        int myTid = Process.myTid();
        if (myTid == getMainTid()) {
            return true;
        }
        return false;
    }

    /**
     * 保证Runnable的实例中的代码在主线程中运行, 能更新UI
     *
     * @param r Runnable的实例
     */
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            r.run();
        } else {
            getHandler().post(r);
        }
    }

    public static void runOnChildThread(Runnable r) {
        if (r != null) {
            new Thread(r).start();
        }
    }

    public static ColorStateList getColorStateList(int mTabTextColorResId) {

        return getContext().getResources().getColorStateList(mTabTextColorResId);
    }

    public static String formatFileSize(long bytes) {

        return Formatter.formatFileSize(getContext(), bytes);
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 异常对象
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public static String saveCrashInfo2File(Throwable ex) {

        StringBuilder sb = new StringBuilder();
        /*for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }*/

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "other_crash/" + time + "-" + timestamp + ".txt";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            //Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 将指定字符串设置给EditText(子类)控件
     *
     * @param edt  EditText(子类)控件
     * @param text 需要设置给EditText控件的字符内容
     */
    public static void setEdtSelection(EditText edt, String text) {
        if (edt != null) {
            if (text != null){
                edt.setText(text);
                edt.setSelection(edt.getText().toString().trim().length());
            }else {
                edt.setText("");
            }
        }
    }

    /**
     * 拿到editTExt的内容
     *
     * @param edt EditText(子类)控件
     * @return 返回editTExt的内容
     */
    public static String getEdtContent(EditText edt) {
        if (edt != null) {
            return edt.getText().toString().trim();
        }
        return "";
    }

    /**
     * 让editText获取焦点
     *
     * @param edt editText
     */
    public static void getEdtFocus(EditText edt) {
        if (edt != null) {
            edt.setFocusable(true);
            edt.setFocusableInTouchMode(true);
            edt.requestFocus();
        }
    }

    /**
     * 拿到应用的包名
     *
     * @return 应用的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 拿到应用的版本号
     *
     * @return 应用的版本版本号
     */
    public static int getVersionCode() {

        try {
            return getContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 拿到应用的版本名称
     *
     * @return 应用的版本名称
     */
    public static String getVersionName() {
        try {
            return getContext().getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取点击偏移量
     * 在处理自定义控件的触摸事件时, 有时需要判断触摸偏移是否超过默认的点击偏移量
     *
     * @return 点击偏移量
     */
    public static int getTouchSlop() {
        /*ViewConfiguration configuration = ViewConfiguration.get(getContext());
        return configuration.getScaledTouchSlop();*/
        return GlobalUtil.dp2px(2);
    }


    /**
     * 判断一个集合是否为空
     *
     * @param list 集合
     * @return 是否为空
     */
    public static boolean isEmptyList(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断一个字符串是否是空字符串
     *
     * @param str 字符串
     * @return true:不是, false:是
     */
    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str) && !"null".equalsIgnoreCase(str) && str.length() > 0;
    }

    /**
     * 隐藏掉一个activity的状态栏
     *
     * @param activity activity实例
     */
    public static void hideStatusBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 将一个页面的statusBar设置为透明, 与隐藏相比,
     * statusBar设置为透明后在切换页面时不会感受到页面的突起或者塌陷
     * 注意: 只支持android5.0以后版本
     *
     * @param activity activity实例
     */
    public static void setTransparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity activity实例
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager mana = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            mana.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int parseInt(CharSequence charSequence) {
        return parseInt(charSequence.toString(), 0);
    }

    public static int parseInt(CharSequence charSequence, int def) {
        return parseInt(charSequence.toString(), def);
    }

    public static int parseInt(String string) {
        return parseInt(string, 0);
    }

    public static int parseInt(String string, int def) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return def;
        }
    }

    public static int getRandom(int range) {
        return new Random().nextInt(range);
    }

    public static long parseLong(String string) {
        return parseLong(string, 0);
    }

    public static long parseLong(String string, long def) {
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            return def;
        }

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setEditTextNotEditable(EditText edt) {
        if (edt != null) {
            edt.setFocusable(false);
            edt.setFocusableInTouchMode(false);
        }
    }

    public static void setEditTextEditable(EditText edt) {
        if (edt != null) {
            edt.setFocusable(true);
            edt.setFocusableInTouchMode(true);
        }
    }

    public static CharSequence getColorString(String text, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    public static String getStringFromEditable(Editable s) {
        if (s == null) {
            return null;
        }
        return s.toString();
    }

    /**
     * ScrollView滑动到底部
     * @param scrollView
     */
    public static void scrollToBottom(ScrollView scrollView) {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public static boolean isAndroidN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static void callPhone(Activity activity, String tel) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtil.show("请先给App配置通话权限");
            return;
        }
        activity.startActivity(intent);
    }

    public static void installApk(Activity activity, String apkPath){
        File file = new File(apkPath);
        if (!file.exists()){
            ToastUtil.show("安装失败, 安装包文件不存在");
            return;
        }
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    public static String getDeviceBrand(){
        return Build.BRAND;
    }

    public static DeviceInfoBean getDeviceInfoBean(){
        DeviceInfoBean bean = new DeviceInfoBean();
        bean.brandName = Build.BRAND;
        bean.deviceModel = Build.MODEL;
        bean.ipAddress = NetworkUtil.getIPAddress(getContext());
        try {
            PackageInfo packageInfo = getContext().getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            bean.versionCode = packageInfo.versionCode;
            bean.versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return bean;
    }

}
