package com.itlowly.twenty.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;

import com.itlowly.twenty.R;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.utils.Md5Uils;

/**
 * 闪屏页
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends Activity {

	private SharedPreferences mPre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, "6e731be9fab4ff3e973473e3780a892c");

		mPre = getSharedPreferences("config", Context.MODE_PRIVATE);

		initView();
		initDate();

	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.activity_splash);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			JumpToNext();
		};
	};

	private void initDate() {

		new Thread() {
			public void run() {
				SystemClock.sleep(1500);
				handler.sendEmptyMessage(0);
				this.interrupt();
			};
		}.start();
	}

	/**
	 * 跳转到下一页面
	 */
	protected void JumpToNext() {

		if (mPre.getBoolean("isLocalPasswd", false)) {
			// 判定是否需要本地密码

			showPassWdDialog();

		} else {
			if (mPre.getBoolean("LocateMode", false)) {
				// 启动主页
				startActivity(new Intent(SplashActivity.this,
						HomeActivity.class));
				finish();
			} else {
				// 启动登录界面
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
				finish();
			}
		}

	}

	private void showPassWdDialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this, R.style.CustomDialog);

		View view = View.inflate(this, R.layout.dialog_add_tag, null);

		ImageView iv_ok = (ImageView) view.findViewById(R.id.iv_ok);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);

		TextView tv_dialog_hint = (TextView) view
				.findViewById(R.id.tv_dialog_hint);

		tv_dialog_hint.setText("请输入密码");

		final EditText et_tagname = (EditText) view
				.findViewById(R.id.et_tagname);

		iv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				finish();
			}
		});

		iv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et_tagname.getText().toString();

				if (Md5Uils.encode(password + "TwentyHours").equals(
						mPre.getString("LocalPassWd", ""))) {

					dialog.dismiss();
					if (mPre.getBoolean("LocateMode", false)) {
						// 启动主页
						startActivity(new Intent(SplashActivity.this,
								HomeActivity.class));
						finish();
					} else {
						// 启动登录界面
						startActivity(new Intent(SplashActivity.this,
								LoginActivity.class));
						finish();
					}

				} else {
					Toast.makeText(SplashActivity.this, "密码错误，请重新输入",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		lp.width = DensityUtils.dp2px(this, 312);
		lp.height = DensityUtils.dp2px(this, 255);

		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

}
