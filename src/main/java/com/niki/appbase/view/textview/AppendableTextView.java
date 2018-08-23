package com.niki.appbase.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.niki.appbase.R;

/**
 * Created by Niki on 2018/5/3 15:21
 * E-Mail Address：m13296644326@163.com
 */

public class AppendableTextView extends android.support.v7.widget.AppCompatTextView {

    private String mKeyText;

    private String mValueText;

    public AppendableTextView(Context context) {
        super(context);
        init();
    }

    public AppendableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public AppendableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppendableTextView);
        mKeyText = typedArray.getString(R.styleable.AppendableTextView_key_text);
        typedArray.recycle();
    }

    private void init() {
        setText(mKeyText);
    }

    public void setKeyText(String text){
        mKeyText = text;
        setText(mKeyText);
    }

    public String getKeyText() {
        return mKeyText;
    }

    public String getValueText() {
        return mValueText;
    }

    public void setValueText(String valueText, int color) {
        if (valueText == null){
            valueText = "";
        }
        mValueText = valueText;
        String text = mKeyText + mValueText;
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(color), mKeyText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(style);
    }
    public void setValueText(String valueText){
        if (valueText == null){
            valueText = "";
        }
        mValueText = valueText;
        String text = mKeyText + mValueText;
        setText(text);
    }
}
