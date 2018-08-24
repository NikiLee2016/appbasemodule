package com.niki.appbase.utils;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Niki on 2018/5/1 23:57
 * E-Mail Address：m13296644326@163.com
 */

public class JsonUtil {
    public static <T> T getItem(String data, Class<T> clazz){
        try {
            return JSON.parseObject(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> getItemList(String data, Class<T> clazz){
        try {
            return JSON.parseArray(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getValue(String data, String key, T defValue){
        try {
            JSONObject jsonObject = new JSONObject(data);
            return (T) jsonObject.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }
}
