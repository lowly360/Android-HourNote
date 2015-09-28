package com.itlowly.twenty.base.impl;

import android.app.Activity;
import android.view.View;

import com.itlowly.twenty.R;
import com.itlowly.twenty.base.ContentBasePager;

/**
 * 主页的实现类
 * 
 * @author Administrator
 * 
 */
public class SettingPager extends ContentBasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		mRootView = View.inflate(mActivity, R.layout.content_setting_pager,
				null);

		return mRootView;

	}

}
