package com.niki.appbase.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
 * Created by Niki on 2018/5/2 23:12
 * E-Mail Address：m13296644326@163.com
 */

public class TabButtonWithTriangle extends FrameLayout {

    private Drawable mTriangleDown;
    private Drawable mTriangle_up;
    private boolean isUp;
    private ImageView mIv_triangle;
    private TextView mTv_text;

    public TabButtonWithTriangle(@NonNull Context context) {
        super(context);
        init();
    }

    public TabButtonWithTriangle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabButtonWithTriangle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.btn_with_triangle, this, false);
        addView(view);
        mTv_text = findViewById(R.id.tv_text);
        mIv_triangle = findViewById(R.id.iv_triangle);
        mTriangleDown = getContext().getResources().getDrawable(R.drawable.triangle_down);
        mTriangle_up = getContext().getResources().getDrawable(R.drawable.triangle_up);
        view.setOnClickListener(v -> {
            updateDirection(!isUp);
        });
    }

    public void setBtnText(String text){
        mTv_text.setText(text);
    }

    public void setUpDirection(boolean up){
        updateDirection(up);
    }

    private void updateDirection(boolean up) {
        isUp = up;
        mIv_triangle.setBackground(null);
        mIv_triangle.setImageDrawable(up ? mTriangle_up : mTriangleDown);
    }
}
