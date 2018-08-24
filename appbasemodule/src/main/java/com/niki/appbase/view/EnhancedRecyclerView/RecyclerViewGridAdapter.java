package com.niki.appbase.view.EnhancedRecyclerView;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * 支持headerView的同时，列数为2列
 * Created by wjy on 2016/3/25.
 */
public class RecyclerViewGridAdapter extends RecyclerView.Adapter<RecyclerViewGridAdapter.GirdViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView.Adapter mAdapter;

    private int mColumnCount;

    private Context mContext;

    private int mHeadViewSize;

    private int mItemWidth, mItemHeight; //ITEM 宽高
    private int mLeftMargin = 0, mMidMargin, mRightMargin, mTopMargin, mBottomMargin; //间隔

    public RecyclerViewGridAdapter(Context context, int columnCount, RecyclerView.Adapter mAdapter) {
        this.mContext = context;
        this.mAdapter = mAdapter;
        this.mColumnCount = columnCount;
    }

    public void setItemSize(int itemWidth, int itemHeight) {
        this.mItemWidth = itemWidth;
        this.mItemHeight = itemHeight;
    }

    public void setMargin(int left, int right, int top, int bottom, int mid) {
        mLeftMargin = left;
        mRightMargin = right;
        mTopMargin = top;
        mBottomMargin = bottom;
        mMidMargin = mid;
    }

    @Override
    public GirdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        if (mMidMargin != 0) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setSize(mMidMargin, mItemHeight);
            drawable.setColor(0x00000000);
            layout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            layout.setDividerDrawable(drawable);
        }
        layout.setPadding(mLeftMargin, mTopMargin, mRightMargin, mBottomMargin);
        return new GirdViewHolder(layout, parent, viewType);
    }

    @Override
    public void onBindViewHolder(GirdViewHolder holder, int position) {
        int size = holder.holders.length;
        for (int i = 0; i < size; i++) {
            if (holder.holders[i] != null) {
                int po = position * mColumnCount + i;
                if (mAdapter != null && po < mAdapter.getItemCount()) {
                    holder.holders[i].itemView.setVisibility(View.VISIBLE);
                    holder.holders[i].itemView.setTag(po);
                    mAdapter.onBindViewHolder(holder.holders[i], po);
                } else {
                    holder.holders[i].itemView.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return (int) Math.ceil(mAdapter.getItemCount() * 1f / mColumnCount);
        } else {
            return getItemCount();
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public long getItemId(int position) {
        if (mAdapter != null) {
            return mAdapter.getItemId(position);
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(v, position + mHeadViewSize);
    }


    @Override
    public boolean onLongClick(View v) {
        int position = (Integer) v.getTag();
        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemLongClick(v, position + mHeadViewSize);
        return true;
    }

    public HeaderRecyclerViewAdapter.OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(HeaderRecyclerViewAdapter.OnItemClickListener listener, int headSize) {
        mOnItemClickListener = listener;
        mHeadViewSize = headSize;
    }

    public void ViewHolder() {

    }

    public class GirdViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView.ViewHolder holders[];

        public GirdViewHolder(LinearLayout itemView, ViewGroup parent, int viewType) {
            super(itemView);
            holders = new RecyclerView.ViewHolder[mColumnCount];
            for (int i = 0; i < mColumnCount; i++) {
                RecyclerView.ViewHolder holder = mAdapter.createViewHolder(parent, viewType);
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                if (null == params) {
                    params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.width = mItemWidth;
                params.height = mItemHeight;
                holder.itemView.setOnClickListener(RecyclerViewGridAdapter.this);
                holder.itemView.setOnLongClickListener(RecyclerViewGridAdapter.this);
                holder.itemView.setLayoutParams(params);
                itemView.addView(holder.itemView);
                holders[i] = holder;
            }
        }
    }
}