package com.itlowly.twenty.activity;

import java.util.ArrayList;
import java.util.Date;

import com.itlowly.twenty.R;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.utils.LongToTime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity implements OnClickListener {
	private EditText et_edit_title;
	private EditText et_edit_content;
	private TextView tv_edit_tag;
	private CheckBox cb_edit_istwenty;
	private TextView tv_edit_time;
	private TextView tv_edit_date;
	private ImageButton btn_back;
	private Button btn_edit_editok;
	private String title;
	private String type;
	private DataBean dataBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		initData();
		initListener();

	}

	private void initUI() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit);

		et_edit_title = (EditText) findViewById(R.id.tv_edit_title);
		et_edit_content = (EditText) findViewById(R.id.tv_edit_content);

		et_edit_title.clearFocus();// 默认去掉焦点
		et_edit_content.clearFocus();

		tv_edit_tag = (TextView) findViewById(R.id.tv_edit_tag);
		cb_edit_istwenty = (CheckBox) findViewById(R.id.cb_edit_istwenty);

		tv_edit_tag.requestFocus(); // 让别的控件获取焦点使输入法默认不弹出

		tv_edit_time = (TextView) findViewById(R.id.tv_edit_time);
		tv_edit_date = (TextView) findViewById(R.id.tv_edit_date);

		btn_back = (ImageButton) findViewById(R.id.btn_back);

		btn_edit_editok = (Button) findViewById(R.id.btn_edit_editok);

	}

	private void initData() {

		// 从intent中获取数据
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		type = intent.getStringExtra("type");

		db = new LocalNoteDB(this);
		dataBean = db.getDataBean(title, type);

		mCurrenerTag = dataBean.getType();

		et_edit_title.setText(title);
		et_edit_content.setText(dataBean.getContent());

		tv_edit_tag.setText(dataBean.getType());

		if (dataBean.getIsTwenty().equals("1")) {
			cb_edit_istwenty.setChecked(true);
			String time = dataBean.getTime();
			tv_edit_time.setText(LongToTime.getTime(Long.valueOf(time)));
			et_edit_title.setTextColor(getResources().getColor(
					R.color.title_yes));

		} else {
			cb_edit_istwenty.setChecked(false);
			et_edit_title.setTextColor(getResources().getColor(
					R.color.title_not));
			tv_edit_time.setText("剩余时间：无");
		}

		String data = dataBean.getData();
		Date date = new Date(Long.valueOf(data));
		tv_edit_date.setText("最后修改：" + date.toLocaleString());

	}

	private void initListener() {
		btn_back.setOnClickListener(this);
		btn_edit_editok.setOnClickListener(this);
		tv_edit_tag.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();

			break;

		case R.id.tv_edit_tag:
			choceTag();
			break;

		case R.id.btn_edit_editok:
			String editTitle = et_edit_title.getText().toString();
			String editContent = et_edit_content.getText().toString();
			boolean isTwenty = cb_edit_istwenty.isChecked();
			String is;
			if (isTwenty) {
				is = "1";
			} else {
				is = "0";
			}
			Date date = new Date();
			long time = date.getTime();
			
			Intent data = new Intent();
			data.putExtra("title", editTitle);
			data.putExtra("type", mCurrenerTag);

			if (editTitle.equals(title)
					&& editContent.equals(dataBean.getContent())
					&& dataBean.getIsTwenty().equals(is)
					&& mCurrenerTag.equals(dataBean.getType())) {// 没有任何数据被修改就按了确定

				Toast.makeText(this, "什么都没改变哎！！", 0).show();

			} else {// 修改了数据
					// 判断修改什么而作出对应的处理
				if (!mCurrenerTag.equals(dataBean.getType())) { // 修改了tag类型，需要删除原来的，并加到新标签表中

					db.deleteData(title, type);

					String newtime = dataBean.getTime() ;
					
					if (is.equals(dataBean.getIsTwenty())) {
						setResult(1, data); // 没有修改倒计时类型
					} else {//修改了笔记是否倒计时
						if (is.equals("1")) {//由非计时改为计时
							newtime="72000000";
							setResult(4, data); 
						}else {
							newtime="0";
							setResult(3, data); //由计时改为非计时
						}
					}
					db.addData(editTitle, editContent, String.valueOf(time),
							newtime, mCurrenerTag, is);
					
					Toast.makeText(this, "修改成功", 0).show();
					finish();

				} else {// 没有修改标签类型，只需要更新表中的数据
					DataBean newDataBean = new DataBean();
					// 封装成新的bean
					newDataBean.setTitle(editTitle);
					newDataBean.setContent(editContent);
					newDataBean.setData(String.valueOf(time));
					
					newDataBean.setType(mCurrenerTag);
					newDataBean.setIsTwenty(is);


					if (is.equals(dataBean.getIsTwenty())) {
						setResult(1, data); // 没有修改倒计时类型
					} else {//修改了笔记是否倒计时
						if (is.equals("1")) {//由非计时改为计时
							
							newDataBean.setTime("72000000");
							
							setResult(4, data); 
						}else {
							newDataBean.setTime("0");
							setResult(3, data); //由计时改为非计时
						}
						
					}
					
					boolean updateData = db
							.updateData(title, type, newDataBean);

					if (updateData) {
						Toast.makeText(this, "修改成功", 0).show();
						finish();
					}else {
						setResult(2);  //失败结果码
						Toast.makeText(this, "修改失败", 0).show();
					}
				}
			}

			break;

		default:
			break;
		}
	}

	private String mCurrenerTag;
	private int mCurrenerItem = 0;
	private LocalNoteDB db;

	/**
	 * 显示设置标签的对话框
	 */
	private void choceTag() {
		LocalNoteDB db = new LocalNoteDB(this);

		ArrayList<String> allTag = db.getAllTag(); // 获取数据库中的标签列表

		final String[] tagList = new String[allTag.size()];
		allTag.toArray(tagList); // 转化为string[]

		for (int i = 0; i < allTag.size(); i++) {
			if (allTag.get(i).equals(dataBean.getType())) {
				mCurrenerItem = i;
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("选择标签");

		builder.setSingleChoiceItems(tagList, mCurrenerItem,
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
				tv_edit_tag.setText(mCurrenerTag);
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
