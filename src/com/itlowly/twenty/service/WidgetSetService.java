package com.itlowly.twenty.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.itlowly.twenty.R;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;

public class WidgetSetService extends RemoteViewsService {
	private static final String BROADCAST_STRING = "com.itlowly.twenty.action.APPWIDGET_UPDATE";
	private String mTag;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onCreate();
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind");
		

		
		return super.onBind(intent);
		
		
	}
	

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		System.out.println("onGetViewFactory");
		return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
	}

	class ListRemoteViewsFactory implements
			RemoteViewsService.RemoteViewsFactory {

		private final Context mContext;
		private List<DataBean> mList;
		private int[] appWidgetIds;
		private String mTag;
		private Intent intent;
		private SharedPreferences mPre;
		

		public ListRemoteViewsFactory(Context context, Intent intent) {
			mContext = context;
			this.intent = intent;
//			appWidgetIds = intent
//					.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
			mList = new ArrayList<DataBean>();

			
			mPre = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
			mTag = mPre.getString("mTag", "学习");
			
			mTag = intent.getStringExtra("type");

			System.out.println("WidgetSetService:得到的Tag数据是："+mTag);
			
			LocalNoteDB db = new LocalNoteDB(context);
			if (mTag != null) {
				mList = db.findAllInType(mTag);
				System.out.println("正在更改数据："+mTag);
				
			} else {
				mList = db.findAllInType("学习");
			}

			if (Looper.myLooper() == null) {
				Looper.prepare();
			}
		}

		@Override
		public void onCreate() {
		}

		@Override
		public void onDataSetChanged() {
			mPre = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
			mTag = mPre.getString("mTag", "学习");
			System.out.println("数据改变了"+mTag);
			LocalNoteDB db = new LocalNoteDB(mContext);
			if (mTag != null) {
				mList = db.findAllInType(mTag);
				System.out.println("正在更改数据："+mTag);
				
			} else {
				mList = db.findAllInType("学习");
			}
		}
		

		@Override
		public void onDestroy() {
			mList.clear();
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public RemoteViews getViewAt(int position) {
			if (position < 0 || position >= mList.size())
				return null;
			DataBean data = mList.get(position);
			RemoteViews rv = new RemoteViews(mContext.getPackageName(),
					R.layout.widget_list_item);

			Intent intent = new Intent();
			intent.setAction(BROADCAST_STRING);
			intent.putExtra("title", data.getTitle());
			intent.putExtra("type", data.getType());
			String isTwenty = data.getIsTwenty();
			intent.putExtra("istwenty", isTwenty);


			// 与CustomWidget中remoteViews.setPendingIntentTemplate配对使用，共同作用。
			rv.setTextViewText(R.id.tv_widget_list_item_title, data.getTitle());
			if (isTwenty.equals("1")) {
				rv.setTextColor(R.id.tv_widget_list_item_title, mContext
						.getResources().getColor(R.color.title_yes));
			} else {
				rv.setTextColor(R.id.tv_widget_list_item_title, mContext
						.getResources().getColor(R.color.title_not));
			}

			rv.setTextViewText(R.id.tv_widget_list_item_content,
					data.getContent());
			rv.setOnClickFillInIntent(R.id.tv_widget_list_item_content, intent);
			rv.setOnClickFillInIntent(R.id.tv_widget_list_item_title, intent);
			rv.setOnClickFillInIntent(R.id.ib_widget_list_item_play, intent);
			return rv;
		}

		@Override
		public RemoteViews getLoadingView() {
			return null;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}