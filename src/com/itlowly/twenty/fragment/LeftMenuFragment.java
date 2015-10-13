package com.itlowly.twenty.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.HomeActivity;
import com.itlowly.twenty.activity.LoginActivity;
import com.itlowly.twenty.base.impl.SettingPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LeftMenuFragment extends BaseFragment implements OnClickListener {

	private ImageView iv_leftmenu_icon;
	private TextView tv_login_logout;
	private TextView tv_leftmenu_home;
	private TextView tv_leftmenu_help;
	private TextView tv_leftmenu_setting;
	private TextView tv_leftmenu_exit;
	private HomeActivity mainUI;
	private ContentFragment contentFragment;
	private int mCurrenerItem = 0;

	@Override
	public View initViews() {

		mPre = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);

		locateMode = mPre.getBoolean("LocateMode", false);
		signIn = mPre.getBoolean("SignIn", false);

		View view = View.inflate(mActivity,
				com.itlowly.twenty.R.layout.fragment_lfet_menu, null);

		iv_leftmenu_icon = (ImageView) view.findViewById(R.id.iv_leftmenu_icon);

		tv_login_logout = (TextView) view.findViewById(R.id.tv_login_logout);

		tv_leftmenu_home = (TextView) view.findViewById(R.id.tv_leftmenu_home);
		tv_leftmenu_help = (TextView) view.findViewById(R.id.tv_leftmenu_help);
		tv_leftmenu_setting = (TextView) view
				.findViewById(R.id.tv_leftmenu_setting);
		tv_leftmenu_exit = (TextView) view.findViewById(R.id.tv_leftmenu_exit);

		tv_leftmenu_home.setTextColor(mActivity.getResources().getColor(
				R.color.green));

		if (locateMode) {
			tv_login_logout.setText("本地模式");
			tv_login_logout.setTextColor(Color.GRAY);
		} else {
			String nameString = mPre.getString("LocateUser", "LocalNote");
			if (nameString.equals("LocalNote")) {
				tv_login_logout.setText("登录");
			}else {
				tv_login_logout.setText(nameString);
			}
			
		}

		initListener();

		return view;
	}

	private void initListener() {
		iv_leftmenu_icon.setOnClickListener(this);
		tv_login_logout.setOnClickListener(this);
		tv_leftmenu_home.setOnClickListener(this);
		tv_leftmenu_help.setOnClickListener(this);
		tv_leftmenu_setting.setOnClickListener(this);
		tv_leftmenu_exit.setOnClickListener(this);

	}

	@Override
	public void initData() {

	}

	/**
	 * 切换slidingmenu的状态
	 * 
	 * @param b
	 */
	protected void toggleSlidingMenu() {
		HomeActivity mainUi = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();

		slidingMenu.toggle();// 切换侧边栏状态
	}

	/**
	 * 设置当前菜单详情页
	 * 
	 * @param position
	 */
	protected void setCurrentMenuDetailPager(int position) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_leftmenu_icon:

			talk();

			break;

		case R.id.tv_login_logout:
			HomeActivity mainUi = (HomeActivity) mActivity;
			locateMode = mPre.getBoolean("LocateMode", false);
			signIn = mPre.getBoolean("SignIn", false);
			if (locateMode) {
				Toast.makeText(mActivity, "现在是本地模式,登录请去设置中设置账号", 0).show();
			} else {
				boolean equals = mPre.getString("LocateUser", "LocalNote").equals("LocalNote");
				if (!equals) {
					

					tv_login_logout.setText("登录");

					ContentFragment content = (ContentFragment) mainUi
							.getContentFragment();
					SettingPager settingPager = content.getSettingPager();
					settingPager.loginout();

				}else {
					mainUi.startActivity(new Intent(mainUi,LoginActivity.class));
					mainUi.finish();
				}

			}

			break;

		case R.id.tv_leftmenu_home:
			mainUI = (HomeActivity) mActivity;
			contentFragment = (ContentFragment) mainUI.getContentFragment();
			contentFragment.setCurrenerPager(0);
			mainUI.getSlidingMenu().toggle();
			mCurrenerItem = 0;
			changeColor();

			break;

		case R.id.tv_leftmenu_help:
			mainUI = (HomeActivity) mActivity;
			contentFragment = (ContentFragment) mainUI.getContentFragment();
			contentFragment.setCurrenerPager(1);
			mainUI.getSlidingMenu().toggle();
			mCurrenerItem = 1;
			changeColor();
			break;

		case R.id.tv_leftmenu_setting:
			mainUI = (HomeActivity) mActivity;

			contentFragment = (ContentFragment) mainUI.getContentFragment();
			contentFragment.setCurrenerPager(2);
			mainUI.getSlidingMenu().toggle();

			mCurrenerItem = 2;
			changeColor();

			break;

		case R.id.tv_leftmenu_exit:
			mActivity.finish();
			break;
		}
	}

	/**
	 * 聊天
	 */
	private static int click_num = 0;
	private SharedPreferences mPre;
	private boolean locateMode;
	private boolean signIn;

	private void talk() {

		String[] talk = new String[] { "别点了，点了也没用", "别再点我，制作者还没完成这个功能呢！",
				"你还点我，想被剁手啊！", "还点我，我确定你是小儿多动症了，得治！！",
				"好吧！我准备自动关闭这个软件了，服了你了，别再点了", "3,2,1!boom!!!",
				"还有3秒就自动关闭了，再见啦！！！", "哈哈，吓你的！未来的版本我会更加智能的，敬请期待喔！" };
		if (click_num == talk.length) {

			click_num = -1;

		} else {

			if (click_num != -1) {
				Toast.makeText(mActivity, talk[click_num], 0).show();
				click_num++;
			}
		}
	}

	private void changeColor() {
		int green = mActivity.getResources().getColor(R.color.green);
		tv_leftmenu_home.setTextColor(Color.WHITE);
		tv_leftmenu_help.setTextColor(Color.WHITE);
		tv_leftmenu_setting.setTextColor(Color.WHITE);

		switch (mCurrenerItem) {
		case 0:
			tv_leftmenu_home.setTextColor(green);
			break;
		case 1:
			tv_leftmenu_help.setTextColor(green);
			break;
		case 2:
			tv_leftmenu_setting.setTextColor(green);
			break;
		}
	}

	/**
	 * 用来方便其它页面调用此方法来后台改变菜单选定文字颜色
	 */
	public void setColor() {
		HomeActivity mainUI = (HomeActivity) mActivity;
		ContentFragment contentFragment = (ContentFragment) mainUI
				.getContentFragment();
		mCurrenerItem = contentFragment.getCurrenerPager();
		changeColor();
	}

	public void setLoginBtn() {
		if (mPre.getBoolean("LocateMode", false)) {// 本地模式
			tv_login_logout.setText("本地模式");
			tv_login_logout.setTextColor(Color.GRAY);

		} else {// 用户模式
			
			
			String nameString = mPre.getString("LocateUser", "LocalNote");
			
			if (nameString.equals("LocalNote")) {
				tv_login_logout.setText("登录");
			}else {
				tv_login_logout.setText(nameString);
			}
			
			tv_login_logout.setTextColor(Color.WHITE);

		}

	}
}
