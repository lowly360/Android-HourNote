package com.itlowly.twenty.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LeftMenuFragment extends BaseFragment implements OnClickListener {

	private ImageView iv_leftmenu_icon;
	private TextView tv_login_logout;
	private TextView tv_leftmenu_home;
	private TextView tv_leftmenu_help;
	private TextView tv_leftmenu_setting;
	private TextView tv_leftmenu_about;
	private TextView tv_leftmenu_exit;

	@Override
	public View initViews() {

		View view = View.inflate(mActivity,
				com.itlowly.twenty.R.layout.fragment_lfet_menu, null);

		iv_leftmenu_icon = (ImageView) view.findViewById(R.id.iv_leftmenu_icon);

		tv_login_logout = (TextView) view.findViewById(R.id.tv_login_logout);

		tv_leftmenu_home = (TextView) view.findViewById(R.id.tv_leftmenu_home);
		tv_leftmenu_help = (TextView) view.findViewById(R.id.tv_leftmenu_help);
		tv_leftmenu_setting = (TextView) view
				.findViewById(R.id.tv_leftmenu_setting);
		tv_leftmenu_about = (TextView) view
				.findViewById(R.id.tv_leftmenu_about);
		tv_leftmenu_exit = (TextView) view.findViewById(R.id.tv_leftmenu_exit);

		initListener();

		return view;
	}

	private void initListener() {
		iv_leftmenu_icon.setOnClickListener(this);
		tv_login_logout.setOnClickListener(this);
		tv_leftmenu_home.setOnClickListener(this);
		tv_leftmenu_help.setOnClickListener(this);
		tv_leftmenu_setting.setOnClickListener(this);
		tv_leftmenu_about.setOnClickListener(this);
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
			Toast.makeText(mActivity, "iv_leftmenu_icon", 0).show();
			break;

		case R.id.tv_login_logout:
			Toast.makeText(mActivity, "tv_login_logout", 0).show();
			break;

		case R.id.tv_leftmenu_home:
			Toast.makeText(mActivity, "tv_leftmenu_home", 0).show();
			break;

		case R.id.tv_leftmenu_help:
			Toast.makeText(mActivity, "tv_leftmenu_help", 0).show();
			break;

		case R.id.tv_leftmenu_setting:
			Toast.makeText(mActivity, "tv_leftmenu_setting", 0).show();
			break;

		case R.id.tv_leftmenu_about:
			Toast.makeText(mActivity, "tv_leftmenu_about", 0).show();
			break;

		case R.id.tv_leftmenu_exit:
			mActivity.finish();
			break;
		}
	}

}
