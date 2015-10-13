package com.itlowly.twenty.base.impl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.HomeActivity;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.fragment.ContentFragment;

/**
 * 主页的实现类
 * 
 * @author Administrator
 * 
 */
public class AboutPager extends ContentBasePager {

	public AboutPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		mRootView = View.inflate(mActivity, R.layout.content_about_pager,
				null);
		
		ImageButton btn_about_menu = (ImageButton) mRootView.findViewById(R.id.btn_about_menu);
		
		btn_about_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//返回设置界面
				HomeActivity mainUI = (HomeActivity) mActivity;
				ContentFragment contentFragment = (ContentFragment) mainUI.getContentFragment();
				contentFragment.setCurrenerPager(2);
			}
		});

		return mRootView;

	}

}
