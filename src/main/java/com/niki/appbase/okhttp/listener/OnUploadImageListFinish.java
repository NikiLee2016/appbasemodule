package com.niki.appbase.okhttp.listener;

import java.util.List;

/**
 * Created by Niki on 2018/5/8 15:48
 * E-Mail Address：m13296644326@163.com
 */

public interface OnUploadImageListFinish {
    void onFinished(List<String> urlList);
    void onFailed(String message);
}
