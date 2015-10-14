package com.itlowly.twenty.activity;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itlowly.twenty.R;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.fragment.TimerRightMenuFragment;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.utils.LongToTime;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class DetailActivity extends SlidingFragmentActivity {
	private TextView tv_detial_title;
	private TextView tv_detial_content;
	private TextView tv_detial_tag;
	private TextView tv_detial_istwenty;
	private Button btn_detial_edit;

	private TextView tv_detial_time;
	private TextView tv_detial_date;

	private String title;
	private String type;
	private DataBean dataBean;
	private ImageButton btn_back;
	private ImageButton btn_menu;
	private SlidingMenu slidingMenu;
	private TimerRightMenuFragment rightMenu;

	private int startX = 0;
	private int endX = 0;

	private final String FRAGMENT_RIGHT_MENU = "fl_right_menu";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();

		// 从intent中获取数据
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		type = intent.getStringExtra("type");

		// 根据标题，标签从数据库获取对应的数据databean
		LocalNoteDB db = new LocalNoteDB(this);
		dataBean = db.getDataBean(title, type);

		initData();
		initListener();
	}

	private void initUI() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail);

		setBehindContentView(R.layout.activity_home_behind);

		slidingMenu = getSlidingMenu();

		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		slidingMenu.setMode(SlidingMenu.RIGHT);

		System.out.println("dp = " + DensityUtils.px2dp(this, 240));

		slidingMenu.setBehindOffset((getWindowManager().getDefaultDisplay()
				.getWidth() / 3) * 2);// 设置预留屏幕宽度

		initFragment();

		tv_detial_title = (TextView) findViewById(R.id.tv_detial_title);
		tv_detial_content = (TextView) findViewById(R.id.tv_detial_content);
		tv_detial_tag = (TextView) findViewById(R.id.tv_detial_tag);
		tv_detial_istwenty = (TextView) findViewById(R.id.tv_detial_istwenty);

		tv_detial_time = (TextView) findViewById(R.id.tv_detial_time);
		tv_detial_date = (TextView) findViewById(R.id.tv_detial_date);

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_menu = (ImageButton) findViewById(R.id.btn_menu);

		btn_detial_edit = (Button) findViewById(R.id.btn_detial_edit);

	}

	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		rightMenu = new TimerRightMenuFragment();
		transaction.replace(R.id.fl_left_menu, rightMenu, FRAGMENT_RIGHT_MENU);
		transaction.commit(); // 提交事务
	}

	private void initData() {

		tv_detial_title.setText(title);
		tv_detial_content.setText(dataBean.getContent());

		tv_detial_tag.setText("标签:" + dataBean.getType());

		
		if (dataBean.getIsTwenty().equals("1")) {
			tv_detial_istwenty.setText("20H计时任务");
			String time = dataBean.getTime();
			tv_detial_time.setText(LongToTime.getTime(Long.valueOf(time)));
			tv_detial_title.setTextColor(getResources().getColor(
					R.color.title_yes));
		} else {
			tv_detial_istwenty.setText("非20H计时任务");
			tv_detial_title.setTextColor(getResources().getColor(
					R.color.title_not));
			tv_detial_time.setText("剩余时间：无");
		}

		String data = dataBean.getData();
		Date date = new Date(Long.valueOf(data));
		tv_detial_date.setText("最后修改：" + date.toLocaleString());

	}

	private void initListener() {
		// 跳转到修改页面
		btn_detial_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 是则跳转到修改页面
				Intent intent = new Intent();
				intent.setClass(DetailActivity.this, EditActivity.class);

				intent.putExtra("title", title);
				intent.putExtra("type", type);

				startActivityForResult(intent, 100);

			}
		});

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btn_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});

		// 设置侧边菜单关闭监听器，关闭的话，把图标详细内容隐藏
		slidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				rightMenu.hideText();
			}
		});

	}

	/**
	 * 重写触摸事件来实现，向左划显示图标详细内容
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (slidingMenu.isMenuShowing()) { // 判断右菜单是否开启，否则不做任何操作

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = (int) event.getX();
				break;
			case MotionEvent.ACTION_MOVE:

				endX = (int) event.getRawX();

				if ((startX - endX) >= 150) { // 显示图标信息
					rightMenu.setDefaultText();
				}
				return true;
			case MotionEvent.ACTION_UP:

				break;
			}

		} else {
			return super.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1: //修改成功
			title = data.getStringExtra("title");
			type = data.getStringExtra("type");
			initData(); //刷新界面ui数据
			
			break;
		case 2:
			
			break;
		case 4://由非计时改成计时类型，结束倒计时界面，启动详情界面
			title = data.getStringExtra("title");
			type = data.getStringExtra("type");
			
			Intent intent = new Intent();
			intent.putExtra("title", title);
			intent.putExtra("type", type);
			
			intent.setClass(this, TimerActivity.class);
			startActivity(intent);
			finish();
			break;

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
