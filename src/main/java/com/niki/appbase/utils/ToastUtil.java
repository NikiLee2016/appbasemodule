/**
 *
 */
package com.niki.appbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.niki.appbase.global.GlobalUtil;

public class ToastUtil {

    private static Toast mToast;

    @SuppressLint("ShowToast")
    private static void show(Context context, String text) {
        //Toast.makeText(context, info, Toast.LENGTH_LONG).show();
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void show(String text) {
        show(GlobalUtil.getContext(), text);
    }

    public static void show(int textId) {
        show(GlobalUtil.getContext(), GlobalUtil.getString(textId));
    }

    @SuppressLint("ShowToast")
    public static void show(Context context, int text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
