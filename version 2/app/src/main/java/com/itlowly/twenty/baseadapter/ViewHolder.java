package com.itlowly.twenty.baseadapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 通用viewholder类
 * 
 * @author lowly_pc 2016/1/4
 */
public class ViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConverView;

	public ViewHolder(Context context,ViewGroup parent,int layoutId,int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<>();
		this.mPosition = position;
		mConverView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConverView.setTag(this);
		
	}
	
	public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	public View getmConverView() {
		return mConverView;
	}
	
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		
		if (view == null) {
			view = mConverView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}
}
