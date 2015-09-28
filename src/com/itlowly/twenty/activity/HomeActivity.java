package com.itlowly.twenty.activity;

import com.itlowly.twenty.R;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.base.impl.HomePager;
import com.itlowly.twenty.fragment.ContentFragment;
import com.itlowly.twenty.fragment.LeftMenuFragment;
import com.itlowly.twenty.utils.DensityUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

/**
 * 主页面
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends SlidingFragmentActivity {
	private SlidingMenu slidingMenu;
	private final String FRAGMENT_LEFT_MENU = "fl_left_menu";
	private final String FRAGMENT_CONTENT = "fl_content";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		setBehindContentView(R.layout.activity_home_behind);

		slidingMenu = getSlidingMenu();
		// 设置触摸模式,全屏触摸
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		slidingMenu.setMode(SlidingMenu.LEFT);

		System.out.println("dp = "+DensityUtils.px2dp(this,240));
		
		slidingMenu.setBehindOffset(220);// 设置预留屏幕宽度

		initFragment();
	}

	/**
	 * 初始化fragm
	 */
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		// 设置fragment标签,方便通过标签还获取依附在此Activity上的fragment
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				FRAGMENT_LEFT_MENU);
		transaction.replace(R.id.fl_content, new ContentFragment(),
				FRAGMENT_CONTENT);
		transaction.commit(); // 提交事务
	}

	/**
	 * 通过标签获取侧边栏fragment
	 * 
	 * @return
	 */
	public Fragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
		return fragment;
	}

	/**
	 * 通过标签获取主页面fragment
	 * 
	 * @return
	 */
	public Fragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentByTag(FRAGMENT_CONTENT);
		return fragment;
	}
	
	/**
	 * 重新显示后，更新节目的数据
	 */
	@Override
	protected void onRestart() {
		ContentFragment fragment = (ContentFragment) getContentFragment();
		HomePager pager = (HomePager) fragment.getPager();
		pager.updateDate();
		super.onRestart();
	}
}
