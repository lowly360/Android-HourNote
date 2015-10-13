package com.itlowly.twenty.base.impl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.HomeActivity;
import com.itlowly.twenty.base.ContentBasePager;

/**
 * 主页的实现类
 * 
 * @author Administrator
 * 
 */
public class HelpPager extends ContentBasePager {

	private ImageButton btn_setting_menu;

	public HelpPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		mRootView = View.inflate(mActivity, R.layout.content_help_pager,
				null);
		
		btn_setting_menu = (ImageButton) mRootView.findViewById(R.id.btn_setting_menu);
		
		btn_setting_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HomeActivity mainUI = (HomeActivity) mActivity;
				mainUI.getSlidingMenu().toggle();
			}
		});

		return mRootView;

	}

}
