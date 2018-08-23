package com.niki.appbase.okhttp.http;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.niki.appbase.dialog.CustomProgressDialog;
import com.niki.appbase.global.GlobalUtil;
import com.niki.appbase.okhttp.listener.OnRequestCallback;
import com.niki.appbase.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Niki on 2018/4/30 16:29
 * E-Mail Address：m13296644326@163.com
 */

public class OkHttpNikiClient {
    private static OkHttpClient sHttpClient;
    private static OnRequestCallback sOnRequestCallback;
    private static Dialog sLoadingDialog;
    //private static final String DEFAULT_ERROR_MESSAGE = "您的网络好像不太稳定";
    private static final String DEFAULT_ERROR_MESSAGE = "服务器未响应";

    public static void init(OnRequestCallback callback) {
        if (sHttpClient == null) {
            initOkHttp();
        }
        sOnRequestCallback = callback;
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        // TODO: 2018/5/13 前往不要再手贱加上retryOnConnectionFailure(false)这个配置, 否则会导致android8.0系统莫名其妙的请求错误
                //.retryOnConnectionFailure(false);
        //配置缓存目录
        /*File sd_cache = new File(Constant.SD_TEMP);
        int cacheSize = 10 * 1024 * 1024;
        builder.cache(new Cache(sd_cache.getAbsoluteFile(), cacheSize));*/
        //如果需要自定义builder，httpclient.newBuilder()即可返回一个新的builder
        //System.setProperty("http.keepAlive", "false");
        sHttpClient = builder.build();
    }

    public static void cancelAll() {
        if (sHttpClient != null){
            for (Call call : sHttpClient.dispatcher().queuedCalls()) {
                call.cancel();
            }
            for (Call call : sHttpClient.dispatcher().runningCalls()) {
                call.cancel();
            }
        }

    }

    public static void doGet(Context context, HttpDataRequest request, boolean showLoader, String loadText, OkHttpCallBack callBack) {
        OkHttpUtil.onDoGet(sOnRequestCallback, request);
        request.setRequestMethod(OkHttpConstant.REQUEST_METHOD_GET);
        executeHttpRequest(context, request, null, callBack, showLoader, loadText);
    }

    public static void doGet(Context context, HttpDataRequest request, boolean showLoader, OkHttpCallBack callBack) {
        doGet(context, request, showLoader, "正在处理", callBack);
    }

    public static void doGet(HttpDataRequest request, OkHttpCallBack callBack) {
        doGet(null, request, false, callBack);
    }

    public static void doPost(Context context, HttpDataRequest request, boolean showLoader, String loadText, OkHttpCallBack callBack) {
        FormBody.Builder paramsBuilder = OkHttpUtil.onDoPost(sOnRequestCallback, request);
        request.setModeRequest(OkHttpConstant.REQUEST_MODE_ASYNC);
        executeHttpRequest(context, request, paramsBuilder, callBack, showLoader, loadText);
    }

    public static void doPost(Context context, HttpDataRequest request, boolean showLoader, OkHttpCallBack callBack) {
        //默认: 正在处理
        doPost(context, request, showLoader, "正在处理", callBack);
    }

    public static void doPost(HttpDataRequest request, OkHttpCallBack callBack) {
        doPost(null, request, false, callBack);
    }

    private static ParseResult executeHttpRequest(Context context, HttpDataRequest request, FormBody.Builder paramsBuilder
            , OkHttpCallBack callBack, boolean showLoader, String loadText) {
        if (showLoader
                && (context == null || !(context instanceof Activity))) {
            throw new IllegalArgumentException("showLoader为true, 但是无法创建对话框, 因为context不是一个activity!");
        }
        if (showLoader) {
            sLoadingDialog = CustomProgressDialog.createLoadingDialog((Activity) context, loadText);
            sLoadingDialog.setOnCancelListener(dialog -> {
                // TODO: 2018/6/9 对话框被cancel, 那么就取消全部的请求, 真的有点极端啊....
                cancelAll();
            });
            try {
                sLoadingDialog.show();
            } catch (Exception e) {
                sOnRequestCallback.onFailure(request.getUrl(), e.toString());
                e.printStackTrace();
            }
        }

        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "close")
                .url(request.getUrl());
        Request httpRequest = null;
        if (request.getMethodRequest() == OkHttpConstant.REQUEST_METHOD_GET) {
            httpRequest = builder.get().build();
        } else if (request.getMethodRequest() == OkHttpConstant.REQUEST_METHOD_POST) {
            httpRequest = builder.post(paramsBuilder.build()).build();
        } else {
            throw new IllegalArgumentException("暂不支持post和get之外的其他请求方式!");
        }
        //异步请求
        if (request.getRequestMode() == OkHttpConstant.REQUEST_MODE_ASYNC) {
            doAsyncRequest(callBack, showLoader, httpRequest);
        }
        //同步请求
        else {
            try {
                Response response = sHttpClient.newCall(httpRequest).execute();
                return OkHttpUtil.onGetResponse(sOnRequestCallback, response);
            } catch (IOException e) {
                sOnRequestCallback.onFailure(request.getUrl(), e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void dismissDialog(){
        try {
            sLoadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //进行异步请求
    private static void doAsyncRequest(OkHttpCallBack callBack, boolean showLoader, Request httpRequest) {
        Call call = sHttpClient.newCall(httpRequest);
        if (NetworkUtil.isConnected()) {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    GlobalUtil.runOnUIThread(() -> {
                        //失败, 直接返回 '网络错误'
                        if (showLoader) {
                            dismissDialog();
                        }
                        //LogUtil.print("IOException: " + e.toString(), 5);
                        if(e.toString().contains("closed")) {
                            //如果是主动取消的情况下
                            return;
                        }
                        callBack.onFail(DEFAULT_ERROR_MESSAGE);
                        if (sOnRequestCallback != null){
                            //httpRequest.url()
                            sOnRequestCallback.onFailure(httpRequest.url().toString(), e.getLocalizedMessage());
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        ParseResult parseResult = OkHttpUtil.onGetResponse(sOnRequestCallback, response);
                        GlobalUtil.runOnUIThread(() -> {
                            if (showLoader) {
                                dismissDialog();
                            }
                            if (parseResult != null) {
                                if (parseResult.isSuccess()) {
                                    callBack.onSucceed(parseResult.getDataString());
                                } else {
                                    callBack.onFail(parseResult.getErrorMessage());
                                }
                            } else {
                                callBack.onFail(DEFAULT_ERROR_MESSAGE);
                            }
                        });
                    } catch (Exception e) {
                        try {
                            sOnRequestCallback.onFailure(httpRequest.url().toString(), e.toString());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                        if (showLoader) {
                            dismissDialog();
                        }
                    }
                }
            });
        } else {
            if (showLoader) {
                dismissDialog();
            }
            callBack.onFail(DEFAULT_ERROR_MESSAGE);
        }
    }

    // TODO: 2018/4/30 上文下载文件
    //重载不要context和showLoader的方法

    //同步的Post请求
    public static ParseResult doSyncPost(HttpDataRequest request) {
        FormBody.Builder paramsBuilder = OkHttpUtil.onDoPost(sOnRequestCallback, request);
        request.setModeRequest(OkHttpConstant.REQUEST_MODE_SYNC);
        return executeHttpRequest(null, request, paramsBuilder, null, false, null);
    }

    //异步的Get请求
    public static ParseResult doSyncGet(HttpDataRequest request) {
        request.setModeRequest(OkHttpConstant.REQUEST_MODE_SYNC).setRequestMethod(OkHttpConstant.REQUEST_METHOD_GET);
        return executeHttpRequest(null, request, null, null, false, null);
    }

    //文件上传
    public static ParseResult uploadFile(HttpDataRequest request) {
        //文件的非空校验
        File file = new File(request.getUploadFilePath());
        if (!file.exists()) {
            return null;
        }
        MultipartBody.Builder builder = OkHttpUtil.onDoUploadFile(sOnRequestCallback, request);
        //一般文件的键都是file, 但是要注意还是要和后台确认
        //application/octet-stream: 标识上传任何文件类型
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse("application/octet-stream"), file));
        Request uploadRequest = new Request.Builder()
                .url(request.getUrl())//地址
                .post(builder.build())//添加请求体
                .build();
        try {
            //这个地方由于okhttp框架的缺陷, 只能使用同步请求, 所以需要开启子线程
            Response response = sHttpClient.newCall(uploadRequest).execute();
            return OkHttpUtil.onGetResponse(sOnRequestCallback, response);
        } catch (IOException e) {
            sOnRequestCallback.onFailure(request.getUrl(), e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadFile(HttpDataRequest request) {
        // TODO: 2018/4/30 文件的下载暂时没有时间来实现
        //建议参考: https://github.com/JessYanCoding/ProgressManager
    }

}
