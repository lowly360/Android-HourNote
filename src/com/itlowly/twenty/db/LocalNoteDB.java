package com.itlowly.twenty.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.dao.LocalNoteOperHelper;

public class LocalNoteDB {
	private LocalNoteOperHelper helper;

	public LocalNoteDB(Context context) {
		helper = new LocalNoteOperHelper(context);
	}

	/**
	 * 添加标签
	 * 
	 * @param tagName
	 */
	public void addTag(String tagName) {
		if (findTag(tagName)) {
			return;
		} else {
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("type", tagName);
			db.insert("tag", null, values);
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
	 */
	public void deleteTag(String tagName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("tag", "type = ?", new String[] { tagName });
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
	 */
	public boolean addData(String title, String content, String data,
			String time, String type) {
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

			db.insert("data", null, values);
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
	 * 修改数据库中的data中的time
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
		int update = db.update("data", values, "title = ? and type = ?", new String[] {
				dataBean.getTitle(), dataBean.getType() });
		
		if (update != 0) {
			return true;
		}else {
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

			mDataList.add(dataBean);
		}
		System.out.println("最后的个数为:" + mDataList.size());
		rawQuery.close();
		db.close();

		return mDataList;
	};

	/**
	 * 删除数据库对应的数据
	 * 
	 * @param title
	 * @param type
	 */
	public boolean deleteData(String title, String type) {
		SQLiteDatabase db = helper.getReadableDatabase();
		int delete = db.delete("data", "title = ? and type = ?", new String[] {
				title, type });

		if (delete != 0) {
			return true;
		} else {
			return false;
		}
	}
}
