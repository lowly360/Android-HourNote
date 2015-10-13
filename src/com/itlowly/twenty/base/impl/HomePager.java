package com.itlowly.twenty.base.impl;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.AddDataActivity;
import com.itlowly.twenty.activity.DetailActivity;
import com.itlowly.twenty.activity.HomeActivity;
import com.itlowly.twenty.activity.TimerActivity;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.utils.LongToTime;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 主页的实现类
 * 
 * @author Administrator
 * 
 */
public class HomePager extends ContentBasePager implements OnClickListener {

	private ImageButton btn_leftmenu;
	private ImageButton btn_setting;
	private ImageButton btn_addnew;
	private ListView lv_tag;
	private ListView lv_content;

	public static String mCurrenerTag;

	private ArrayList<String> mTagList;
	private MyTagApater tagApater;

	private ArrayList<DataBean> mDataList;
	private MyContentAdapter contentAdapter;
	private Context context;
	private LocalNoteDB noteDB;

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		mRootView = View.inflate(mActivity, R.layout.content_home_pager, null);

		// imageButtong 实例化和绑定监听
		btn_leftmenu = (ImageButton) mRootView.findViewById(R.id.btn_leftmenu);
		btn_setting = (ImageButton) mRootView.findViewById(R.id.btn_setting);
		btn_addnew = (ImageButton) mRootView.findViewById(R.id.btn_addnew);

		btn_leftmenu.setOnClickListener(this);
		btn_setting.setOnClickListener(this);
		btn_addnew.setOnClickListener(this);

		lv_tag = (ListView) mRootView.findViewById(R.id.lv_tag);
		lv_content = (ListView) mRootView.findViewById(R.id.lv_content);

		return mRootView;
	}

	/**
	 * 初始化数据,设置adpater等
	 */
	@Override
	public void initData() {
		context = mActivity.getBaseContext();

		noteDB = new LocalNoteDB(context);
		mTagList = new ArrayList<String>();

		mTagList = noteDB.getAllTag();

		if (mTagList.size() <= 0) {
			System.out.println("数据库中没有保存任何标签,自动添加标签");
			noteDB.addTag("学习");
			noteDB.addTag("备忘");

			mTagList = noteDB.getAllTag();
		}

		if (mTagList.size() != 0) {// 设置tag的适配器
			if (tagApater == null) {
				tagApater = new MyTagApater();
				lv_tag.setAdapter(tagApater);
				lv_tag.addHeaderView(View.inflate(mActivity,
						R.layout.listview_tag_add, null));
			}

			tagApater.notifyDataSetChanged();

		}

		mCurrenerTag = mTagList.get(0);// 设置默认标签为第一个标签

		// System.out.println("____" + mTagList.get(0));

		// 读取默认的标签页
		mDataList = noteDB.findAllInType(mCurrenerTag);

		// 获取当前时间，并转化为string进行存储
		Date date = new Date();
		long dateLong = date.getTime();

		if (mDataList.size() == 0) {
			noteDB.addData("背六级单词", "坚持每天背30~40个单词", String.valueOf(dateLong),
					"72000000", "学习", "1");
			noteDB.addData("了解Git", "有空xxxxxxxxxx????",
					String.valueOf(dateLong), "72000000", "学习", "1");
			mDataList = noteDB.findAllInType(mCurrenerTag);
		}

		noteDB.addData("备忘test", "XXXXXXXXXXXXXXXXXXXXXXXX",
				String.valueOf(dateLong), "0", "备忘", "0");

		mDataList = noteDB.findAllInType(mCurrenerTag);

		if (mDataList.size() > 0) {
			if (contentAdapter == null) {
				contentAdapter = new MyContentAdapter();
				lv_content.setAdapter(contentAdapter);
			}

			contentAdapter.notifyDataSetChanged();
		}

		// 一下是设置各listview的点击事件
		lv_tag.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position - 1 != mTagList.size() && position != 0) { // 不是点击了添加标签
					ImageView iView = (ImageView) view
							.findViewById(R.id.iv_tag_delete);
					iView.setVisibility(View.GONE);

					mCurrenerTag = mTagList.get(position - 1);

					tagApater.notifyDataSetChanged();

					setCurrenerContent(mCurrenerTag);

				} else if (position == 0) {
					// 跳出添加标签提示框
					showDialogToAddTag();
				}
			}
		});

		// 长按时间lv_tag
		lv_tag.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position - 1 < mTagList.size() && position != 0) {
					ImageView iView = (ImageView) view
							.findViewById(R.id.iv_tag_delete);
					iView.setVisibility(View.VISIBLE);

				}

				return true;
			}
		});

		// lv_content点击事件
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				DataBean object = (DataBean) lv_content
						.getItemAtPosition(position);

				if (object.getIsTwenty().equals("1")) { // 判断是否为20h倒计时备忘
					// 是则跳转到倒计时详情页面
					Intent intent = new Intent();
					intent.setClass(mActivity, TimerActivity.class);

					intent.putExtra("title", object.getTitle());
					intent.putExtra("type", object.getType());

					mActivity.startActivity(intent);
				} else {
					// 跳转到详情页面
					Intent intent = new Intent();
					intent.setClass(mActivity, DetailActivity.class);

					intent.putExtra("title", object.getTitle());
					intent.putExtra("type", object.getType());

					mActivity.startActivity(intent);
				}

			}

		});

		// 数据listview长按事件处理
		lv_content.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				DataBean object = (DataBean) lv_content
						.getItemAtPosition(position);

				showPopMenu(view, object);

				return true;
			}
		});

		if (tagApater != null) {
			tagApater.notifyDataSetChanged();
		}

		if (contentAdapter != null) {
			contentAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 弹出lv_content的条目菜单
	 * 
	 * @param object
	 */
	protected void showPopMenu(View view, final DataBean object) {
		final PopupMenu popupMenu = new PopupMenu(mActivity, view);
		MenuInflater inflater = popupMenu.getMenuInflater();
		inflater.inflate(R.menu.main, popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.detail: // 详情

					// 跳转到详情页面
					Intent intent = new Intent();
					intent.setClass(mActivity, DetailActivity.class);

					intent.putExtra("title", object.getTitle());
					intent.putExtra("type", object.getType());

					mActivity.startActivity(intent);

					break;

				case R.id.delete: // 删除
					LocalNoteDB db = new LocalNoteDB(mActivity);

					if (db.deleteData(object.getTitle(), object.getType())) {
						Toast.makeText(mActivity, "删除成功！", Toast.LENGTH_SHORT)
								.show();
						updateDate();
					} else {
						Toast.makeText(mActivity, "删除失败！", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				}
				popupMenu.dismiss();
				return true;
			}
		});

		popupMenu.show();

	}

	/**
	 * 弹出添加标签的对话框
	 */
	protected void showDialogToAddTag() {

		final Dialog dialog = new Dialog(mActivity, R.style.CustomDialog);

		View view = View.inflate(mActivity, R.layout.dialog_add_tag, null);

		ImageView iv_ok = (ImageView) view.findViewById(R.id.iv_ok);
		ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);

		final EditText et_tagname = (EditText) view
				.findViewById(R.id.et_tagname);

		iv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		iv_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tagName = et_tagname.getText().toString();

				if (!TextUtils.isEmpty(tagName)) {
					if (noteDB.findTag(tagName)) {
						Toast.makeText(mActivity, "已存在此标签!请重新输入",
								Toast.LENGTH_SHORT).show();
						et_tagname.setText("");
					} else {
						noteDB.addTag(tagName);

						mTagList.clear();

						mTagList = noteDB.getAllTag();

						if (tagApater != null) {
							tagApater.notifyDataSetChanged();
						}

						dialog.dismiss();
					}
				} else {
					Toast.makeText(mActivity, "请输入标签名字", Toast.LENGTH_SHORT)
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

	/**
	 * 刷新lv_content的内容
	 * 
	 * @param type
	 */
	public void setCurrenerContent(String type) {

		mDataList.clear();
		LocalNoteDB localNoteDB = new LocalNoteDB(context);

		mDataList = localNoteDB.findAllInType(type);
		if (mDataList.size() == 0) {
			Toast.makeText(mActivity, "此标签没有数据", Toast.LENGTH_SHORT).show();
		}

		if (contentAdapter != null) {
			contentAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 按钮点击事件处理
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_leftmenu: // 点击,打开或关闭侧边栏

			HomeActivity mainUI = (HomeActivity) mActivity;
			SlidingMenu slidingMenu = mainUI.getSlidingMenu();

			slidingMenu.toggle();

			break;
		case R.id.btn_setting:
			Toast.makeText(mActivity, "你点击了btn_setting", Toast.LENGTH_SHORT)
					.show();

			break;
		case R.id.btn_addnew:
			Intent intent = new Intent(mActivity, AddDataActivity.class);
			intent.putExtra("mCurrenerTag", mCurrenerTag);
			mActivity.startActivity(intent);

			break;

		}
	}

	/**
	 * tag的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class MyTagApater extends BaseAdapter {

		@Override
		public int getCount() {

			return mTagList.size();
		}

		@Override
		public String getItem(int position) {
			return mTagList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;

			if (position < mTagList.size()) {
				view = View
						.inflate(mActivity, R.layout.listview_tag_item, null);

				TextView tagName = (TextView) view
						.findViewById(R.id.tv_tagName);

				ImageView iv_delete = (ImageView) view
						.findViewById(R.id.iv_tag_delete);

				final String tagnameString = mTagList.get(position);

				tagName.setText(tagnameString);

				if (mTagList.get(position).equals(mCurrenerTag)) {
					view.setBackgroundColor(mActivity.getResources().getColor(
							R.color.tag_chosen));
					view.setAlpha(0.8f);
				} else {
					view.setBackground(null);
				}

				iv_delete.setOnClickListener(new OnClickListener() { // 删除事件
							@Override
							public void onClick(View v) {

								noteDB.deleteTag(tagnameString);
								Toast.makeText(mActivity,
										"已经删除:" + tagnameString,
										Toast.LENGTH_SHORT).show();
								// 删除此标签的数据 暂不处理,先留在数据库中
								mTagList.remove(getItem(position));
								tagApater.notifyDataSetChanged();
							}
						});

				return view;

			} else {
				// view = View.inflate(mActivity, R.layout.listview_tag_add,
				// null);

				return view;
			}
		}

	}

	class MyContentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public DataBean getItem(int position) {
			return mDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mActivity,
						R.layout.listview_content_item, null);
				holder = new ViewHolder();
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_content_title);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_content_time);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.tv_content_content);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			DataBean item = getItem(position);

			holder.tv_title.setText(item.getTitle());
			if (item.getIsTwenty().equals("0")) { // 判断是否为20H计划笔记
				holder.tv_title.setTextColor(mActivity.getResources().getColor(
						R.color.title_not));

				holder.tv_time.setText("无剩余时间");
			} else {
				holder.tv_title.setTextColor(mActivity.getResources().getColor(
						R.color.title_yes));

				String time = item.getTime();

				long timeLong = Long.valueOf(time);

				String timeString = LongToTime.getTime(timeLong);// 获取剩余时间

				holder.tv_time.setText(timeString);
			}

			holder.tv_content.setText(item.getContent());

			return convertView;
		}

	}

	static class ViewHolder {
		TextView tv_title;
		TextView tv_time;
		TextView tv_content;
	}

	/**
	 * 方便activity更新显示内容，在HomeActivity中调用
	 */
	public void updateDate() {
		setCurrenerContent(mCurrenerTag);
	}

	/**
	 * 更新homepager中所有数据，用户与本地模式切换中使用
	 */
	public void UpdateAll(){
		mTagList.clear();
		mTagList = new ArrayList<String>();

		LocalNoteDB newDB = new LocalNoteDB(context);
		
		mTagList = newDB.getAllTag();
		tagApater.notifyDataSetChanged();

		if (mTagList.size()!= 0) {
			mCurrenerTag = mTagList.get(0);
		}else {
			//Toast.makeText(context, "更新失败！！1", 0).show();
		}
		

		// 读取默认的标签页
		mDataList.clear();
		mDataList = newDB.findAllInType(mCurrenerTag);
		
		if (mDataList.size()!=0) {
			contentAdapter.notifyDataSetChanged();
		}else{
			//Toast.makeText(context, "更新失败！！1", 0).show();
		}
		
		//Toast.makeText(context, "更新成功！！1", 0).show();
	}

}
