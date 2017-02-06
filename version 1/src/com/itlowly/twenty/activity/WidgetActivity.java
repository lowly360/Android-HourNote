package com.itlowly.twenty.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itlowly.twenty.R;
import com.itlowly.twenty.db.LocalNoteDB;

/**
 * 用于桌面小控件，弹出对话框来选择当前标签
 * 
 * @author lowly_pc
 *
 */
public class WidgetActivity extends Activity implements OnClickListener {
	private static final String BROADCAST_TAG = "com.itlowly.twenty.action.CHANCE_TAG";
	private LinearLayout ll_widget_dialog;
	private String type;
	private ArrayList<String> allTag;
	private SharedPreferences mPre;
	private float x;
	private float y;
	private int width;
	private int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initUI();

		initData();

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		x = ll_widget_dialog.getX();
		y = ll_widget_dialog.getY();

		width = ll_widget_dialog.getWidth();
		height = ll_widget_dialog.getHeight();

		super.onWindowFocusChanged(hasFocus);
	}

	private void initUI() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_widget);

		ll_widget_dialog = (LinearLayout) findViewById(R.id.ll_widget_dialog);
	}

	private void initData() {

		mPre = getSharedPreferences("config", Context.MODE_PRIVATE);

		type = mPre.getString("mTag", "学习");

		LocalNoteDB db = new LocalNoteDB(this);

		allTag = db.getAllTag();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		params.setMargins(10, 2, 10, 2);

		params.gravity = Gravity.CENTER;

		for (int i = 0; i < allTag.size(); i++) {

			final TextView textView = new TextView(this);

			textView.setText(allTag.get(i));

			if (allTag.get(i).equals(type)) {
				textView.setTextColor(this.getResources().getColor(
						R.color.title_not));
			} else {
				textView.setTextColor(Color.GRAY);
			}

			textView.setGravity(Gravity.CENTER);

			textView.setTextSize(25);

			textView.setLayoutParams(params);

			textView.setOnClickListener(this);

			// textView.setOnClickListener(new OnClickListener() {

			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			//
			//
			// Intent intent = new Intent();
			// intent.setAction(BROADCAST_TAG);
			// intent.putExtra("type", textView.getText().toString());
			// sendBroadcast(intent);
			// System.out.println("发送了广播+"+textView.getText().toString());
			// finish();
			// }
			// });

			ll_widget_dialog.addView(textView);

		}

	}

	@Override
	public void onClick(View v) {

		if (v instanceof TextView) {
			TextView textView = (TextView) v;

			Intent intent = new Intent();
			intent.setAction(BROADCAST_TAG);
			intent.putExtra("type", textView.getText().toString());
			mPre.edit()
					.putString("mCurrenerTag", textView.getText().toString())
					.commit();
			sendBroadcast(intent);
			System.out.println("发送了广播+" + textView.getText().toString());
			finish();
		} else {
			finish();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			float x2 = event.getRawX();
			float y2 = event.getRawY();

			System.out.println("downX" + x2 + " downY" + y2 + "x&width" + x
					+ width + "  y&height" + y + height);
			// System.out.println("left"+rect.left+" right"+rect.right+" top"+rect.top+" bottom"+rect.bottom);

			if (x < x2 && x2 < x + width) {
				if (y < y2 && y < y + height) {

				} else {
					finish();
				}
			}

			break;

		}
		return super.onTouchEvent(event);
	}
}
