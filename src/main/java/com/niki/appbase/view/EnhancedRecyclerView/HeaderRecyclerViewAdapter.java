package com.niki.appbase.view.EnhancedRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 为了让RecycleView可以使用header和footer，RecycleViewAdapter实现的
 * Created by wjy on 2016/3/25.
 */
public class HeaderRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView.Adapter mAdapter;

    private ArrayList<View> mHeaderViews;

    private ArrayList<View> mFootViews;

    static final ArrayList<View> EMPTY_INFO_LIST = new ArrayList();

    private int mCurrentPosition;

    public HeaderRecyclerViewAdapter(ArrayList<View> mHeaderViews, ArrayList<View> mFootViews, RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        if (mHeaderViews == null) {
            this.mHeaderViews = EMPTY_INFO_LIST;
        } else {
            this.mHeaderViews = mHeaderViews;
        }
        if (mFootViews == null) {
            this.mFootViews = EMPTY_INFO_LIST;
        } else {
            this.mFootViews = mFootViews;
        }
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int numHeaders = getHeadersCount();
        if (viewType <= RecyclerView.INVALID_TYPE - numHeaders) {
            return new HeaderViewHolder(mFootViews.get(RecyclerView.INVALID_TYPE - numHeaders - viewType));
        } else if (viewType <= RecyclerView.INVALID_TYPE) {
            return new HeaderViewHolder(mHeaderViews.get(RecyclerView.INVALID_TYPE - viewType));
        }
        RecyclerView.ViewHolder holder = mAdapter.onCreateViewHolder(parent, viewType);
        if (mAdapter instanceof RecyclerViewGridAdapter) {
            ((RecyclerViewGridAdapter) mAdapter).setOnItemClickListener(mOnItemClickListener, getHeadersCount());
        } else {
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return;
        }
        holder.itemView.setTag(position);
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        mCurrentPosition = position;
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return RecyclerView.INVALID_TYPE - position;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        return RecyclerView.INVALID_TYPE - position + adapterCount;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v, position);
    }

    @Override
    public boolean onLongClick(View v) {
        int position = (Integer) v.getTag();
        if (mOnItemClickListener != null) mOnItemClickListener.onItemLongClick(v, position);
        return false;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}