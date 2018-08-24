package com.niki.appbase.view.login;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.niki.appbase.R;
import com.niki.appbase.global.GlobalUtil;
import com.niki.appbase.utils.StringUtils;

/**
 * Created by Niki on 2018/5/2 16:26
 * E-Mail Address：m13296644326@163.com
 */

public class MyLoginEditText extends FrameLayout implements TextWatcher {

    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_PASSWORD = 2;

    private Context mContext;
    private EditText mEdit_text;
    private ImageButton mBtn_clear;

    private OnTextChangedListener mOnTextChangedListener;
    private int mMaxLength;
    private String mHint;
    /**
     * 输入框输入方式, 与原生的EditText的枚举值一致
     */
    private int mInputType = -1;
    private boolean mShowClearIcon;

    public MyLoginEditText(Context context) {
        super(context);
        init();
    }

    public MyLoginEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public MyLoginEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyLoginEditText);
        mMaxLength = typedArray.getInt(R.styleable.MyLoginEditText_maxLength, 100);
        mHint = typedArray.getString(R.styleable.MyLoginEditText_hint);
        mShowClearIcon = typedArray.getBoolean(R.styleable.MyLoginEditText_showClearIcon, true);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyLoginEditText,
                0, 0);
        if (a.hasValue(R.styleable.MyLoginEditText_inputType)) {
            mInputType = a.getInt(R.styleable.MyLoginEditText_inputType, -1);
        }
        typedArray.recycle();
    }

    private void init() {
        mContext = getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_edit_text, this, false);
        addView(view);
        mEdit_text = findViewById(R.id.edit_text);
        initEditText();
        mBtn_clear = findViewById(R.id.btn_clear);
        mBtn_clear.setVisibility(GONE);
        mBtn_clear.setOnClickListener(v -> {
            mEdit_text.setText("");
        });
    }

    private void initEditText() {
        if (mInputType == INPUT_TYPE_NUMBER){
            mEdit_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else if (mInputType == INPUT_TYPE_PASSWORD){
            mEdit_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        mEdit_text.setHint(mHint);
        mEdit_text.addTextChangedListener(this);
    }

    public MyLoginEditText setEdtHint(String hint){
        mEdit_text.setHint(hint);
        return this;
    }

    public MyLoginEditText setMaxLength(int length){
        mMaxLength = length;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null){
            String text = s.toString();
            if (StringUtils.isEmpty(text)){
                onEmptyContent();
                return;
            }
            if (mOnTextChangedListener != null){
                mOnTextChangedListener.onTextChanged(text);
            }
            if (text.length() > mMaxLength){
                GlobalUtil.setEdtSelection(mEdit_text, text.substring(0, mMaxLength));
            }
            mBtn_clear.setVisibility(mShowClearIcon ? VISIBLE : GONE);
        }else {
            onEmptyContent();
        }

    }

    private void onEmptyContent() {
        if (mBtn_clear != null){
            mBtn_clear.setVisibility(View.GONE);
        }
        if (mOnTextChangedListener != null){
            mOnTextChangedListener.onTextChanged("");
        }
    }

    public static interface OnTextChangedListener{
        void onTextChanged(String text);
    }

    public MyLoginEditText setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        mOnTextChangedListener = onTextChangedListener;
        return this;
    }

    public void setText(String text) {
        GlobalUtil.setEdtSelection(mEdit_text, text);
    }

    /**
     * 拿到editText的值, 不会返回null
     * @return
     */
    public String getText(){
        String content = GlobalUtil.getEdtContent(mEdit_text);
        if (StringUtils.isEmpty(content)){
            return "";
        }
        return content;
    }


}
