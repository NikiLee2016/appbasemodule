package com.niki.appbase.view.textview;

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
 * Created by Niki on 2018/5/2 18:35
 * E-Mail Address：m13296644326@163.com
 */

public class TextCheckBox extends FrameLayout {
    private Context mContext;
    private boolean mChecked;
    private Drawable mCircleEmpty;
    private Drawable mCircleSelected;
    private ImageView mIv_status;
    private TextView mTv_text;
    private OnCheckChangedListener mOnCheckChangedListener;

    public void setOnCheckChangedListener(OnCheckChangedListener onCheckChangedListener) {
        mOnCheckChangedListener = onCheckChangedListener;
    }

    public TextCheckBox(@NonNull Context context) {
        super(context);
        init();
    }

    public TextCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextCheckBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        mCircleEmpty = mContext.getResources().getDrawable(R.drawable.circle_empty);
        mCircleSelected = mContext.getResources().getDrawable(R.drawable.circle_selected);
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_text_check_box, this, false);
        addView(view);
        view.setOnClickListener(v -> {
            updateCheckState(!mChecked);
        });
        mIv_status = view.findViewById(R.id.iv_status);
        mTv_text = view.findViewById(R.id.tv_text);
    }

    public void setCheckText(CharSequence text){
        mTv_text.setText(text);
    }

    private void updateCheckState(boolean checked){
        mChecked = checked;
        mIv_status.setImageDrawable(checked ? mCircleSelected : mCircleEmpty);
        if (mOnCheckChangedListener != null){
            mOnCheckChangedListener.onCheckChanged(checked);
        }
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        updateCheckState(checked);
    }

    public static interface OnCheckChangedListener{
        void onCheckChanged(boolean checked);
    }

}
