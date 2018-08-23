package com.niki.appbase.okhttp.http;

/**
 * Created by Niki on 2018/4/30 16:46
 * E-Mail Address：m13296644326@163.com
 */

public interface OkHttpCallBack {

    /**
     * 透过外层固定数据格式, 拿到的有效信息
     * @param data 包含有效信息的数据, 一般可转为json
     */
    void onSucceed(String data);

    /**
     * 1.与服务器的连接出现错误
     * 2.服务器返回的errCode不为零
     * @param msg 错误信息, 连接不成功则返回'网络错误'
     */
    void onFail(String msg);

}
