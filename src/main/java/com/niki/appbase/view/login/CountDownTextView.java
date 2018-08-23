package com.niki.appbase.view.login;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.niki.appbase.global.GlobalUtil;
import com.niki.appbase.utils.StringUtils;

/**
 * Created by Niki on 2018/5/7 17:56
 * E-Mail Address：m13296644326@163.com
 */

public class CountDownTextView extends android.support.v7.widget.AppCompatTextView {

    private int mColorRed;

    private static final int TOTAL_SECONDS = 60;
    private static final double TIME_BLOCK = 1;
    private int mSecondsCount;
    private int mColorGray;

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
       /* TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleTextWithCube);
        mTitle = typedArray.getString(R.styleable.TitleTextWithCube_title_text);
        mTextSize = typedArray.getDimension(R.styleable.TitleTextWithCube_textSize, GlobalUtil.dp2px(14));
        typedArray.recycle();*/
        // TODO: 2018/5/7 初始color, 计数时的color
    }

    private void init() {
        //红色
        mSecondsCount = TOTAL_SECONDS;
        mColorRed = Color.parseColor("#cf4340");
        mColorGray = Color.parseColor("#666666");
    }

    public void startCountTask(){
        setEnabled(false);
        updateSeconds(TOTAL_SECONDS);
        mSecondsCount = TOTAL_SECONDS;
        startTask();
    }

    //秒后重新获取
    //重新发送
    //发送验证码
    //蓝色 -> 灰色 -> 蓝色

    private void startTask(){
        GlobalUtil.getHandler().postDelayed(progressTask, (long) (TIME_BLOCK * 1000));
    }

    private void stopTask(){
        GlobalUtil.getHandler().removeCallbacks(progressTask);
    }

    Runnable progressTask = () -> {
        mSecondsCount --;
        updateSeconds(mSecondsCount);
        if (mSecondsCount <= 0){
            stopTask();
            onCountFinished();
        }else {
            startTask();
        }
    };

    private void updateSeconds(int seconds) {
        CharSequence numberText = GlobalUtil.getColorString(StringUtils.makeUpZero(seconds), mColorRed);
        setText(numberText);
        CharSequence suffix = GlobalUtil.getColorString("秒后重试", mColorGray);
        append(suffix);
    }

    private void onCountFinished() {
        setText("重新获取");
        setEnabled(true);
    }

}
