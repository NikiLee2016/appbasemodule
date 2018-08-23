package com.niki.appbase.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niki.appbase.dialog.CustomProgressDialog;

/**
 * Created by Niki on 2018/5/1 21:15
 * E-Mail Address：m13296644326@163.com
 */

public abstract class BaseFragment extends Fragment {
    protected int mLayoutResId;//布局文件
    protected ViewGroup mBaseContainer;
    protected Activity mContext;
    protected View mContentView;
    private Dialog mLoadingDialog;

    public static <T extends BaseFragment> T getInstance(T fragment, Bundle args){
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatus();
        mLayoutResId = getLayoutResId();
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseContainer = container;
        mContentView = inflater.inflate(mLayoutResId, container, false);
        //If has title layout, set title
        initView(inflater, mContentView);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        if (autoRequestData()){
            onRequestData();
        }
    }

    /**
     * 是否自动开始请求(第一次)数据
     * 默认为true, ui加载完毕之后会自动请求数据
     * @return
     */
    protected boolean autoRequestData(){
        return true;
    }

    protected abstract void onRequestData();

    // TODO: 2018/5/1 这三个方法的执行顺序为从上而下
    //初始化状态
    protected void initStatus() {

    }
    //初始化view,ButterKnife的方法放在initView方法的第一行就可以了
    protected abstract void initView(LayoutInflater inflater, View view);

    //开始进行数据处理或者数据请求
    private void initData() {

    }

    public abstract int getLayoutResId();

    public void showLoadingDialog(String text) {
        mLoadingDialog = CustomProgressDialog.createLoadingDialog(mContext, text);
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
