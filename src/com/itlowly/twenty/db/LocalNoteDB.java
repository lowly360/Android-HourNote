package com.itlowly.twenty.db;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.widget.Toast;

import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.bean.HistoryDataBean;
import com.itlowly.twenty.db.dao.LocalNoteOperHelper;

public class LocalNoteDB {
	private LocalNoteOperHelper helper;
	private Context context;
	private static final String BROADCAST_TAG = "com.itlowly.twenty.action.CHANCE_TAG";
	private static final Intent INTENT = new Intent(BROADCAST_TAG);
	private SharedPreferences mPre;

	public LocalNoteDB(Context context) {
		helper = new LocalNoteOperHelper(context);
		this.context = context;
		mPre = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	/**
	 * 添加标签
	 * 
	 * @param tagName
	 * @param i
	 */
	public void addTag(String tagName, int i) {
		if (findTag(tagName)) {
			return;
		} else {
			//获取加密数据库
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("type", tagName);
			db.insert("tag", null, values);

			if (i != 0) {
				// 添加历史纪录
				values.clear();
				values.put("historyType", "AddTag");
				long timeMillis = SystemClock.currentThreadTimeMillis();
				values.put("data", String.valueOf(timeMillis));
				values.put("tagName", tagName);
				db.insert("history", null, values);
			}

			// 发送广播通知桌面小控件更新ui
			context.sendBroadcast(INTENT);

			db.close();
		}
	}

	/**
	 * 判断是否存在标签
	 * 
	 * @param tagName
	 * @return
	 */
	public boolean findTag(String tagName) {
		SQLiteDatabase db = helper.getReadableDatabase();
		
		Cursor cursor = db.query("tag", null, "type = ?",
				new String[] { tagName }, null, null, null);

		if (cursor.moveToNext()) {
			System.out.println("已经拥有该tag" + cursor.getColumnName(0));
			return true;
		}

		cursor.close();
		db.close();
		return false;
	}

	/**
	 * 删除标签
	 * 
	 * @param tagName
	 * @param i
	 */
	public void deleteTag(String tagName, int i) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("tag", "type = ?", new String[] { tagName });

		if (i != 0) {
			// 添加历史纪录
			ContentValues values = new ContentValues();
			values.put("historyType", "DeleteTag");
			values.put("tagName", tagName);
			long timeMillis = SystemClock.currentThreadTimeMillis();
			values.put("data", String.valueOf(timeMillis));
			db.insert("history", null, values);
		}

		// 发送广播通知桌面小控件更新ui
		context.sendBroadcast(INTENT);

		db.close();
	}

	/**
	 * 获取数据库中所有的tag数据列表
	 * 
	 * @return
	 */
	public ArrayList<String> getAllTag() {
		ArrayList<String> tagList = new ArrayList<String>();

		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor cursor = db.rawQuery("select type from tag", null);

		while (cursor.moveToNext()) {
			tagList.add(cursor.getString(0));
		}

		cursor.close();
		db.close();

		return tagList;
	};

	/**
	 * 获取数据库中的某一数据
	 * 
	 * @param title
	 *            标题
	 * @param type
	 *            标签类型
	 * @return
	 */
	public DataBean getDataBean(String title, String type) {
		DataBean bean = new DataBean();
		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor query = db.query("data", null, "title = ? and type = ?",
				new String[] { title, type }, null, null, null);

		if (query.moveToNext()) {

			bean.setTitle(query.getString(1));
			bean.setContent(query.getString(2));
			bean.setData(query.getString(3));
			bean.setTime(query.getString(4));
			bean.setType(query.getString(5));
			bean.setIsTwenty(query.getString(6));
		} else {
			Toast.makeText(context, "获取不到对应的数据！ From LocalNoteDB",
					Toast.LENGTH_SHORT).show();
		}
		query.close();
		db.close();
		return bean;
	}

	/**
	 * 添加data数据到数据库中
	 * 
	 * @param title
	 * @param content
	 * @param data
	 * @param time
	 * @param type
	 * @param i
	 *            用来代表是否为历史纪录，1的话需要添加历史纪录，0不需要添加历史纪录
	 */
	public boolean addData(String title, String content, String data,
			String time, String type, String isTwenty, int i) {
		if (findDateWithTag(title, type)) { // 如果发现同一tag内存在相同的title则不添加进数据库中
			return false;
		} else {
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("title", title);
			values.put("content", content);
			values.put("data", data);
			values.put("time", time);
			values.put("type", type);
			values.put("isTwenty", isTwenty);

			db.insert("data", null, values);

			// 发送广播通知桌面小控件更新ui
			context.sendBroadcast(INTENT);

			if (i != 0) {
				// 添加历史纪录
				values.put("historyType", "AddData");
				db.insert("history", null, values);
			}

			values.clear();

			db.close();

			return true;
		}
	};

	public boolean findFromData(String title) {

		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor query = db.query("data", null, "title = ?",
				new String[] { title }, null, null, null);

		if (query.moveToNext()) {
			query.close();
			db.close();
			return true;
		} else {
			query.close();
			db.close();
			return false;
		}
	}

	/**
	 * 通过tag类型和title来查找数据库中是否存在
	 * 
	 * @param title
	 * @param tag
	 * @return
	 */
	public boolean findDateWithTag(String title, String tag) {
		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor query = db.query("data", null, "title = ? and type = ?",
				new String[] { title, tag }, null, null, null);

		if (query.moveToNext()) {
			query.close();
			db.close();
			return true;
		} else {
			query.close();
			db.close();
			return false;
		}
	}

	/**
	 * 修改数据库中的data中的剩余时间
	 * 
	 * @param title
	 * @param tag
	 * @param newTime
	 * @return
	 */
	public boolean updateDate(DataBean dataBean, String newTime) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", dataBean.getTitle());
		values.put("content", dataBean.getContent());
		values.put("time", newTime);
		values.put("type", dataBean.getType());
		values.put("data", dataBean.getData());
		values.put("isTwenty", dataBean.getIsTwenty());
		int update = db.update("data", values, "title = ? and type = ?",
				new String[] { dataBean.getTitle(), dataBean.getType() });

		if (update != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新数据库中的数据
	 * 
	 * @param title
	 * @param type
	 * @param dataBean
	 * @return
	 */
	public boolean updateData(String title, String type, DataBean dataBean) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", dataBean.getTitle());
		values.put("content", dataBean.getContent());
		values.put("time", dataBean.getTime());
		values.put("type", dataBean.getType());
		values.put("data", dataBean.getData());
		values.put("isTwenty", dataBean.getIsTwenty());
		int update = db.update("data", values, "title = ? and type = ?",
				new String[] { title, type });

		if (update != 0) {
			// 发送广播通知桌面小控件更新ui
			// 添加历史纪录
			values.put("historyType", "EditData");
			db.insert("history", null, values);

			db.close();

			context.sendBroadcast(INTENT);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * 按标签查找对应的databean
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<DataBean> findAllInType(String type) {
		ArrayList<DataBean> mDataList = new ArrayList<DataBean>();

		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor rawQuery = db.rawQuery("select * from data where type = ?",
				new String[] { type });
		while (rawQuery.moveToNext()) {
			DataBean dataBean = new DataBean();

			dataBean.setTitle(rawQuery.getString(1));
			dataBean.setContent(rawQuery.getString(2));
			dataBean.setData(rawQuery.getString(3));
			dataBean.setTime(rawQuery.getString(4));
			dataBean.setType(rawQuery.getString(5));
			dataBean.setIsTwenty(rawQuery.getString(6));

			mDataList.add(dataBean);
		}
		System.out.println("最后的个数为:" + mDataList.size());
		rawQuery.close();
		db.close();

		return mDataList;
	};

	/**
	 * 获取所有历史纪录
	 * 
	 * @param title
	 * @return
	 */
	public ArrayList<HistoryDataBean> getDataBeanFromHistory() {
		ArrayList<HistoryDataBean> mDataList = new ArrayList<HistoryDataBean>();

		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery("select * from history", null);

		while (cursor.moveToNext()) {
			HistoryDataBean dataBean = new HistoryDataBean();

			dataBean.setHistoryType(cursor.getString(1));
			dataBean.setTagName(cursor.getString(2));
			dataBean.setTitle(cursor.getString(3));
			dataBean.setContent(cursor.getString(4));
			dataBean.setData(cursor.getString(5));
			dataBean.setTime(cursor.getString(6));
			dataBean.setType(cursor.getString(7));
			dataBean.setIsTwenty(cursor.getString(8));

			System.out.println("" + cursor.getString(1));
			mDataList.add(dataBean);
		}
		System.out.println("size:" + mDataList.size());
		cursor.close();
		db.close();
		return mDataList;
	}

	/**
	 * 删除某条历史纪录
	 * 
	 * @param historytype
	 * @param tagName
	 * @param title
	 * @return
	 */
	public boolean deleteHistory(String historytype, String tagName,
			String title) {
		SQLiteDatabase db = helper.getWritableDatabase();

		System.out.println("db收到数据为： " + historytype + tagName + title);

		int delete = 0;

		if (historytype == null && tagName == null && title == null) {
			// 清除所有历史纪录
			db.delete("history", null, null);
		}

		if (tagName == null) {
			// 说明要删除的是title
			delete = db.delete("history", "historytype = ? and title = ?",
					new String[] { historytype, title });
		} else if (title == null) {
			delete = db.delete("history", "historytype = ? and tagName = ?",
					new String[] { historytype, tagName });
		}

		System.out.println("db 删除数据为：" + delete);

		if (delete != 0) {
			return true;
		}

		return false;

	}

	public boolean deleteAllData() {
		// 获取要删除的数据bean

		SharedPreferences mPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);

		String pathString = context.getDatabasePath(mPre.getString(
				"LocateUser", "LocalNote")) + ".db";

		System.out.println(pathString);

		File dbFile = new File(pathString);

		return dbFile.delete();

	}

	/**
	 * 删除数据库对应的数据
	 * 
	 * @param title
	 * @param type
	 * @param i
	 */
	public boolean deleteData(String title, String type, int i) {
		// 获取要删除的数据bean
		DataBean dataBean = getDataBean(title, type);

		SQLiteDatabase db = helper.getWritableDatabase();

		int delete = db.delete("data", "title = ? and type = ?", new String[] {
				title, type });

		if (delete != 0) {

			if (i != 0) {
				// 添加历史纪录
				ContentValues values = new ContentValues();

				values.put("historyType", "DeleteData");
				values.put("title", dataBean.getTitle());
				values.put("content", dataBean.getContent());
				values.put("time", dataBean.getTime());
				values.put("type", dataBean.getType());
				values.put("data", dataBean.getData());
				values.put("isTwenty", dataBean.getIsTwenty());

				db.insert("history", null, values);
			}

			db.close();

			// 发送广播通知桌面小控件更新ui
			context.sendBroadcast(INTENT);

			return true;
		} else {
			Toast.makeText(context, "删除该数据失败！From LocalNoteDB",
					Toast.LENGTH_SHORT).show();
			db.close();
			return false;
		}
	}
}
