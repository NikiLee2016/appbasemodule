package com.niki.appbase.okhttp.listener;


import com.niki.appbase.okhttp.http.ParseResult;

import java.util.Map;

/**
 * Created by Niki on 2018/1/10 0010 10:43
 * E-Mail Address：m13296644326@163.com
 */

public interface OnRequestCallback {
    /**
     * 解析一段json, 从中抽取我们需要的errorMessage, errorCode和data数据
     *
     * @param response json字符串
     * @return 包含errorMessage, errorCode和data数据的ParseResult对象
     */
    ParseResult onParseResponse(String response);

    /**
     * 拿到原始参数map, 添加每次请求固定的参数, 比如token, versionCode等等
     * @param originMap 原始参数map
     * @return 添加完固定参数的map
     */
    // TODO: 2018/4/30 添加一个能支持添加参数的固定参数的回调
    Map<String, String> onAddFixedParams(Map<String, String> originMap);

    //请求
    void onFailure(String url, String msg);

}
