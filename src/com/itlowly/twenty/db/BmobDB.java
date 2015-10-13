package com.itlowly.twenty.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itlowly.twenty.db.dao.BmobOperHelper;

public class BmobDB {

	private BmobOperHelper helper;
	private Context context;

	public BmobDB(Context context) {
		helper = new BmobOperHelper(context);
		this.context = context;
	}

	public void clearAllUploadData() {
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"select name from sqlite_master where type='table' order by name",
						null);
		while (cursor.moveToNext()) {
			// 遍历出表名
			String name = cursor.getString(0);
			if (name.equals("upload")) {
				db.delete("upload", null, null);
			}
		}
		cursor.close();
		db.close();
	}

}
