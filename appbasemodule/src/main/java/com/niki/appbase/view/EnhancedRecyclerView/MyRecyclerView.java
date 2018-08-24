package com.niki.appbase.view.EnhancedRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.niki.appbase.utils.LogUtil;

import java.util.ArrayList;

/**
 * 自定义RecycleView，实现header和footer
 * Created by wjy on 2016/3/25.
 */
public class MyRecyclerView extends RecyclerView {

    private ArrayList<View> mHeaderViews = new ArrayList<>();

    private ArrayList<View> mFootViews = new ArrayList<>();

    private HeaderRecyclerViewAdapter mAdapter;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View view) {
        if (mHeaderViews.size() > 1){
            throw new IllegalStateException("MyRecyclerView只允许添加一个HeaderView!");
        }
        mHeaderViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderRecyclerViewAdapter)) {
                mAdapter = new HeaderRecyclerViewAdapter(mHeaderViews, mFootViews, mAdapter);
            }
        }
    }

    public View getHeaderView(){
        if (mHeaderViews.size() > 0){
            return mHeaderViews.get(0);
        }
        return null;
    }

    public void removeHeaderView(){
        if (mHeaderViews.size() > 0){
            mHeaderViews.clear();
            mAdapter.notifyDataSetChanged();
            //return mHeaderViews.get(0);
        }
    }

    public void addFootView(View view) {
        mFootViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderRecyclerViewAdapter)) {
                mAdapter = new HeaderRecyclerViewAdapter(mHeaderViews, mFootViews, mAdapter);
            }
        }
    }

    public int getHeaderSize() {
        return mHeaderViews == null ? 0 : mHeaderViews.size();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = new HeaderRecyclerViewAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mAdapter);
    }

    public void setOnItemClickListener(HeaderRecyclerViewAdapter.OnItemClickListener listener) {
        if (mAdapter != null) mAdapter.setOnItemClickListener(listener);
    }

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    private OnKeyBoardChangeListener mListener;

    /**
     * set keyboard state listener
     */
    public void setKeyboardStateListener(OnKeyBoardChangeListener listener) {
        mListener = listener;
    }

    private final static int DEFAULT_KEYBOARD_HEIGHT = 200;

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (oldH != 0) {
            if (h - oldH > DEFAULT_KEYBOARD_HEIGHT) {
                if (mListener != null) {
                    mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
                }
            } else if (oldH - h > DEFAULT_KEYBOARD_HEIGHT) {
                if (mListener != null) {
                    mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
                }
            }
        }
    }

    public interface OnKeyBoardChangeListener {
        public void onKeyBoardStateChange(int state);
    }

}