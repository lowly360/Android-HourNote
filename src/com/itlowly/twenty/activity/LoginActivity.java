package com.itlowly.twenty.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.itlowly.twenty.R;
import com.itlowly.twenty.bean.FileNameTable;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.utils.FileUtils;
import com.itlowly.twenty.utils.Md5Uils;

/**
 * 登录界面
 * 
 * @author lowly_pc
 *
 */
public class LoginActivity extends Activity implements OnClickListener {
	private EditText et_login_username;
	private EditText et_login_password;
	private Button btn_login_signin;
	private Button btn_login_locate;
	private TextView tv_login_singup;
	private SharedPreferences mPre;
	private LinearLayout ll_login_user;
	private LinearLayout ll_login_password;
	private LinearLayout ll_login_singup;
	private TextView tv_login_or;

	private boolean isSignIn = false;
	private TranslateAnimation trans_anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mPre = getSharedPreferences("config", Context.MODE_PRIVATE);

		mPre.edit().putBoolean("SignIn", false).commit();

		String locateUser = mPre.getString("LocateUser", "LocalNote");

		boolean isLocateMode = mPre.getBoolean("LocateMode", false);

		if (locateUser.equals("LocalNote") && !isLocateMode) {

		} else {
			startActivity(new Intent(LoginActivity.this, HomeActivity.class));
			finish();
		}

		initUI();

		initListener();

		initAnim();

	}

	private void initUI() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_loginactivity);

		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		btn_login_signin = (Button) findViewById(R.id.btn_login_signin);
		btn_login_locate = (Button) findViewById(R.id.btn_login_locate);
		tv_login_singup = (TextView) findViewById(R.id.tv_login_singup);

		ll_login_user = (LinearLayout) findViewById(R.id.ll_login_user);
		ll_login_password = (LinearLayout) findViewById(R.id.ll_login_password);
		ll_login_singup = (LinearLayout) findViewById(R.id.ll_login_singup);
		tv_login_or = (TextView) findViewById(R.id.tv_login_or);
		tv_login_singup_text = (TextView) findViewById(R.id.tv_login_singup_text);

		String name = mPre.getString("LocateUser", "LocalNote");
		if (name.equals("LocalNote")) {
			et_login_username.setText("");
		} else {
			et_login_username.setText(name);
		}

	}

	private void initListener() {
		btn_login_signin.setOnClickListener(this);
		btn_login_locate.setOnClickListener(this);
		tv_login_singup.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_signin:
			// 账户模式

			signInOrSignUp();

			break;

		case R.id.btn_login_locate:

			// 启动本地模式

			mPre.edit().putBoolean("LocateMode", true).commit();
			mPre.edit().putString("LocateUser", "LocalNote").commit();

			startActivity(new Intent(this, HomeActivity.class));
			finish();

			break;

		case R.id.tv_login_singup:

			isSignIn = true;

			et_login_username.setText("");
			et_login_password.setText("");

			btn_login_signin.setText("注 册");
			btn_login_locate.setVisibility(View.INVISIBLE);
			btn_login_locate.setClickable(false);
			tv_login_or.setVisibility(View.INVISIBLE);
			tv_login_singup.setVisibility(View.INVISIBLE);
			tv_login_singup.setClickable(false);
			tv_login_singup_text.setText("按返回键取消注册");

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(DensityUtils.dp2px(this, 15),
					DensityUtils.dp2px(this, 5), DensityUtils.dp2px(this, 15),
					0);// 4个参数按顺序分别是左上右下

			ll_login_singup.setLayoutParams(layoutParams);

			startAnim();

			break;

		default:
			break;
		}
	}

	private void signInOrSignUp() {

		final String userName = et_login_username.getText().toString();
		String passWord = et_login_password.getText().toString();

		final BmobUser user = new BmobUser();
		user.setUsername(userName);

		String encodePassWord = Md5Uils.encode(passWord + userName); // 加密用户密码

		user.setPassword(encodePassWord);

		if (!isSignIn) { // 不是在注册页面

			if (userName.equals("")) {
				Toast.makeText(LoginActivity.this, "请输入用户名", 0).show();
				return;
			}

			if (passWord.equals("")) {
				Toast.makeText(LoginActivity.this, "请输入密码", 0).show();
				return;
			}

			user.login(this, new SaveListener() {
				private String fileName;

				@Override
				public void onSuccess() { // 登录成功进入到主界面

					Toast.makeText(LoginActivity.this, "登录成功", 0).show();

					mPre.edit().putBoolean("LocateMode", false).commit();

					mPre.edit().putBoolean("SignIn", true).commit();

					mPre.edit().putString("LocateUser", userName).commit(); // 保存当前用户名

					// 同步网上数据到本地数据

					BmobQuery<FileNameTable> query = new BmobQuery<FileNameTable>();
					// 查询fileLocateName叫“userName”的数据
					query.addWhereEqualTo("fileLocateName", userName);

					query.setLimit(2);

					query.findObjects(LoginActivity.this,
							new FindListener<FileNameTable>() {

								@Override
								public void onError(int arg0, String arg1) {
									// Toast.makeText(LoginActivity.this,
									// "获取文件名失败！" + arg0, 0).show();

									// 获取失败，证明没同步过数据，所以直接跳转到主页面

									startActivity(new Intent(
											LoginActivity.this,
											HomeActivity.class));
									finish();

								}

								@Override
								public void onSuccess(List<FileNameTable> arg0) {
									// Toast.makeText(LoginActivity.this,
									// "获取文件名成功！" + arg0, 0).show();

									fileName = arg0.get(0).getFileServerName();

									BmobProFile.getInstance(LoginActivity.this)
											.download(fileName,
													new DownloadListener() {

														@Override
														public void onError(
																int arg0,
																String arg1) {

														}

														@Override
														public void onSuccess(
																String fullPath) {

															carryDatebase(fullPath);

														}

														@Override
														public void onProgress(
																String arg0,
																int arg1) {

														}
													});

								}
							});
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(LoginActivity.this, "登录失败，请检查您的账号是否正确", 0)
							.show();
					// 此处应该提示出是账号错误还是密码错误

				}
			});
			mPre.edit().putBoolean("LocateMode", false).commit();
		} else { // 正在注册过程

			if (userName.equals("")) {
				Toast.makeText(LoginActivity.this, "请输入用户名", 0).show();

				return;
			}

			if (passWord.equals("")) {
				Toast.makeText(LoginActivity.this, "请输入密码", 0).show();

				return;
			}

			user.signUp(this, new SaveListener() {

				@Override
				public void onSuccess() {
					Toast.makeText(LoginActivity.this, "注册成功", 0).show();
					et_login_password.setText("");
					isSignIn = false;
					refreshview(); // 还原界面

				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(LoginActivity.this, "注册失败，用户名已存在", 0).show();

				}
			});
		}
	}

	/**
	 * 把下载好的文件复制到数据库文件中
	 * 
	 * @param fullPath
	 */
	private void carryDatebase(String fullPath) {
		File downFile = new File(fullPath);

		String path = Environment.getDataDirectory()
				+ "/data/com.itlowly.twenty/databases/"
				+ mPre.getString("LocateUser", "") + ".db";

		try {
			FileUtils.fileCopy(new File(fullPath), new File(path));
		} catch (IOException e) {
			System.out.println("复制失败");
			e.printStackTrace();
		}

		downFile.delete();

		startActivity(new Intent(LoginActivity.this, HomeActivity.class));
		finish();

	}

	private void refreshview() {
		btn_login_signin.setText("登 录");
		btn_login_locate.setVisibility(View.VISIBLE);
		btn_login_locate.setClickable(true);
		ll_login_user.setPadding(0, 0, 0, 0);
		tv_login_or.setVisibility(View.VISIBLE);
		tv_login_singup.setClickable(true);
		tv_login_singup_text.setText("还没拥有账户？");
		tv_login_singup.setVisibility(View.VISIBLE);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(DensityUtils.dp2px(this, 15),
				DensityUtils.dp2px(this, 50), DensityUtils.dp2px(this, 15), 0);// 4个参数按顺序分别是左上右下

		ll_login_singup.setLayoutParams(layoutParams);
	}

	/**
	 * 初始化动画
	 */
	private void initAnim() {

		trans_anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -80);
		trans_anim.setFillAfter(true);
		trans_anim.setDuration(1000);

	}

	private Handler handler = new Handler() {
		int pad = 0;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				pad += 1;
				ll_login_user.setPadding(0, pad, 0, 0);
				break;
			case 1:
				pad = 0;
				break;
			}

		};
	};
	private TextView tv_login_singup_text;

	private void startAnim() {
		new Thread() {

			int i = 0;

			@Override
			public void run() {
				while (i < 80) {
					SystemClock.sleep(5);
					handler.sendEmptyMessage(0);
					i++;
				}
				handler.sendEmptyMessage(1);
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if (isSignIn) {
				isSignIn = false;
				refreshview();
				return true;
			}

			break;
		}

		return super.onKeyDown(keyCode, event);
	}
}
