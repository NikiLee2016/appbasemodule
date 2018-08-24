package com.niki.appbase.view.EnhancedRecyclerView;

import android.support.v7.widget.RecyclerView;

import com.niki.appbase.listeners.OnItemClickListener;

import java.util.List;

/**
 * RecycleViewAdapter适配器
 *
 * Created by wjy on 2016/3/9.
 */
public abstract class BaseListAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mData;

    protected OnItemClickListener<T> mOnItemClickListener;

    public BaseListAdapter(List<T> data) {
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void append(T t) {
        if (mData != null) {
            int size = mData.size();
            mData.add(size, t);
            notifyItemInserted(size);
        }
    }

    public void insert(T t, int position) {
        if (mData != null) {
            mData.add(position, t);
            notifyItemInserted(position);
        }
    }

    public void remove(int position) {
        if (mData != null && mData.size() > position) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public T getItem(int index){
        if (mData != null && index< mData.size() && index != -1) {
            return mData.get(index);
        }
        return null;
    }

    public void addAll(List<T> list) {
        if (mData != null) mData.addAll(list);
    }

    public void clear() {
        if (mData != null) {
            int size = mData.size();
            mData.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> TOnItemClickListener) {
        mOnItemClickListener = TOnItemClickListener;
    }

    // TODO: 2018/5/15 子类可直接粘贴此代码
    /*
    static class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
    */
}
