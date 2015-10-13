package com.itlowly.twenty.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import cn.bmob.v3.Bmob;

import com.itlowly.twenty.R;

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
		
		 // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "6e731be9fab4ff3e973473e3780a892c");
		
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
		
		SharedPreferences mPre = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		if (mPre.getBoolean("LocateMode", false)) {
			//启动主页
			startActivity(new Intent(this,HomeActivity.class));
		}else {
			//启动登录界面
			startActivity(new Intent(this,LoginActivity.class));
		}
		finish();
	}

}
