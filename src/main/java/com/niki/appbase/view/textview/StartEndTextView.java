package com.niki.appbase.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.niki.appbase.R;

/**
 * Created by Niki on 2018/5/25 11:24
 * E-Mail Address：m13296644326@163.com
 */

public class StartEndTextView extends FrameLayout {

    private String mKeyText;
    private TextView mTvValue;

    public StartEndTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public StartEndTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public StartEndTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StartEndTextView);
        mKeyText = typedArray.getString(R.styleable.StartEndTextView_start_text);
        typedArray.recycle();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_start_end_view, this, false);
        addView(view);
        TextView tvKey = view.findViewById(R.id.tv_key);
        mTvValue = view.findViewById(R.id.tv_value);
        tvKey.setText(mKeyText);
    }

    public void setValueText(String valueText){
        mTvValue.setText(valueText);
    }
}
