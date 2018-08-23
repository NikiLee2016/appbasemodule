package com.niki.appbase.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.niki.appbase.global.GlobalUtil;

/**
 * SharePreferences操作工具类
 */
public class SPUtil {
    private static String tag = SPUtil.class.getSimpleName();
    private final static String SP_NAME = "config";
    private static SharedPreferences sp;

    public static void saveBoolean(String key, boolean value) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void saveString(String key, String value) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();

    }

    public static void clear() {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().clear().apply();
    }

    /**
     * 保存long型
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, long value) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putLong(key, value).apply();
    }

    public static void saveInt(String key, int value) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 保存float型
     *
     * @param key
     * @param value
     */
    public static void saveFloat(String key, float value) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        sp.edit().putFloat(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);

        return sp.getString(key, defValue);
    }

    public static String getString(String key) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);

        return sp.getString(key, null);
    }

    public static int getInt(String key, int defValue) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        return sp.getInt(key, defValue);
    }

    /**
     * 获取long值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(String key, long defValue) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        return sp.getLong(key, defValue);
    }

    /**
     * 获取float值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(String key, float defValue) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        return sp.getFloat(key, defValue);
    }

    public static boolean getBoolean(String key,
                                     boolean defValue) {
        if (sp == null)
            sp = GlobalUtil.getContext().getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(key, defValue);
    }

    public static void saveObject(String key, Object obj){
        if (obj != null){
            try {
                saveString(key, JSON.toJSONString(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T getObject(String key, Class<T> clazz){
        String string = getString(key);
        if (StringUtils.isNotEmpty(string)){
            try {
              return  JSON.parseObject(string, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
