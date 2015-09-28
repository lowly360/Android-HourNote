package com.itlowly.twenty.activity;

import java.util.ArrayList;

import com.itlowly.twenty.R;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.base.timerpager.CountDown;
import com.itlowly.twenty.base.timerpager.DetailPager;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.fragment.LeftMenuFragment;
import com.itlowly.twenty.service.CountDownService;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.utils.ServiceUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 倒计时的页面
 * 
 * @author lowly_pc
 *
 */
public class TimerActivity extends SlidingFragmentActivity implements OnClickListener {

	private LinearLayout ll_pager;
	private TextView tv_title;
	private ImageView iv_timer_edit;
	private ImageButton ib_timer_play;
	private ImageButton ib_timer_pause;
	private ImageButton ib_timer_quit;
	private DataBean dataBean;
	private ImageButton btn_back;
	private ImageButton btn_menu;
	private TextView tv_timer_pagerTitle;
	private ViewPager vpPager;
	
	private final String FRAGMENT_LEFT_MENU = "fl_left_menu";

	private ArrayList<ContentBasePager> contentList;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUi();
		initData();
		initListener();

	}

	private void initUi() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.activity_timer);
		setContentView(R.layout.activity_timer);
		setBehindContentView(R.layout.activity_home_behind);
		//set(R.layout.activity_home_behind);
		
		slidingMenu = getSlidingMenu();
		// 设置触摸模式,全屏触摸
		
		
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		slidingMenu.setMode(SlidingMenu.RIGHT);
		
		//slidingMenu.setSecondaryShadowDrawable(resId);
		slidingMenu.setBehindScrollScale(0.5f);
		
		
		//slidingMenu.setSecondaryMenu(R.layout.activity_home_behind);

		System.out.println("dp = "+DensityUtils.px2dp(this,240));
		
		slidingMenu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 3);// 设置预留屏幕宽度

		initFragment();
		
		

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		tv_timer_pagerTitle = (TextView) findViewById(R.id.tv_timer_pagerTitle);
		btn_menu = (ImageButton) findViewById(R.id.btn_menu);

		ll_pager = (LinearLayout) findViewById(R.id.ll_pager);

		vpPager = (ViewPager) findViewById(R.id.vp_timer);

		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_timer_edit = (ImageView) findViewById(R.id.iv_timer_edit);

		ib_timer_play = (ImageButton) findViewById(R.id.ib_timer_play);
		ib_timer_pause = (ImageButton) findViewById(R.id.ib_timer_pause);
		ib_timer_quit = (ImageButton) findViewById(R.id.ib_timer_quit);

	}

	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		// 设置fragment标签,方便通过标签还获取依附在此Activity上的fragment
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				FRAGMENT_LEFT_MENU);
		transaction.commit(); // 提交事务
	}

	private void initData() {
		// 从intent中获取数据
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		type = intent.getStringExtra("type");

		// Toast.makeText(this, "-------"+title+"---"+type,
		// Toast.LENGTH_SHORT).show();

		// 根据标题，标签从数据库获取对应的数据封装到databean中
		LocalNoteDB db = new LocalNoteDB(this);
		dataBean = db.getDataBean(title, type);

		tv_title.setText(dataBean.getTitle());

		contentList = new ArrayList<ContentBasePager>();

		countDown = new CountDown(this, title, type);

		contentList.add(countDown);

		detailPager = new DetailPager(this, title, type);

		contentList.add(detailPager);

		for (int i = 0; i < contentList.size(); i++) {
			ImageView dot = new ImageView(this);
			if (i == 0) {
				dot.setBackgroundResource(R.drawable.shape_dot_currener);
			} else {
				dot.setBackgroundResource(R.drawable.shape_dot_gray);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					DensityUtils.dp2px(this, 8), DensityUtils.dp2px(this, 8));
			layoutParams.leftMargin = 8;

			dot.setLayoutParams(layoutParams);
			ll_pager.addView(dot);
		}

		vpPager.setAdapter(new MyPagerAdapter());

	}

	private void initListener() {
		btn_back.setOnClickListener(this);
		ib_timer_play.setOnClickListener(this);
		ib_timer_pause.setOnClickListener(this);
		ib_timer_quit.setOnClickListener(this);

		vpPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int count = ll_pager.getChildCount();

				for (int i = 0; i < count; i++) {
					ImageView view = (ImageView) ll_pager.getChildAt(i);
					view.setBackgroundResource(R.drawable.shape_dot_gray);
				}

				ImageView view = (ImageView) ll_pager.getChildAt(arg0);
				view.setBackgroundResource(R.drawable.shape_dot_currener);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public CountDownService myservice = null;// 绑定的服务对象，默认为 null
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("绑定失败");
			myservice = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("绑定成功");
			myservice = ((CountDownService.MsgBinder) service).getService();
		}

	};
	private String title;
	private String type;
	private CountDown countDown;
	private DetailPager detailPager;
	private boolean isStart = false; // 标志是否开始倒计时
	private Intent service;
	private SlidingMenu slidingMenu;

	/**
	 * 实现点击处理事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back: // 返回
			if (isStart) {
				showHintDailog();
				
			}else {
				finish();
			}

			break;

		case R.id.ib_timer_play:
			System.out.println("前台倒计时开始");
			ib_timer_play.setEnabled(false);
			ib_timer_pause.setEnabled(true);

			if (countDown != null && !isStart) {
				isStart = true;

				LocalNoteDB db = new LocalNoteDB(TimerActivity.this);

				dataBean = db.getDataBean(title, type);

				countDown.startCount(Long.valueOf(dataBean.getTime()));

			}

			break;

		case R.id.ib_timer_pause:
			System.out.println("前台倒计时暂停");
			ib_timer_play.setEnabled(true);
			ib_timer_pause.setEnabled(false);

			if (countDown != null) {
				isStart = false;

				countDown.stopCount();

				LocalNoteDB db2 = new LocalNoteDB(TimerActivity.this);

				boolean updateDate = db2.updateDate(dataBean,
						String.valueOf(countDown.getTime() * 1000));
				if (updateDate) {
					System.out.println("更新成功！");
				} else {
					System.out.println("更新失败!");
				}

			}

			break;
		case R.id.ib_timer_quit:

			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		System.out.println("TimerActivity:onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (isStart) { // activity切换至后台，启动后台服务倒计时，防止被系统内存不足的时候自动销毁activity

			/**
			 * 先停止本地activity的倒计时
			 */
			if (countDown != null) {
				// isStart = false;

				countDown.stopCount();

				LocalNoteDB db2 = new LocalNoteDB(TimerActivity.this);

				boolean updateDate = db2.updateDate(dataBean,
						String.valueOf(countDown.getTime() * 1000));
				if (updateDate) {
					System.out.println("更新成功！");
				} else {
					System.out.println("更新失败!");
				}
			}

			// 启动后台倒计时的服务
			System.out.println("后台启动服务中ing~~~");

			LocalNoteDB db = new LocalNoteDB(TimerActivity.this);

			dataBean = db.getDataBean(title, type); // 刷新时间数据

			service = new Intent(TimerActivity.this, CountDownService.class);
			System.out.println("准备开启服务：时间为" + dataBean.getTime());

			service.putExtra("time", dataBean.getTime());
			// 开启服务
			startService(service);
			// 绑定服务
			bindService(service, connection, Context.BIND_AUTO_CREATE);
		}

		super.onStop();
	}

	@Override
	protected void onRestart() {
		System.out.println("TimerActivity:onRestart");

		// 检测倒计时服务是否在运行，如果在运行，又未绑定，则重新绑定service
		if (ServiceUtils.isServiceRunning(this,
				"com.itlowly.twenty.service.CountDownService")) {
			isStart = true;

			if (myservice != null) {
				System.out.println("TimerActivity:onRestart isStart:" + isStart
						+ "  myservice" + myservice.toString());
			} else {
				System.out.println("重新绑定服务");

				LocalNoteDB db = new LocalNoteDB(TimerActivity.this);

				dataBean = db.getDataBean(title, type); // 刷新时间数据

				System.out.println("dataBean" + dataBean.toString() + title
						+ type);

				service = new Intent(TimerActivity.this, CountDownService.class);

				service.putExtra("time", dataBean.getTime());

				// 开启服务
				startService(service);
				// 绑定服务
				bindService(service, connection, Context.BIND_AUTO_CREATE);
			}
		} else {
			System.out.println("服务未启动！！");
		}

		if (isStart && myservice != null) {

			System.out.println("进入后台服务了");
			myservice.stopCount();

			unbindService(connection);

			if (service != null) {
				stopService(service);
				System.out.println("停止服务了");
			}

			/**
			 * 重新开始actvity的本地倒计时
			 */
			long time = myservice.getTime();

			if (countDown != null) {
				System.out.println("重新开始actvity的本地倒计时");
				countDown.startCount(time);
			}

			LocalNoteDB db2 = new LocalNoteDB(TimerActivity.this);

			boolean updateDate = db2.updateDate(dataBean, String.valueOf(time));
			if (updateDate) {
				System.out.println("后台更新成功！" + time);
			} else {
				System.out.println("后台更新失败!" + time);
			}
		}

		super.onRestart();
	}

	@Override
	protected void onResume() {
		System.out.println("TimerActivity:onResume");
		super.onResume();
	}

	/**
	 * activity 销毁时，若开始了计时，及时保存好最后的时间
	 */
	@Override
	protected void onDestroy() {
		System.out.println("TimerActivity:onDestroy");
		if (isStart) {
			if (countDown != null) {
				// 紧急处理，保存数据，防止数据丢失
				LocalNoteDB db2 = new LocalNoteDB(TimerActivity.this);

				boolean updateDate = db2.updateDate(dataBean,
						String.valueOf(countDown.getTime() * 1000));
				if (updateDate) {
					System.out.println("更新成功！");
				} else {
					System.out.println("更新失败!");
				}
				countDown.stopCount(); // 停止计时
			}
		}

		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// Toast.makeText(this, "退回键", Toast.LENGTH_SHORT).show();
			if (isStart) {
				showHintDailog();
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退回键
	 */
	private void showHintDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("提示");
		builder.setMessage("此时退出将会停止倒计时，是否继续退出？");
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				return;
			}
		});

		builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				isStart = false;

				if (countDown != null) {
					countDown.stopCount();
				}
				finish();
			}
		});
		builder.show();
	}

	/**
	 * Vpager的自定义适配器
	 * 
	 * @author lowly_pc
	 *
	 */
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return contentList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ContentBasePager contentBasePager = contentList.get(position);
			container.addView(contentBasePager.mRootView);
			return contentBasePager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}
	}
}
