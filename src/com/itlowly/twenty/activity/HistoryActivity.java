package com.itlowly.twenty.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itlowly.twenty.R;
import com.itlowly.twenty.bean.HistoryDataBean;
import com.itlowly.twenty.db.LocalNoteDB;

public class HistoryActivity extends Activity {
	private Context mContext;
	private ListView lv_history;
	private ImageButton btn_history_menu;
	private ArrayList<HistoryDataBean> dataList;
	private int color_history_add;
	private int color_history_edit;
	private int color_history_delete;
	private int color_history_title;
	private int color_history_tag;
	private Button btn_history_clear;
	private LocalNoteDB db;
	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		initUI();
		initData();

		InitListener();
		super.onCreate(savedInstanceState);
	}

	private void initUI() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_history);

		lv_history = (ListView) findViewById(R.id.lv_history);
		btn_history_menu = (ImageButton) findViewById(R.id.btn_history_menu);
		btn_history_clear = (Button) findViewById(R.id.btn_history_clear);
	}

	private void initData() {
		color_history_add = this.getResources().getColor(R.color.history_add);
		color_history_edit = this.getResources().getColor(R.color.history_edit);
		color_history_delete = this.getResources().getColor(
				R.color.history_delete);
		color_history_title = this.getResources().getColor(R.color.title_yes);
		color_history_tag = this.getResources().getColor(R.color.title_not);

		db = new LocalNoteDB(this);

		dataList = db.getDataBeanFromHistory();
		myAdapter = new MyAdapter();
		lv_history.setAdapter(myAdapter);

	}

	private void InitListener() {
		// 返回键处理
		btn_history_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 清楚所有历史纪录
		btn_history_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				db.deleteHistory(null, null, null);
				Toast.makeText(HistoryActivity.this	, "清除完毕", 0).show();
				dataList.clear();
				if (myAdapter!=null) {
					myAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(dataList.size() - position - 1);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.listview_history_item, null);
				holder = new ViewHolder();
				holder.tv_history_item_type = (TextView) convertView
						.findViewById(R.id.tv_history_item_type);
				holder.tv_history_name = (TextView) convertView
						.findViewById(R.id.tv_history_name);
				holder.tv_history_time = (TextView) convertView
						.findViewById(R.id.tv_history_time);
				holder.tv_history_title = (TextView) convertView
						.findViewById(R.id.tv_history_title);
				holder.iv_history_undo = (ImageButton) convertView
						.findViewById(R.id.iv_history_undo);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final HistoryDataBean historyDataBean = dataList.get(dataList
					.size() - position - 1);

			final String historyType = historyDataBean.getHistoryType();
			String tagName = historyDataBean.getTagName();
			String title = historyDataBean.getTitle();
			String data = historyDataBean.getData();

			if (data == null) {
				data = String.valueOf(SystemClock.currentThreadTimeMillis());
			}

			Date timedata = new Date(Long.valueOf(data));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

			if (historyType.equals("AddTag")) {
				holder.tv_history_item_type.setText("增");
				holder.tv_history_name.setText("Tag");
				holder.tv_history_title.setText(tagName);
				holder.tv_history_title.setTextColor(color_history_tag);
				holder.tv_history_item_type.setTextColor(color_history_add);

			} else if (historyType.equals("AddData")) {

				holder.tv_history_item_type.setText("增");
				holder.tv_history_name.setText("Data");
				holder.tv_history_title.setText(title);
				holder.tv_history_title.setTextColor(color_history_title);
				holder.tv_history_item_type.setTextColor(color_history_add);

			} else if (historyType.equals("EditData")) {

				holder.tv_history_item_type.setText("改");
				holder.tv_history_name.setText("Data");
				holder.tv_history_title.setText(title);
				holder.tv_history_title.setTextColor(color_history_title);
				holder.tv_history_item_type.setTextColor(color_history_edit);

			} else if (historyType.equals("DeleteData")) {

				holder.tv_history_item_type.setText("删");
				holder.tv_history_name.setText("Data");
				holder.tv_history_title.setText(title);
				holder.tv_history_title.setTextColor(color_history_title);
				holder.tv_history_item_type.setTextColor(color_history_delete);

			} else if (historyType.equals("DeleteTag")) {
				holder.tv_history_item_type.setText("删");
				holder.tv_history_name.setText("Tag");
				holder.tv_history_title.setText(tagName);
				holder.tv_history_title.setTextColor(color_history_tag);
				holder.tv_history_item_type.setTextColor(color_history_delete);
			}

			holder.tv_history_time.setText("时间: "
					+ dateFormat.format(timedata));
			// holder.tv_history_time.setText(data);
			holder.iv_history_undo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 还原历史纪录处理
					if (historyType.equals("AddTag")) {
						// 删除便签
						System.out.println("删除便签"+historyDataBean.getTagName());
						db.deleteTag(historyDataBean.getTagName(),0);

					} else if (historyType.equals("DeleteTag")) {
						// 添加便签
						System.out.println("添加便签"+historyDataBean.getTagName());
						db.addTag(historyDataBean.getTagName(),0);

					} else if (historyType.equals("AddData")) {
						// 删除数据
						db.deleteData(historyDataBean.getTitle(),
								historyDataBean.getType(),0);

					} else if (historyType.equals("DeleteData")) {
						// 添加数据
						db.addData(historyDataBean.getTitle(),
								historyDataBean.getContent(),
								historyDataBean.getData(),
								historyDataBean.getTime(),
								historyDataBean.getType(),
								historyDataBean.getIsTwenty(),0);

					} else if (historyType.equals("EditData")) {
						// 先把现在的删除，在恢复历史纪录的数据

						boolean isDelete = db.deleteData(
								historyDataBean.getTitle(),
								historyDataBean.getType(),0);

						if (isDelete) {
							db.addData(historyDataBean.getTitle(),
									historyDataBean.getContent(),
									historyDataBean.getData(),
									historyDataBean.getTime(),
									historyDataBean.getType(),
									historyDataBean.getIsTwenty(),0);
							Toast.makeText(HistoryActivity.this, "恢复成功",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(HistoryActivity.this, "恢复失败",
									Toast.LENGTH_SHORT).show();
						}

					}

					// 删除该历史记录，并更新listview的数据
					boolean isDelete = db.deleteHistory(historyDataBean.getHistoryType(),
							historyDataBean.getTagName(),
							historyDataBean.getTitle());
					
					if (isDelete) {
						System.out.println("删除成功");
						dataList.remove(historyDataBean);
						if (myAdapter != null) {
							myAdapter.notifyDataSetChanged();
						}
					}else {
						System.out.println("删除失败");
					}
				}
			});

			return convertView;
		}

	}

	class ViewHolder {

		TextView tv_history_item_type;
		TextView tv_history_name;
		TextView tv_history_time;
		TextView tv_history_title;
		ImageButton iv_history_undo;

	}
}
