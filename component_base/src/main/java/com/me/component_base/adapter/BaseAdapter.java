package com.me.component_base.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

/**
 *
 * @author zhy
 * @date 16/4/9
 * 鸿洋大神
 */
public abstract class BaseAdapter<T> extends MultiItemTypeAdapter<T> {

    public BaseAdapter(final Context context, final int layoutId, List<T> data) {
        super(context, data);
        Context mContext = context;
        LayoutInflater mInflater = LayoutInflater.from(context);
        int mLayoutId = layoutId;
        List<T> mData = data;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                BaseAdapter.this.convert(holder, t, position);
            }
        });
    }

    /**
     * convert
     * @param holder holder
     * @param t T
     * @param position pos
     */
    protected abstract void convert(ViewHolder holder, T t, int position);
}
