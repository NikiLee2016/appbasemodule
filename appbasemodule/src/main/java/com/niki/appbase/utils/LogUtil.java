package com.niki.appbase.utils;

import android.util.Log;

import com.niki.appbase.global.GlobalUtil;

public class LogUtil {
    private static final int MIN_LEVEL = 3;

    /**
     * 打印一串日志, 日志TAG为包名 + ".log"
     * @param content   内容
     * @param level     打印级别. 如果以后想过滤掉之前的日志, 将MIN_LEVEL调高就行
     */
    public static void print(String content, int level){
        print(GlobalUtil.getPackageName() + ".log", content, level);
    }

    public static void print(String tag, String content, int level){
        if(level >= MIN_LEVEL){
            Log.i(tag, content);
        }
    }

    public static void formatPrint(String parent, String params, int level){
        if(level >= MIN_LEVEL){
            print(String.format(parent, params), level);
        }
    }
}
