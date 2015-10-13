package com.itlowly.twenty.base.timerpager;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.TimerActivity;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.utils.LongToTime;

public class CountDown extends ContentBasePager {

	private ImageView iv_circle_out;
	private ImageView iv_circle_in;
	private TextView tv_hour;
	private TextView tv_cen;
	private TextView tv_sce;
	private ImageView iv_red_dot;
	private int hour;
	private int cen;
	private int sce;
	private RotateAnimation redDotRotate;
	private LocalNoteDB db;
	private DataBean dataBean;

	private long time;

	public CountDown(Activity activity, String title, String type) {
		super(activity, title, type);

	}

	@Override
	public View initView() {
		activity = (TimerActivity) mActivity;

		mRootView = View.inflate(mActivity, R.layout.timer_countdown_pager,
				null);

		iv_circle_out = (ImageView) mRootView.findViewById(R.id.iv_circle_out);
		iv_circle_in = (ImageView) mRootView.findViewById(R.id.iv_circle_in);

		tv_hour = (TextView) mRootView.findViewById(R.id.tv_hour);
		tv_cen = (TextView) mRootView.findViewById(R.id.tv_cen);
		tv_sce = (TextView) mRootView.findViewById(R.id.tv_sce);
		iv_red_dot = (ImageView) mRootView.findViewById(R.id.iv_red_dot);

		db = new LocalNoteDB(mActivity);
		dataBean = db.getDataBean(title, type);

		String time = dataBean.getTime();

		long timeLong = Long.valueOf(time);

		hour = LongToTime.getTimeHour(timeLong);
		cen = LongToTime.getTimecen(timeLong);
		sce = LongToTime.getTimeSce(timeLong);

		tv_hour.setText("" + hour);
		if (cen <= 10) {
			tv_cen.setText("0" + cen);
		} else {
			tv_cen.setText("" + cen);
		}
		if (sce <= 10) {
			tv_sce.setText("0" + sce);
		} else {
			tv_sce.setText("" + sce);
		}

		setAnimation();

		return mRootView;
	}

	private void setAnimation() {
		RotateAnimation outRotate = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		outRotate.setInterpolator(new LinearInterpolator());

		outRotate.setDuration(5000);

		outRotate.setRepeatCount(Animation.INFINITE);

		iv_circle_out.startAnimation(outRotate);

		RotateAnimation inRotate = new RotateAnimation(0, -359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		inRotate.setInterpolator(new LinearInterpolator());

		inRotate.setDuration(4000);

		inRotate.setRepeatCount(Animation.INFINITE);

		iv_circle_in.startAnimation(inRotate);

		redDotRotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		redDotRotate.setDuration(1000);

		redDotRotate.setRepeatCount(1);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (flag) {
					
					iv_red_dot.startAnimation(redDotRotate);
					// 更新UI
					updateTimeUI();
				}

				break;
			}
		};
	};

	private void updateTimeUI() {

		hour = LongToTime.getTimeHour(time * 1000);
		cen = LongToTime.getTimecen(time * 1000);
		sce = LongToTime.getTimeSce(time * 1000);

		tv_hour.setText("" + hour);
		if (cen < 10) {
			tv_cen.setText("0" + cen);
		} else {
			tv_cen.setText("" + cen);
		}
		if (sce < 10) {
			tv_sce.setText("0" + sce);
		} else {
			tv_sce.setText("" + sce);
		}
	}

	protected boolean flag = true;
	private Thread thread = null;
	private TimerActivity activity;



	public void stopCount() {
		iv_red_dot.clearAnimation();
		flag = false;
		if (thread!=null) {
			thread.interrupt();
			thread = null;
		}		
	
	}

	/**
	 * 本地计时
	 */
	public void startCount(final long timeScr) {
		flag = true;
		
		this.time = timeScr / 1000;

		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {
					while (flag) {
						SystemClock.sleep(1000);
						handler.sendEmptyMessage(0);// 设置每秒发送一次消息
						time = time - 1;
					}
				}
			};
		}
		thread.start();

	}

	public long getTime() {
		return time;
	}

}
