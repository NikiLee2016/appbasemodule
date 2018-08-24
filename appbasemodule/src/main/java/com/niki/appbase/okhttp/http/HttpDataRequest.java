package com.niki.appbase.okhttp.http;

import com.niki.appbase.okhttp.utils.ParamsUtil;

import java.util.Map;

/**
 * Created by Niki on 2018/1/18 0018 17:09
 * E-Mail Address：m13296644326@163.com
 */

public class HttpDataRequest {

    private String url;
    private String downloadFilePath;
    private String uploadFilePath;

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public HttpDataRequest setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
        return this;
    }

    private Map<String, String> paramsMap;

    /**
     * 访问方法, post or get
     * 只有同路径下的类可以访问这个属性
     */
    private int requestMethod;

    /**
     * 访问模式, 同步 or 异步
     * 默认为异步请求
     */
    private int requestMode = OkHttpConstant.REQUEST_MODE_ASYNC;

    int getRequestMode() {
        return requestMode;
    }

    HttpDataRequest setModeRequest(int requestMode) {
        this.requestMode = requestMode;
        return this;
    }

    int getMethodRequest() {
        return requestMethod;
    }

    HttpDataRequest setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public String getUrl() {
        //如果是get请求, 那么直接拼接参数
        //如果是post, 直接返回原url
        if (requestMethod == OkHttpConstant.REQUEST_METHOD_GET){
            return ParamsUtil.createGetRequestUrlByParams(url, paramsMap);
        }
        return url;
    }

    public HttpDataRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public HttpDataRequest setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
        return this;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public HttpDataRequest setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
        return this;
    }

    public HttpDataRequest() {
    }
}
