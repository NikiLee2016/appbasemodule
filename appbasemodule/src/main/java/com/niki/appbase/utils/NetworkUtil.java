package com.niki.appbase.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.niki.appbase.global.GlobalUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {

    private static Context getContext() {
        return GlobalUtil.getContext();
    }

    /**
     * 检测手机是否有网络连接, 不管是wifi还是GPRS网络
     *
     * @return 是否有网络连接
     */
    public static boolean isConnected() {
        //由于8.0版本网络判断出问题, 所以直接返回false
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return true;
        }
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }*/
        return true;
    }


    /**
     * 检测手机是否有Wifi网络连接
     *
     * @return 是否有Wifi网络连接
     */
    public static boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检测手机是否是GPRS / 3G网络连接
     *
     * @return 是否是GPRS / 3G网络连接
     */
    public static boolean isMobileConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检测手机是否处于飞行模式
     *
     * @return 是否处于飞行模式
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn() {
        try {
            int modeIdx = Settings.System.getInt(getContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
            return modeIdx == 1;
        } catch (SecurityException e) {
            return false;
        }
    }

    /*public static final int NETWORK_TYPE_UNKNOWN = -1;
    public static final int NETWORK_TYPE_NET_WORK_DISABLED = 0;
    public static final int NETWORK_TYPE_WAP = 1;// wap
    public static final int NETWORK_TYPE_NET = 2;// net
    public static final int NETWORK_TYPE_WIFI = 3;// wifi
    public static final int NETWORK_TYPE_CM_NET = 4;// cmnet

    public static String getNetworkTypeString() {
        int nettype = checkNetworkType();
        String result = "";
        switch (nettype) {
            case NETWORK_TYPE_NET:
                result = "3gnet";
                break;
            case NETWORK_TYPE_WAP:
                result = "wap";
                break;
            case NETWORK_TYPE_WIFI:
                result = "wifi";
                break;
            case NETWORK_TYPE_NET_WORK_DISABLED:
                result = "网络处于关闭状态";
                break;
            case NETWORK_TYPE_UNKNOWN:
                result = "未知类型";
                break;
            case NETWORK_TYPE_CM_NET:
                result = "cmnet";
        }
        return result;
    }

    */
    /**
     *
     * 检测手机网络连接状态类型
     * @return 手机网络连接状态类型
     *//*
    public static int checkNetworkType() {
        final String CTWAP = "ctwap";
        final String CMWAP = "cmwap";
        final String WAP_3G = "3gwap";
        final String NET_3G = "3gnet";
        final String NET_CM_3G = "cmnet";
        final String UNIWAP = "uniwap";

        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return NETWORK_TYPE_NET_WORK_DISABLED;
            } else {
                int netType = info.getType();
                int retType = NETWORK_TYPE_UNKNOWN;
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    retType = NETWORK_TYPE_WIFI;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    String netMode = info.getExtraInfo();
                    if (netMode != null) {
                        netMode = netMode.toLowerCase(Locale.getDefault());
                        if (netMode.equals(NET_3G)) {
                            retType = NETWORK_TYPE_NET;
                        } else if (netMode.equals(NET_CM_3G)) {
                            retType = NETWORK_TYPE_CM_NET;
                        } else {
                            if (netMode.equals(CTWAP) || netMode.equals(CMWAP) || netMode.equals(WAP_3G) || netMode.equals(UNIWAP)) {
                                return NETWORK_TYPE_WAP;
                            }
                        }
                    }
                }
                return retType;
            }
        } catch (Exception ex) {
            return NETWORK_TYPE_UNKNOWN;
        }
    }
*/

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
