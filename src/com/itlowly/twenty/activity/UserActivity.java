package com.itlowly.twenty.activity;

import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.itlowly.twenty.R;
import com.itlowly.twenty.bean.myUser;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.utils.Md5Uils;

/**
 * 用户界面
 * 
 * @author lowly_pc
 *
 */
public class UserActivity extends Activity implements OnClickListener {
	private Button btn_user_changepassword;
	private ImageButton btn_back;
	private ImageView iv_user_icon;
	private TextView tv_user_username;
	private Button btn_user_signout;
	private SharedPreferences mPre;
	private String userName;

	private boolean isPassWordRight = false;

	private String oldPassword;
	private String newPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPre = getSharedPreferences("config", Context.MODE_PRIVATE);
		initUI();
	}

	private void initUI() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user);

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
		tv_user_username = (TextView) findViewById(R.id.tv_user_username);

		btn_user_changepassword = (Button) findViewById(R.id.btn_user_changepassword);
		btn_user_signout = (Button) findViewById(R.id.btn_user_signout);

		userName = mPre.getString("LocateUser", "未知用户");

		tv_user_username.setText(userName);

		InitListener();
	}

	private void InitListener() {
		btn_back.setOnClickListener(this);
		iv_user_icon.setOnClickListener(this);

		btn_user_changepassword.setOnClickListener(this);
		btn_user_signout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.iv_user_icon:

			break;

		case R.id.btn_user_changepassword:
			showMyDialog();
			break;

		case R.id.btn_user_signout:
			mPre.edit().putString("LocateUser", "LocalNote").commit();
//			btn_user_signout
//					.setBackgroundResource(R.drawable.login_btn_pressed_shape);
			System.out.println("到这里了！！！");
			setResult(101); // 101代表注销
			finish();
			break;

		}
	}

	// 更改密码的提示框
	private void showMyDialog() {
		final Dialog dialog = new Dialog(this, R.style.CustomDialog);

		View view = View.inflate(this, R.layout.dialog_add_tag, null);

		ImageView iv_ok = (ImageView) view.findViewById(R.id.iv_ok);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
		TextView tv_dialog_hint = (TextView) view
				.findViewById(R.id.tv_dialog_hint);

		if (isPassWordRight) {
			tv_dialog_hint.setText("输入新密码");
		} else {
			tv_dialog_hint.setText("输入旧密码");
		}

		final EditText et_tagname = (EditText) view
				.findViewById(R.id.et_tagname);

		if (isPassWordRight) {
			et_tagname.setHint("请输入新密码");
		} else {
			et_tagname.setHint("请输入旧密码");
		}
		
		iv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		// 确定事件
		iv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et_tagname.getText().toString();

				if (isPassWordRight) {
					newPassword = Md5Uils.encode(password + userName);

					BmobUser.updateCurrentUserPassword(UserActivity.this,
							oldPassword, newPassword, new UpdateListener() {

								@Override
								public void onSuccess() {
									Toast.makeText(UserActivity.this, "更改密码成功",
											0).show();
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									Toast.makeText(UserActivity.this,
											"更改密码失败" + arg1, 0).show();
								}
							});

				} else {
					oldPassword = Md5Uils.encode(password + userName); // 加密用户密码

					myUser myUser = new myUser();
					myUser.setUsername(userName);
					myUser.setPassword(oldPassword);
					myUser.login(UserActivity.this, new SaveListener() {

						@Override
						public void onSuccess() {
							isPassWordRight = true;
							showMyDialog(); // 提示输入新密码
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(UserActivity.this, "旧密码不正确", 0)
									.show();
						}
					});

				}

				dialog.dismiss();
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
