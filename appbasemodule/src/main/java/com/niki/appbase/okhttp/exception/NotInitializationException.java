package com.niki.appbase.okhttp.exception;

/**
 * Created by Niki on 2018/1/10 0010 10:53
 * E-Mail Address：m13296644326@163.com
 */

public class NotInitializationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NotInitializationException(String message) {
        super(message);
    }
}
