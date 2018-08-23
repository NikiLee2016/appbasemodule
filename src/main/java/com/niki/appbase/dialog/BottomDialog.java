package com.niki.appbase.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niki.appbase.R;
import com.niki.appbase.global.GlobalUtil;

import java.util.ArrayList;

public class BottomDialog {

    private Dialog mDialog;
    private View mView;
    private String[] mTextArray;
    private OnItemClickListener mOnItemClickListener;
    private LinearLayout mLlContainer;
    private ArrayList<TextView> mTextViewList;

    /**
     * @param context activity的引用
     * @param title 对话框的标题
     * @param textArray 条目的text集合
     */
    public BottomDialog(Activity context, String title, String[] textArray) {
        if (textArray == null || textArray.length == 0) {
            throw new IllegalArgumentException("textArray数组不能为空!");
        }
        mDialog = new Dialog(context);
        mView = View.inflate(GlobalUtil.getContext(), R.layout.dialog_ll_and_btn, null);
        initDefaultView(title);
        mTextArray = textArray;
        initContentView();
    }

    private void initDefaultView(String title) {
        mLlContainer = mView.findViewById(R.id.ll_container);
        TextView titleView = mView.findViewById(R.id.tv_title);
        titleView.setText(title);
        TextView viewById = mView.findViewById(R.id.tv_cancel);
        viewById.setOnClickListener(v -> mDialog.dismiss());
    }

    private LinearLayout.LayoutParams getTvLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = GlobalUtil.dp2px(10);
        return layoutParams;
    }

    private void initDialog() {
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //View view = View.inflate(mContext, R.layout.dialog_ll_and_btn_3, null);
        mDialog.setContentView(mView);
        //getWindow().setBackgroundDrawable(null);
        Window window = mDialog.getWindow();
        assert window != null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.setBackgroundDrawable(null);
        } else {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        window.setGravity(Gravity.BOTTOM);
        //window.getDecorView().setPadding(0, paddTop, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mDialog.show();
    }

    private void initContentView() {
        mTextViewList = new ArrayList<>();
        for (int i = 0; i < mTextArray.length; i++) {
            TextView textView = new TextView(mDialog.getContext());
            textView.setLayoutParams(getTvLayoutParams());
            textView.setText(mTextArray[i]);
            setTvStyle(textView);
            int finalI = i;
            textView.setOnClickListener(v -> {
                mDialog.dismiss();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(finalI);
                }
            });
            mLlContainer.addView(textView, mLlContainer.getChildCount() - 1);
            mTextViewList.add(textView);
        }
    }

    /**
     * 若想自定义textView的值, 可以拿到条目集合, 根据对应数组拿到textView引用, 然后进行自定义
     * @return TextViewList
     */
    public ArrayList<TextView> getTextViewList() {
        return mTextViewList;
    }

    private void setTvStyle(TextView textView) {
        int dp10 = GlobalUtil.dp2px(10);
        textView.setPadding(dp10, dp10, dp10, dp10);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(Color.parseColor("#66abfa"));
        textView.setBackground(GlobalUtil.getDrawable(R.drawable.rect_with_stroke));
        textView.setGravity(Gravity.CENTER);
    }

    @FunctionalInterface
    public static interface OnItemClickListener {
        void onClick(int which);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        initDialog();
    }
}
