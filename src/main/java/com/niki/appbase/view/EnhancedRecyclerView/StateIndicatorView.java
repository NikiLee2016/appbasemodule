package com.niki.appbase.view.EnhancedRecyclerView;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niki.appbase.R;
import com.niki.appbase.listeners.SimpleOnClickListener;


/**
 * ListView为空时显示
 * Created by wjy on 2015/4/23.
 */
public final class StateIndicatorView extends FrameLayout {
    private RequestState mState = RequestState.LOADING;
    private View mLoadingView;
    private TextView mTextView;
    private ImageView mEmptyView;

    public static RequestState[] stateValues;
    private String mEmptyString = "";
    private int mEmptyImageRes, mEmptyNetErrorRes;
    private TextView mRetryView;

    private SimpleOnClickListener mSimpleListener;
    private LinearLayout mLl_container;
    private FrameLayout mCustomViewContainer;
    private View mCustomView;
    private LinearLayout mLl_state_container;

    public StateIndicatorView(Context context) {
        this(context, null);
    }

    public StateIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (null == stateValues) {
            stateValues = RequestState.values();
        }
        LayoutInflater.from(context).inflate(R.layout.list_indicator_view, this, true);
        mLl_container = findViewById(R.id.ll_container);
        mLoadingView = findViewById(R.id.empty_view_progress);
        mTextView = findViewById(R.id.empty_view_text);
        mEmptyView = findViewById(R.id.empty_view_image);
        mRetryView = findViewById(R.id.tv_retry);
        mCustomViewContainer = findViewById(R.id.fl_custom_view_container);
        mLl_state_container = findViewById(R.id.ll_state_container);
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    private void internalSetState() {
        switch (mState) {
            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mLl_state_container.setVisibility(GONE);
                mCustomViewContainer.setVisibility(GONE);
                break;
            case NO_DATA:
                mLoadingView.setVisibility(GONE);
                if (mCustomView == null){
                    mLl_state_container.setVisibility(VISIBLE);
                    mCustomViewContainer.setVisibility(GONE);
                    mEmptyView.setImageResource(mEmptyImageRes != 0 ? mEmptyImageRes : R.drawable.empty_data);
                    mTextView.setText("还没有数据哦");
                    //mRetryView.setVisibility(GONE);
                    // TODO: 2018/5/28 空数据也要支持加载
                    mRetryView.setOnClickListener(v -> {
                        if (mSimpleListener != null) {
                            mSimpleListener.onClick();
                        }
                    });
                    mRetryView.setText("刷新");
                }
                else {
                    mLl_state_container.setVisibility(GONE);
                    mCustomViewContainer.setVisibility(VISIBLE);

                }
                break;
            case NETWORK_ERROR:
                mLoadingView.setVisibility(GONE);
                mLl_state_container.setVisibility(VISIBLE);
                mCustomViewContainer.setVisibility(GONE);
                mEmptyView.setImageResource(mEmptyNetErrorRes != 0 ? mEmptyNetErrorRes : R.drawable.network_error);
                mTextView.setText("网络错误, 点击重试");
                mRetryView.setOnClickListener(v -> {
                    if (mSimpleListener != null) {
                        mSimpleListener.onClick();
                    }
                });
                mRetryView.setText("重试");
                break;
        }
    }

    public void addHeaderView(View view) {
        try {
            if (mLl_container.getChildCount() > 2) {
                throw new IllegalStateException("StateIndicatorView只允许添加一个HeaderView!");
            }
            if (view.getLayoutParams() != null) {
                mLl_container.addView(view, 0, view.getLayoutParams());
            } else {
                mLl_container.addView(view, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeHeaderView() {
        if (mLl_container.getChildCount() > 1){
            mLl_container.removeViewAt(0);
        }
    }

    public final RequestState getState() {
        return mState;
    }

    public final void setState(RequestState state, String empty, int imageRes) {
        if (state == RequestState.NO_DATA) mEmptyImageRes = imageRes;
        else mEmptyNetErrorRes = imageRes;
        if (!TextUtils.equals(mEmptyString, empty) || (state != mState
                && (!(state == RequestState.NO_DATA && (mState == RequestState.NETWORK_ERROR
                || mState == RequestState.SERVER_ERROR))))) {
            // If current state is NETWORK_ERROR or SERVER_ERROR,
            // we are not turning to NO_DATA
            mEmptyString = empty;
            mState = state;
            try {
                internalSetState();
            } catch (Exception ignored) {
            }
        }
    }

    public final void setState(RequestState state, String empty) {
        setState(state, empty, 0);
    }

    public final void setState(RequestState state, int imageRes) {
        setState(state, null, imageRes);
    }

    public void setOnRetryClickListener(SimpleOnClickListener listener) {
        mSimpleListener = listener;
    }

    public void setCustomEmptyView(View view){
        if (view != null){
            mCustomView = view;
            mCustomViewContainer.addView(mCustomView);
        }
    }

    public ViewGroup getCustomContainer(){
        return mCustomViewContainer;
    }
}
