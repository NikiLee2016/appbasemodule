package com.niki.appbase.view.searchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.niki.appbase.R;
import com.niki.appbase.global.GlobalUtil;
import com.niki.appbase.utils.StringUtils;

/**
 * Created by Niki on 2018/5/5 21:04
 * E-Mail Address：m13296644326@163.com
 */

public class SearchBarView extends FrameLayout implements TextWatcher {

    private ImageView mIv_icon_left;
    private TextView mBtnSerach;
    private EditText mEdit_text;
    private View mBtnClear;
    private Drawable mSearIcon;
    private Drawable mDeleteIcon;
    private OnTextChangedListener mOnTextChangedListener;
    private String mHint;
    private boolean mIsDoingReplace;

    private static final int ANIMATE_DURATION = 300;

    public SearchBarView(@NonNull Context context) {
        super(context);
        init();
    }

    public SearchBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public SearchBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchBarView);
        mHint = typedArray.getString(R.styleable.SearchBarView_hint_text);
        typedArray.recycle();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_search_bar, this, false);
        addView(view);
        mBtnSerach = view.findViewById(R.id.btn_search);
        mEdit_text = view.findViewById(R.id.edit_text);
        mBtnClear = view.findViewById(R.id.ll_clear_text);
        mIv_icon_left = view.findViewById(R.id.iv_icon_left);

        //初始化drawable对象
        mSearIcon = getContext().getResources().getDrawable(R.drawable.search_icon);
        mDeleteIcon = getContext().getResources().getDrawable(R.drawable.delete_circle);

        mIv_icon_left.setBackground(mSearIcon);
        mBtnClear.setOnClickListener(v -> mEdit_text.setText(""));
        mBtnSerach.setVisibility(GONE);
        mBtnSerach.setOnClickListener(v -> {
            if (mOnTextChangedListener != null) {
                mOnTextChangedListener.onSearchClick(GlobalUtil.getEdtContent(mEdit_text));
            }
        });
        if (StringUtils.isNotEmpty(mHint)) {
            mEdit_text.setHint(mHint);
        }
        mEdit_text.addTextChangedListener(this);
        new EdtKeyListenerAdapter().setEditText(mEdit_text, () -> {
            String edtContent = GlobalUtil.getEdtContent(mEdit_text);
            if (!TextUtils.isEmpty(edtContent)) {
                //开始搜索
                if (mOnTextChangedListener != null){
                    mOnTextChangedListener.onSearchClick(edtContent);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = GlobalUtil.getStringFromEditable(s);
        if (mOnTextChangedListener != null) {
            mOnTextChangedListener.onTextChanged(content);
        }
        if (StringUtils.isNotEmpty(content)) {
            showSearchBtn();
            if (!mIsDoingReplace){
                replaceLeftIcon(mDeleteIcon);
            }
        } else {
            dismissSearchBtn();
            if (!mIsDoingReplace){
                replaceLeftIcon(mSearIcon);
            }
        }
    }

    private void replaceLeftIcon(Drawable newDrawable) {
        if (newDrawable == mIv_icon_left.getBackground()){
            return;
        }
        mIsDoingReplace = true;
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(ANIMATE_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIv_icon_left.setBackground(newDrawable);
                mIsDoingReplace = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mIv_icon_left.startAnimation(animation);
    }

    private void showSearchBtn() {
        if (mBtnSerach.getVisibility() == VISIBLE) {
            return;
        }
        mBtnSerach.setVisibility(VISIBLE);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(ANIMATE_DURATION);
        mBtnSerach.startAnimation(animation);
    }

    private void dismissSearchBtn() {
        if (mBtnSerach.getVisibility() == GONE) {
            return;
        }
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(ANIMATE_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnSerach.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBtnSerach.startAnimation(animation);
    }

    public static interface OnTextChangedListener {
        void onTextChanged(String text);

        void onSearchClick(String text);
    }

    public SearchBarView setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        mOnTextChangedListener = onTextChangedListener;
        return this;
    }

    public String getContent(){
        return GlobalUtil.getEdtContent(mEdit_text);
    }
}
