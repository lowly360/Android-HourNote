package com.itlowly.twenty.activity;


import com.itlowly.twenty.R;
import com.itlowly.twenty.base.impl.HomePager;
import com.itlowly.twenty.base.impl.SettingPager;
import com.itlowly.twenty.fragment.ContentFragment;
import com.itlowly.twenty.fragment.LeftMenuFragment;
import com.itlowly.twenty.utils.DensityUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
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

		System.out.println("dp = " + DensityUtils.px2dp(this, 240));

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

	private int backDown = 0;

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


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			ContentFragment contentFragment = (ContentFragment) getContentFragment();
			LeftMenuFragment leftMenuFragment = (LeftMenuFragment) getLeftMenuFragment();
			switch (contentFragment.getCurrenerPager()) {
			case 0:// 界面处于主页面

				backDown++;
				if (backDown != 2) {
					Toast.makeText(this, "再按一次返回，退出应用", Toast.LENGTH_SHORT)
							.show();
				} else {
					finish();
				}

				if (backDown == 1) {
					new Thread() {
						@Override
						public void run() {
							// 2.5秒内没按下第二次back，则把backdown归零
							SystemClock.sleep(2500);

							backDown = 0;
						}
					}.start();
				}

				return true;

			case 1:// 界面处于帮助页面，按返回键应该返回到主界面
				contentFragment.setCurrenerPager(0);
				leftMenuFragment.setColor();
				break;

			case 2:// 界面处于设置页面，按返回键应该返回到主界面
				contentFragment.setCurrenerPager(0);
				leftMenuFragment.setColor();
				break;

			case 3: // 界面处于应用关于界面，按返回键应该返回到设置界面
				contentFragment.setCurrenerPager(2);
				break;

			default:
				break;
			}

			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {

		SharedPreferences mPre = getSharedPreferences("config",
				Context.MODE_PRIVATE);

		mPre.edit().putBoolean("SignIn", false).commit();

		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		System.out.println("onActivityResult回调" + resultCode);

		switch (resultCode) {
		case 101:
			System.out.println("!!!!!!!!!!!!!收到注销消息");

			this.getSharedPreferences("config", Context.MODE_PRIVATE).edit()
					.putString("LocateUser", "LocalNote").commit();

			ContentFragment fragment = (ContentFragment) getContentFragment();

			SettingPager settingPager = fragment.getSettingPager();

			settingPager.loginout();

			slidingMenu.toggle();

			// itloginout();
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
