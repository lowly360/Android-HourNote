package com.itlowly.twenty.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * 实现后台倒计时的服务
 * 
 * @author lowly_pc
 *
 */
public class CountDownService extends Service {
	long time;
	boolean flag = false;
	private Thread thread = null;

	@Override
	public void onCreate() {
		System.out.println("service 创建了");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String timeString = intent.getStringExtra("time");

		this.time = Long.valueOf(timeString);
		
		startCount();

		return super.onStartCommand(intent, flags, startId);
	}
	

	/**
	 * 开始计时
	 */
	public void startCount() {
		System.out.println("开始倒计时");
		flag = true;
		countDown();
	}

	/**
	 * 计时的子线程
	 */
	private void countDown() {
		
		if (thread == null) {
			thread  = new Thread() {
				@Override
				public void run() {
					while (flag) {
						time -= 1000;
						SystemClock.sleep(1000);
						System.out.println("计时中：" + time);
					}
				}
			};
			thread.start();
		}		
		
	}
	
	public void stopCount(){
		System.out.println("结束计时：" + time);
		if (thread!=null) {
			thread.interrupt();
			thread = null;
		}
		
		flag = false;
	}
	
	public long getTime() {
		
		return time;
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return new MsgBinder();
	}

	public class MsgBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public CountDownService getService() {
			return CountDownService.this;
		}
	}
}
