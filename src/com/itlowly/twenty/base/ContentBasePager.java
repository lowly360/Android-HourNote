package com.itlowly.twenty.base;


import android.app.Activity;
import android.view.View;

/**
 * 主页下的基本页面
 * 
 * @author Administrator
 * 
 */
public abstract class ContentBasePager {

	public Activity mActivity;

	public View mRootView;
	
	public String title;
	public String type;

	public ContentBasePager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
		initData();
	}
	
	public ContentBasePager(Activity activity,String title,String type) {
		mActivity = activity;
		this.title = title;
		this.type = type;
		mRootView = initView();
		initData();
	}

	/**
	 * 初始化界面
	 * 
	 * @return
	 */
	public abstract View initView();

	/**
	 * 初始化数据
	 */
	public void initData() {

	}

}
