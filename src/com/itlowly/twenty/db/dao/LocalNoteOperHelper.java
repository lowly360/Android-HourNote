package com.itlowly.twenty.db.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalNoteOperHelper extends SQLiteOpenHelper {

	public LocalNoteOperHelper(Context context) {
		super(context,context.getSharedPreferences("config", Context.MODE_PRIVATE).getString("LocateUser", "LocalNote")+".db", null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table tag (_id integer primary key autoincrement,type varchar(100))");
		db.execSQL("create table data (_id integer primary key autoincrement,title varchar(200),content varchar(1000),data varchar(400),time varchar(100),type varchar(100),isTwenty varchar(2))");
		db.execSQL("create table history (_id integer primary key autoincrement,historyType varchar(100),tagName varchar(100),title varchar(200),content varchar(1000),data varchar(400),time varchar(100),type varchar(100),isTwenty varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
