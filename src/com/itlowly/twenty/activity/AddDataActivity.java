package com.itlowly.twenty.activity;

import java.util.ArrayList;

import com.itlowly.twenty.R;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.db.dao.LocalNoteOperHelper;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 添加笔记的activity
 * @author lowly_pc
 *
 */
public class AddDataActivity extends Activity implements OnClickListener {
	private ImageButton btn_leftmenu;
	private ImageButton btn_setting;
	private EditText et_add_title;
	private EditText et_add_content;
	private Button btn_setTag;
	private ImageButton ib_add_ok;
	private ImageButton ib_add_cancel;

	private String mCurrenerTag ;
	private int mCurrenerItem = 0;
	
	
	private CheckBox cb_add_isTwenty;
	private ArrayList<String> allTag;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LocalNoteDB db = new LocalNoteDB(this);

		allTag = db.getAllTag();
		initView();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_data);
		btn_leftmenu = (ImageButton) findViewById(R.id.btn_leftmenu);
		btn_setting = (ImageButton) findViewById(R.id.btn_setting);

		et_add_title = (EditText) findViewById(R.id.et_add_title);
		et_add_content = (EditText) findViewById(R.id.et_add_content);
		
		et_add_content.clearFocus();//默认去掉焦点
		et_add_title.clearFocus();

		btn_setTag = (Button) findViewById(R.id.btn_setTag);
		
		btn_setTag.requestFocus();
		
		cb_add_isTwenty = (CheckBox) findViewById(R.id.cb_add_isTwenty);

		ib_add_ok = (ImageButton) findViewById(R.id.ib_add_ok);
		ib_add_cancel = (ImageButton) findViewById(R.id.ib_add_cancel);
		
		mCurrenerTag = getIntent().getStringExtra("mCurrenerTag");
		
		if (mCurrenerTag == null) {
			mCurrenerTag = allTag.get(0);
		}
		
		btn_setTag.setText("设置标签：当前为["+mCurrenerTag+"]");

		initListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		ib_add_cancel.setOnClickListener(this);
		ib_add_ok.setOnClickListener(this);
		btn_leftmenu.setOnClickListener(this);
		btn_setTag.setOnClickListener(this);
	}

	/**
	 * 点击事件处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_add_cancel:
			finish();
			break;

		case R.id.ib_add_ok:
			String mTitleString = et_add_title.getText().toString();
			String mContentString = et_add_content.getText().toString();

			if (TextUtils.isEmpty(mTitleString)) {
				Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
				break;
			}

			if (TextUtils.isEmpty(mContentString)) {
				Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
				break;
			}
			
			boolean addData = false;
			if (cb_add_isTwenty.isChecked()) {
				LocalNoteDB db = new LocalNoteDB(this);
				addData = db.addData(mTitleString, mContentString,
							String.valueOf(System.currentTimeMillis()), "72000000",
							mCurrenerTag,"1");
			}else {
				LocalNoteDB db = new LocalNoteDB(this);
				addData = db.addData(mTitleString, mContentString,
							String.valueOf(System.currentTimeMillis()), "0",
							mCurrenerTag,"0");
			}

			
			if (addData) {
				Toast.makeText(this, "成功添加 "+mTitleString, Toast.LENGTH_SHORT).show();
				finish();
			}	else {
				Toast.makeText(this, "哎呀，添加失败，该数据已存在 "+mTitleString, Toast.LENGTH_SHORT).show();
			}
				
			

			break;
		case R.id.btn_leftmenu: // 功能等同返回按钮
			finish();
			break;

		case R.id.btn_setTag:
			choceTag();
			break;

		default:
			break;
		}
	}

	/**
	 * 显示设置标签的对话框
	 */
	private void choceTag() {
		

		final String[] tagList = new String[allTag.size()]; 
		allTag.toArray(tagList); //转化为string[]
		
		for (int i = 0; i < allTag.size(); i++) {
			if (allTag.get(i).equals(mCurrenerTag) ){
				mCurrenerItem = i;
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("选择标签");

		builder.setSingleChoiceItems(tagList,mCurrenerItem,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mCurrenerItem = which;
						mCurrenerTag = tagList[which];
					}
				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				btn_setTag.setText("设置标签：当前为["+mCurrenerTag+"]");
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.show();
	}
}
