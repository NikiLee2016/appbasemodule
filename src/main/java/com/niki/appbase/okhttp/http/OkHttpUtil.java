package com.niki.appbase.okhttp.http;

import com.niki.appbase.okhttp.listener.OnRequestCallback;

import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Niki on 2018/4/30 20:09
 * E-Mail Address：m13296644326@163.com
 */

class OkHttpUtil {
    static FormBody.Builder onDoPost(OnRequestCallback onRequestCallback, HttpDataRequest request){
        request.setRequestMethod(OkHttpConstant.REQUEST_METHOD_POST);
        FormBody.Builder paramsBuilder = new FormBody.Builder();
        // TODO: 2018/4/30 进行初始化
        Map<String, String> newParamMap = onRequestCallback.onAddFixedParams(request.getParamsMap());
        if (newParamMap == null || newParamMap.size() == 0) {
            newParamMap = request.getParamsMap();
        }
        if (newParamMap != null) {
            Set<String> keySet = newParamMap.keySet();
            for (String key : keySet) {
                if (newParamMap.get(key) != null){
                    paramsBuilder.add(key, newParamMap.get(key));
                }
            }
        }
        return paramsBuilder;
    }

    static void onDoGet(OnRequestCallback onRequestCallback, HttpDataRequest request){
        request.setParamsMap(onRequestCallback.onAddFixedParams(request.getParamsMap()));
    }

    static MultipartBody.Builder onDoUploadFile(OnRequestCallback onRequestCallback, HttpDataRequest request){
        request.setRequestMethod(OkHttpConstant.REQUEST_METHOD_POST);
        MultipartBody.Builder paramsBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // TODO: 2018/4/30 进行初始化
        Map<String, String> newParamMap = onRequestCallback.onAddFixedParams(request.getParamsMap());
        if (newParamMap == null || newParamMap.size() == 0) {
            newParamMap = request.getParamsMap();
        }
        if (newParamMap != null) {
            Set<String> keySet = newParamMap.keySet();
            for (String key : keySet) {
                paramsBuilder.addFormDataPart(key, newParamMap.get(key));
            }
        }
        return paramsBuilder;
    }



    static ParseResult onGetResponse(OnRequestCallback onRequestCallback, Response response){
        ResponseBody body = response.body();
        if (body != null){
            try {
                String jsonString = body.string();
                return onRequestCallback.onParseResponse(jsonString);
            } catch (Exception e) {
                try {
                    onRequestCallback.onFailure(response.request().url().toString(), e.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return null;
    }

}
