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
	private int hour;
	private int cen;
	private int sce;
	private RotateAnimation redDotRotate;
	private LocalNoteDB db;
	private DataBean dataBean;
	
	protected boolean flag = true;
	private Thread thread = null;
	private TimerActivity activity;
	private RotateAnimation outRotate;
	private RotateAnimation inRotate;


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

		db = new LocalNoteDB(mActivity);
		dataBean = db.getDataBean(title, type);

		String time = dataBean.getTime();
		
		

		long timeLong = Long.valueOf(time);

		
		
		hour = LongToTime.getTimeHour(timeLong);
		cen = LongToTime.getTimecen(timeLong);
		sce = LongToTime.getTimeSce(timeLong);

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
		
		if (timeLong<=0) {
			tv_hour.setTextSize(35);
			tv_hour.setText("计时完成");
			tv_cen.setText("00");
			tv_sce.setText("00");
		}

		setAnimation();

		return mRootView;
	}

	private void setAnimation() {
		outRotate = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		outRotate.setInterpolator(new LinearInterpolator());

		outRotate.setDuration(60000);

		outRotate.setRepeatCount(Animation.INFINITE);

	

		inRotate = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		inRotate.setInterpolator(new LinearInterpolator());

		inRotate.setDuration(10000);

		inRotate.setRepeatCount(Animation.INFINITE);

		

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
					// 更新UI
					updateTimeUI();
				}
				break;
			case 1:
				tv_hour.setTextSize(35);
				tv_hour.setText("计时完成");
				tv_cen.setText("00");
				tv_sce.setText("00");
				
				iv_circle_in.clearAnimation();
				iv_circle_out.clearAnimation();
				
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

	

	public void stopCount() {
		iv_circle_out.clearAnimation();
		
		iv_circle_in.clearAnimation();
		
		iv_circle_out.clearAnimation();
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
		
		if (timeScr<=0) {
			tv_hour.setTextSize(35);
			tv_hour.setText("计时完成");
			tv_cen.setText("00");
			tv_sce.setText("00");
			
			flag = false;
			
			return ;	
			
		}
		
		iv_circle_in.startAnimation(outRotate);
		
		iv_circle_out.startAnimation(inRotate);
		this.time = timeScr / 1000;

		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {
					while (flag) {
						SystemClock.sleep(1000);
						handler.sendEmptyMessage(0);// 设置每秒发送一次消息
						time = time - 1;
						
						
						if (time == 0) {
							flag = false;
							//发送计时完成消息
							handler.sendEmptyMessage(1);
						}
					}
				}
			};
		}
		thread.start();

	}

	/**
	 * 返回的是秒，不是毫秒
	 * @return
	 */
	public long getTime() {
		return time;
	}

}
