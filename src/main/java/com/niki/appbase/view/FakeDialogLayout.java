package com.niki.appbase.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

/**
 * Created by Niki on 2018/5/15 11:04
 * E-Mail Address：m13296644326@163.com
 */

public class FakeDialogLayout extends LinearLayout {

    private AlphaAnimation mShowAnima;
    private AlphaAnimation mHideAnima;

    public FakeDialogLayout(Context context) {
        super(context);
        init();
    }

    public FakeDialogLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FakeDialogLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mShowAnima = new AlphaAnimation(0, 1);
        mHideAnima = new AlphaAnimation(1, 0);
        mShowAnima.setDuration(300);
        mHideAnima.setDuration(300);
        mHideAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mShowAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        setOnClickListener(v -> dismiss());
    }

    public void show(){
        setVisibility(VISIBLE);
        startAnimation(mShowAnima);
    }

    public void dismiss(){
        startAnimation(mHideAnima);
    }
}
