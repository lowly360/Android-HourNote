package com.itlowly.twenty.activity;

import com.itlowly.twenty.R;
import com.itlowly.twenty.R.layout;
import com.itlowly.twenty.R.menu;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

/**
 * 闪屏页
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initView();
		initDate();

	}

	

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.activity_splash);
	}
	
	private void initDate() {
		new Thread(){
			public void run() {
				SystemClock.sleep(1500);
				JumpToNext();
			};
		}.start();
	}


	/**
	 * 跳转到下一页面
	 */
	protected void JumpToNext() {
		startActivity(new Intent(this,HomeActivity.class));
		finish();
	}

}
