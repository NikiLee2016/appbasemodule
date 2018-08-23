package com.niki.appbase.view.EnhancedRecyclerView;

/**
 * Created by Niki on 2018/5/2 00:49
 * E-Mail Address：m13296644326@163.com
 */

public enum RequestState {
    //加载中
    LOADING,
    //空数据
    NO_DATA,
    //请求failed, 但是此时pageNumber大于0
    SERVER_ERROR,
    //直接请求错误
    NETWORK_ERROR,
    //正常
    NORMAL
}
