package com.niki.appbase.utils;

import com.niki.appbase.bean.LatLngBean;

import java.text.DecimalFormat;

/**
 * Created by Niki on 2018/5/10 11:07
 * E-Mail Address：m13296644326@163.com
 */

public class MapUtil {
    public static float getDistance(LatLngBean var0, LatLngBean var1) {
        if(var0 != null && var1 != null) {
            try {
                double var2 = 0.01745329251994329D;
                double var4 = var0.longitude;
                double var6 = var0.latitude;
                double var8 = var1.longitude;
                double var10 = var1.latitude;
                var4 *= 0.01745329251994329D;
                var6 *= 0.01745329251994329D;
                var8 *= 0.01745329251994329D;
                var10 *= 0.01745329251994329D;
                double var12 = Math.sin(var4);
                double var14 = Math.sin(var6);
                double var16 = Math.cos(var4);
                double var18 = Math.cos(var6);
                double var20 = Math.sin(var8);
                double var22 = Math.sin(var10);
                double var24 = Math.cos(var8);
                double var26 = Math.cos(var10);
                double[] var28 = new double[3];
                double[] var29 = new double[3];
                var28[0] = var18 * var16;
                var28[1] = var18 * var12;
                var28[2] = var14;
                var29[0] = var26 * var24;
                var29[1] = var26 * var20;
                var29[2] = var22;
                double var30 = Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2]));
                return (float)(Math.asin(var30 / 2.0D) * 1.27420015798544E7D);
            } catch (Throwable var32) {
                var32.printStackTrace();
                return 0.0F;
            }
        } else {
            try {
                throw new IllegalStateException("非法坐标值");
            } catch (Exception var33) {
                var33.printStackTrace();
                return 0.0F;
            }
        }
    }

    /**
     * 将以米为单位的长度转化为更合适的长度描述
     *
     * @param lenMeter 以米为单位的长度
     * @return 更合适的长度描述(英语)
     */
    public static String getEnglishFriendlyLength(int lenMeter) {
        if (lenMeter <= 0){
            return "未知位置";
        }
        return getEnglishFriendlyLength(lenMeter, true);
    }

    public static String getEnglishFriendlyLength(int lenMeter, boolean needRadixPoint) {

        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + " km";
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            String pattern = needRadixPoint ? "##0.0" : "##0";
            DecimalFormat fnum = new DecimalFormat(pattern);
            String dstr = fnum.format(dis);
            return dstr + " km";
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + " m";
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + " m";
    }
}
