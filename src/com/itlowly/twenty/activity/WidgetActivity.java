package com.itlowly.twenty.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itlowly.twenty.R;
import com.itlowly.twenty.db.LocalNoteDB;

/**
 * 用于桌面小控件，弹出对话框来选择当前标签
 * @author lowly_pc
 *
 */
public class WidgetActivity extends Activity {
	private static final String BROADCAST_TAG = "com.itlowly.twenty.action.CHANCE_TAG";
	private LinearLayout ll_widget_dialog;
	private String type;
	private ArrayList<String> allTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		initUI();
		
		initData();
		
		super.onCreate(savedInstanceState);
	}

	

	private void initUI() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_widget);
		
		ll_widget_dialog = (LinearLayout) findViewById(R.id.ll_widget_dialog);
	}
	
	private void initData() {
		type = getIntent().getStringExtra("type");
		
		LocalNoteDB db = new LocalNoteDB(this);
		
		allTag = db.getAllTag();
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,     
                LinearLayout.LayoutParams.WRAP_CONTENT);
		
		params.setMargins(10, 2, 10, 2);
		
		params.gravity =Gravity.CENTER;
		
		for (int i = 0; i < allTag.size(); i++) {
			
			final TextView textView = new TextView(this);
			
			
			textView.setText(allTag.get(i));
			
			if (allTag.get(i).equals(type)) {
				textView.setTextColor(this.getResources().getColor(R.color.title_not));
			}else {
				textView.setTextColor(Color.GRAY);
			}
			
			textView.setGravity(Gravity.CENTER);
			
			textView.setTextSize(25);
			
			textView.setLayoutParams(params);
			
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					Intent intent = new Intent();
					intent.setAction(BROADCAST_TAG);
					intent.putExtra("type", textView.getText().toString());
					sendBroadcast(intent);
					System.out.println("发送了广播+"+textView.getText().toString());
					finish();
				}
			});
			
			ll_widget_dialog.addView(textView);
			
		}
		
	}
	
}
