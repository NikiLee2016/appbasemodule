package com.niki.appbase.view.searchview;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.niki.appbase.global.GlobalUtil;

public class EdtKeyListenerAdapter implements View.OnKeyListener{

    private EditText mEditText;
    private OnKeyDownListener mListener;

    public void setEditText(EditText editText, OnKeyDownListener listener) {
        mEditText = editText;
        mListener = listener;
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditText.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) GlobalUtil.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }

            if (mListener != null) {
                mListener.onEnter();
            }
            return true;
        }
        return false;
    }

    public interface OnKeyDownListener{
        void onEnter();
    }
}
