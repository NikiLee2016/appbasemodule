package com.niki.appbase.okhttp.http;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by Niki on 2018/1/10 0010 10:42
 * E-Mail Address：m13296644326@163.com
 */

public class ParseResult {
    private boolean isSuccess;
    private String errorMessage;
    private int errorCode;
    private String dataString;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    /**
     * 封装单个jsonObject的解析过程
     * @param clazz model类的字节码文件
     * @param <T>   model数据类型
     * @return model类, 解析失败时返回null
     */
    public  <T> T getItem(Class<T> clazz){
        try {
            return JSON.parseObject(dataString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 封装一个jsonArray的解析过程
     * @param clazz model类的字节码文件
     * @param <T>   model数据类型
     * @return model类集合, 解析失败时返回null
     */
    public <T> List<T> getItemList(Class<T> clazz){
        try {
            return JSON.parseArray(dataString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
