package com.niki.appbase.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.niki.appbase.R;

/**
 * Created by Niki on 2018/5/3 00:18
 * E-Mail Address：m13296644326@163.com
 */

public class ItemViewWithDrawable extends FrameLayout {

    private ImageView mIv_icon;
    private ImageView mIv_arrow_next;
    private TextView mTv_item_text;
    private Resources mResources;
    private int mIconResId;
    private String mText;
    private boolean mShowArrow;

    public ItemViewWithDrawable(@NonNull Context context) {
        super(context);
        init();
    }

    public ItemViewWithDrawable(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public ItemViewWithDrawable(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemViewWithDrawable);
        mIconResId = typedArray.getResourceId(R.styleable.ItemViewWithDrawable_icon, 0);
        mText = typedArray.getString(R.styleable.ItemViewWithDrawable_text);
        mShowArrow = typedArray.getBoolean(R.styleable.ItemViewWithDrawable_show_arrow, true);
        typedArray.recycle();
    }

    private void init() {
        mResources = getContext().getResources();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_item_view, this, false);
        addView(view);
        mIv_icon = view.findViewById(R.id.iv_icon);
        mIv_arrow_next = view.findViewById(R.id.iv_arrow_next);
        mTv_item_text = view.findViewById(R.id.tv_item_text);
        initView();
    }

    private void initView() {
        setBackground(mResources.getDrawable(R.drawable.selector_item_click));
        //如果id为零, 不显示
        if (mIconResId == 0){
            mIv_icon.setVisibility(View.GONE);
        }else {
            mIv_icon.setVisibility(View.VISIBLE);
            mIv_icon.setBackground(null);
            mIv_icon.setImageDrawable(mResources.getDrawable(mIconResId));
        }
        mTv_item_text.setText(mText);
        mIv_arrow_next.setVisibility(mShowArrow ? VISIBLE : GONE);

    }

    public void setItemText(String text){
        mTv_item_text.setText(text);
    }

    public void setIconResourceId(int id){
        mIv_icon.setVisibility(View.VISIBLE);
        mIv_icon.setBackground(null);
        mIv_icon.setImageDrawable(mResources.getDrawable(id));
    }

    public void showArrowNext(boolean show){
        mIv_arrow_next.setVisibility(show ? VISIBLE : GONE);
    }
}
