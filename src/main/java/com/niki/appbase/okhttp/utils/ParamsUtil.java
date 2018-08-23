package com.niki.appbase.okhttp.utils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by Niki on 2018/1/18 0018 20:01
 * E-Mail Address：m13296644326@163.com
 */

public class ParamsUtil {
    public static String createGetRequestUrlByParams(String url, Map<String, String> map){
        if(map == null || map.size() == 0){
            return url;
        }
        Set<Map.Entry<String, String>> entries = map.entrySet();
        int index = 0;
        StringBuilder urlBuilder = new StringBuilder(url);
        for (Map.Entry<String, String> entry : entries) {
            String value = entry.getValue();
            value = encodeStr(value);
            if(index == 0){
                urlBuilder.append("?").append(entry.getKey()).append("=").append(value);
            }else{
                urlBuilder.append("&").append(entry.getKey()).append("=").append(value);
            }
            index++ ;
        }
        url = urlBuilder.toString();
        return url;
    }


    public static String encodeStr(String content){
        try {
            return URLEncoder.encode(content, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
