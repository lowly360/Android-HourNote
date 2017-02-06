package com.itlowly.twenty.baseadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 通用适配器
 * @author lowly_pc  2016/1/4
 * 
 * @param <T> Item需要展示的数据bean
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	protected int mLayoutId;

	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		this.mContext = context;
		this.mDatas = datas;
		this.mLayoutId = layoutId;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				mLayoutId, position);
		
		convert(holder, getItem(position));
		
		return holder.getmConverView();
	};
	
	/**
	 * 通用适配器需要实现的函数
	 * @param holder
	 * @param t
	 */
	public abstract void convert(ViewHolder holder,T t);

}
