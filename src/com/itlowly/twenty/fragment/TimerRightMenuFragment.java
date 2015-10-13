package com.itlowly.twenty.fragment;

import com.itlowly.twenty.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 倒计时界面的fragment实现类，主要处理图片点击事件等
 * @author Administrator
 *
 */
public class TimerRightMenuFragment extends BaseFragment implements OnClickListener {

	private TextView tv_right_share;
	private TextView tv_right_setting;
	private TextView tv_right_delete;
	private TextView tv_right_help;

	@Override
	public View initViews() {
		
		View view = View.inflate(mActivity, R.layout.fragment_timer_right_menu, null);
		
		tv_right_share = (TextView) view.findViewById(R.id.tv_right_share);
		tv_right_setting = (TextView) view.findViewById(R.id.tv_right_setting);
		tv_right_delete = (TextView) view.findViewById(R.id.tv_right_delete);
		tv_right_help = (TextView) view.findViewById(R.id.tv_right_help);
		
		hideText();
		initListener();
		return view;
	}
	
	private void initListener() {
		tv_right_share.setOnClickListener(this);
		tv_right_setting.setOnClickListener(this);
		tv_right_delete.setOnClickListener(this);
		tv_right_help.setOnClickListener(this);
	}

	/**
	 * 隐藏图标文字
	 */
	public void hideText(){
		tv_right_share.setText("");
		tv_right_setting.setText("");
		tv_right_delete.setText("");
		tv_right_help.setText("");
	}
	
	/**
	 * 设置默认的图标文字
	 */
	public void setDefaultText(){
		tv_right_share.setText("分享");
		tv_right_setting.setText("设置");
		tv_right_delete.setText("删除");
		tv_right_help.setText("帮助");
		
	}

	/**
	 * 按钮的实现
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_share:
			Toast.makeText(getActivity(), "tv_right_share", 0).show();
			break;
		case R.id.tv_right_setting:
			Toast.makeText(getActivity(), "tv_right_setting", 0).show();
			break;
		case R.id.tv_right_delete:
			Toast.makeText(getActivity(), "tv_right_delete", 0).show();
			break;
		case R.id.tv_right_help:
			Toast.makeText(getActivity(), "tv_right_help", 0).show();
			break;

		default:
			break;
		}
	}

}
