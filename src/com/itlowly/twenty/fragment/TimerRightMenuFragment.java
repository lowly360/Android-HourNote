package com.itlowly.twenty.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itlowly.twenty.R;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.utils.DensityUtils;

/**
 * 倒计时界面的fragment实现类，主要处理图片点击事件等
 * 
 * @author Administrator
 *
 */
public class TimerRightMenuFragment extends BaseFragment implements
		OnClickListener {

	private TextView tv_right_share;
	private TextView tv_right_setting;
	private TextView tv_right_delete;
	private TextView tv_right_help;

	private String title;
	private String type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public View initViews() {

		View view = View.inflate(mActivity, R.layout.fragment_timer_right_menu,
				null);

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
	public void hideText() {
		tv_right_share.setText("");
		tv_right_setting.setText("");
		tv_right_delete.setText("");
		tv_right_help.setText("");
	}

	/**
	 * 设置默认的图标文字
	 */
	public void setDefaultText() {
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
			//分享
			showShareDialog();
			break;
		case R.id.tv_right_setting:
			//设置
			//Toast.makeText(getActivity(), "tv_right_setting", 0).show();
			break;
		case R.id.tv_right_delete:
			// 删除数据
			showDeleteDialog();
			break;
		case R.id.tv_right_help:
			//帮助
			showHelpDialog();
			break;

		default:
			break;
		}
	}

	/**
	 * 显示帮助对话框
	 */
	private void showHelpDialog() {
		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);
		View view = View.inflate(mActivity, R.layout.dialog_right_help, null);
		


		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

	/**
	 * 显示分享对话框
	 */
	private void showShareDialog() {
		// *处理好分享不同内容的判定
		LocalNoteDB db = new LocalNoteDB(mActivity);

		DataBean dataBean = db.getDataBean(title, type);

		long time = Long.valueOf(dataBean.getTime());
		int cen = (int) (time / 1000 / 60);

		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);
		View view = View.inflate(mActivity, R.layout.dialog_right_share, null);
		ImageView iv_ok = (ImageView) view.findViewById(R.id.iv_ok);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
		final EditText et_right_share_content = (EditText) view
				.findViewById(R.id.et_right_share_content);

		if (dataBean.getIsTwenty().equals("1")) {// 判定是否为倒计时计划
			if (cen <= 0) {
				et_right_share_content.setText("在\"" + title
						+ "\"这个计划中已经坚持玩20个小时啦");
			} else {
				et_right_share_content.setText("在\"" + title + "\"这个计划中再坚持"
						+ cen + "分钟就可以了");
			}

		} else {
			et_right_share_content.setText("提醒自己：" + title + "，一定记得才行");
		}
		iv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		iv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				shareToFriend(et_right_share_content.getText().toString()
						+ " [TwentyHours分享]");
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		lp.width = DensityUtils.dp2px(mActivity, 312);
		lp.height = DensityUtils.dp2px(mActivity, 255);

		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

	/**
	 * 分享给朋友
	 * 
	 * @param file
	 */
	private void shareToFriend(String content) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		startActivity(intent);
	}

	/**
	 * 显示确定是否删除的对话框
	 */
	private void showDeleteDialog() {
		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);
		View view = View.inflate(mActivity, R.layout.dialog_right_delete, null);
		ImageView iv_ok = (ImageView) view.findViewById(R.id.iv_ok);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
		TextView tv_right_delete_title = (TextView) view
				.findViewById(R.id.tv_right_delete_title);
		tv_right_delete_title.setText(title);
		iv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		iv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LocalNoteDB db = new LocalNoteDB(mActivity);
				if (db.deleteData(title, type, 1)) {
					Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT)
							.show();
					mActivity.finish();
				} else {
					Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		dialog.setContentView(view);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

		lp.width = DensityUtils.dp2px(mActivity, 312);
		lp.height = DensityUtils.dp2px(mActivity, 255);

		dialog.getWindow().setAttributes(lp);
		dialog.show();
	}

}
