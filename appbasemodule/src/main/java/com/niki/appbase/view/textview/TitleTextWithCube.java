package com.niki.appbase.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.niki.appbase.R;
import com.niki.appbase.global.GlobalUtil;


/**
 * Created by Niki on 2018/5/3 18:31
 * E-Mail Address：m13296644326@163.com
 */

public class TitleTextWithCube extends FrameLayout {

    private View mView_cube;
    private TextView mTv_title;
    private String mTitle;
    private float mTextSize;

    public TitleTextWithCube(@NonNull Context context) {
        super(context);
        init();
    }

    public TitleTextWithCube(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public TitleTextWithCube(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleTextWithCube);
        mTitle = typedArray.getString(R.styleable.TitleTextWithCube_title_text);
        mTextSize = typedArray.getDimension(R.styleable.TitleTextWithCube_textSize, GlobalUtil.dp2px(14));
        typedArray.recycle();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_title_with_cube, this, false);
        addView(view);
        mTv_title = findViewById(R.id.tv_title);
        mView_cube = findViewById(R.id.view_cube);
        mTv_title.setText(mTitle);
        mTv_title.setTextSize(GlobalUtil.px2dp((int) mTextSize));
    }

    public void setTitle(String title){
        mTitle = title;
        mTv_title.setText(mTitle);
    }

    public void  setTextSize(int size){
        mTv_title.setTextSize(size, TypedValue.COMPLEX_UNIT_SP);
    }
    public void appendTitle(CharSequence append){
        mTv_title.append(append);
    }
}
