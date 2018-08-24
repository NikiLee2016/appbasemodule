package com.niki.appbase.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niki.appbase.R;
import com.niki.appbase.okhttp.http.OkHttpCallBack;
import com.niki.appbase.utils.JsonUtil;
import com.niki.appbase.utils.ListUtil;
import com.niki.appbase.utils.LogUtil;
import com.niki.appbase.utils.ToastUtil;
import com.niki.appbase.view.EnhancedRecyclerView.MyRecyclerView;
import com.niki.appbase.view.EnhancedRecyclerView.RequestState;
import com.niki.appbase.view.EnhancedRecyclerView.StateIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * Created by Niki on 2018/5/1 22:53
 * E-Mail Address：m13296644326@163.com
 */

//使用
public abstract class BaseRecyclerListFragment<T> extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    protected RecyclerView mRecyclerView;
    protected BGARefreshLayout mRefreshLayout;
    protected RecyclerView.Adapter mAdapter;
    protected List<T> mBaseDataList = new ArrayList<>();
    public static final int PAGE_SIZE = 20;
    //页码
    protected int mPageNum;
    //是否有更多数据, 判断依据是当前返回的数据的数量是否小于PAGE_SIZE
    private boolean hasMore = true;
    //当前列表里面是否有数据, 如果fragment此次生命周期内请求成功过, 那么hasData就为true, 表示有数据
    //设置hasData的目的是为了这种情况考虑: 列表刷新时, 如果刷新失败了, 以前的数据不能被移除掉, 然后显示网络错误页面
    private boolean hasData;

    private boolean isLoadingMore;
    private boolean isRefreshing;

    //默认状态为loading
    private StateIndicatorView mStateView;

    public void setRecyclerView(RecyclerView recyclerView) {
        setRecyclerView(recyclerView, true);
    }

    public void setRecyclerView(RecyclerView recyclerView, boolean needRefreshLayout) {
        mRecyclerView = recyclerView;
        //getLayoutManager
        if (mRecyclerView.getLayoutManager() == null) {
            mRecyclerView.setLayoutManager(getLayoutManager());
        }
        mAdapter = getAdapter(mBaseDataList);
        mRecyclerView.setAdapter(mAdapter);
        // TODO: 2018/5/2 初始化一个Indicator
        //如果使用者自己放了一个Indicator, 那么优先用他的
        int indicatorId = getStateIndicatorViewId();
        if (indicatorId == 0) {
            //动态inflate一个
            View view = LayoutInflater.from(mContext).inflate(R.layout.default_state_indicator, mBaseContainer, false);
            mStateView = view.findViewById(R.id.state_view);
            mStateView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            ((ViewGroup) mContentView).addView(mStateView);
        } else {
            mStateView = mContentView.findViewById(indicatorId);
        }
        initStateView();
        if (!needRefreshLayout) {
            updateStateView(RequestState.LOADING);
        }
    }

    private void initStateView() {
        mStateView.setOnRetryClickListener(() -> {
            updateStateView(RequestState.LOADING);
            onRequestData();
        });
        mStateView.setCustomEmptyView(getCustomEmptyView(mStateView.getCustomContainer()));
    }

    public void setRefreshLayout(BGARefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        mRefreshLayout.setDelegate(this);
        //是否允许下拉刷新
        mRefreshLayout.setPullDownRefreshEnable(enablePullRefresh());
        //是否允许加载更多
        mRefreshLayout.setIsShowLoadingMoreView(enableLoadingMore());
        BGARefreshViewHolder holder = new BGANormalRefreshViewHolder(mContext, true);
        mRefreshLayout.setRefreshViewHolder(holder);
        updateStateView(RequestState.LOADING);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isRefreshing = true;
        hasMore = true;
        mPageNum = 0;
        onRequestData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (!hasMore) {
            return false;
        }
        isLoadingMore = true;
        onRequestData();
        return true;
    }

    protected int getStateIndicatorViewId() {
        return 0;
    }

    public void refreshListView() {
        LogUtil.print("刷新列表", 3);
        hasMore = true;
        isRefreshing = true;
        if (mStateView.getState() == RequestState.NO_DATA
                || mStateView.getState() == RequestState.NETWORK_ERROR){
            updateStateView(RequestState.LOADING);
            onRequestData();
            return;
        }
        if (mRefreshLayout != null) {
            mRefreshLayout.beginRefreshing();
        } else {
            mPageNum = 0;
            onRequestData();
        }
    }

    // TODO: 2018/5/2 如果包含头布局, 那么这种方式不可行
    // TODO: 2018/5/2 头布局的空视图, 应该借助MyRecyclerView的HeaderView功能来实现
    public void updateStateView(RequestState state) {
        boolean showListView = shouldShowListView(state);
        int listVis = showListView ? View.VISIBLE : View.GONE;
        if (mRefreshLayout == null) {
            mRecyclerView.setVisibility(listVis);
        } else {
            mRefreshLayout.setVisibility(listVis);
        }
        if (mRecyclerView instanceof MyRecyclerView) {
            View headerView = ((MyRecyclerView) mRecyclerView).getHeaderView();
            if (headerView != null) {
                if (state == RequestState.NORMAL) {
                    mStateView.removeHeaderView();
                } else if (state == RequestState.NETWORK_ERROR && showHeaderWhenNoNetwork()) {
                    mRecyclerView.removeView(headerView);
                    mStateView.addHeaderView(headerView);
                } else if (state == RequestState.SERVER_ERROR) {

                } else if (state == RequestState.NO_DATA && showEmptyView()) {
                    mRecyclerView.removeView(headerView);
                    mStateView.addHeaderView(headerView);
                } else if (state == RequestState.LOADING) {
                    mStateView.removeHeaderView();
                }
            }
        }
        mStateView.setVisibility(!showListView ? View.VISIBLE : View.GONE);
        mStateView.setState(state, 0);
    }

    //计算是否显示listView
    private boolean shouldShowListView(RequestState state) {
        //如果没数据, 但是子类表示不显示空布局, 那么返回true
        if (state == RequestState.NO_DATA && !showEmptyView()) {
            return true;
        }
        return state == RequestState.NORMAL || state == RequestState.SERVER_ERROR;
    }


    public abstract RecyclerView.Adapter getAdapter(List<T> baseDataList);

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    //全局的callBack
    protected OkHttpCallBack mBaseCallBack = new OkHttpCallBack() {
        @Override
        public void onSucceed(String data) {
            List<T> itemList = JsonUtil.getItemList(data, getItemClazz());
            onGetItemList(itemList);
        }

        @Override
        public void onFail(String msg) {
            onRequestFailed(msg);
        }
    };


    /**
     * 如果是不需要下拉刷新和下拉加载更多的listView(数据一般是本地来源), 在加载数据成功之后, 可以直接调用本方法.
     * 从而可以继续使用本框架提供的空布局视图机制
     *
     * @param itemList 供Recycler使用的数据
     */
    public final void onGetItemList(List<T> itemList) {
        if (ListUtil.isEmptyList(itemList)) {
            //有可能是请求完了
            if (mPageNum == 0) {
                //todo 数据为空
                updateStateView(RequestState.NO_DATA);
            } else {
                updateStateView(RequestState.NORMAL);
                // ToastUtil.show("没有更多数据了");
                hasMore = false;
                stopRefreshAnim(true);
            }
        } else {
            hasData = true;
            updateStateView(RequestState.NORMAL);
            //pageNum增加一位
            mPageNum++;
            // 如果是刷新, 将原数据清空
            if (isRefreshing) {
                mBaseDataList.clear();
            }
            mBaseDataList.addAll(itemList);
            mRecyclerView.getAdapter().notifyDataSetChanged();
            //判断是否还需要分页, 根据当前值是否小于pageSize
            if (itemList.size() < PAGE_SIZE) {
                hasMore = false;
            }
            stopRefreshAnim(true);
        }
    }

    private void stopRefreshAnim(boolean success) {
        if (mRefreshLayout != null) {
            mRefreshLayout.endRefreshing();
            mRefreshLayout.endLoadingMore();
        }
        if (isRefreshing && success) {
            mRecyclerView.smoothScrollToPosition(0);
        }
        isLoadingMore = false;
        isRefreshing = false;
    }

    protected abstract Class<T> getItemClazz();

    protected void onRequestFailed(String msg) {
            //之前有没有数据, 如果有, 那么不要清空数据
        if (mPageNum == 0 && !hasData) {
            updateStateView(RequestState.NETWORK_ERROR);
        } else {
            ToastUtil.show(msg);
            updateStateView(RequestState.SERVER_ERROR);
            stopRefreshAnim(false);
        }
    }

    protected View getCustomEmptyView(ViewGroup parent){
        return null;
    }

    protected boolean enableLoadingMore() {
        return true;
    }

    protected boolean enablePullRefresh() {
        return true;
    }

    /**
     * 在数据为空时, 是否展示空布局
     * 一般用于有footer的情况
     */
    protected boolean showEmptyView() {
        return true;
    }

    /**
     * 网络错误是是否还显示header, 默认不显示
     * @return
     */
    protected boolean showHeaderWhenNoNetwork() {
        return false;
    }

}
