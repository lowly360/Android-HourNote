package com.itlowly.twenty.receiver;

import java.util.ArrayList;

import com.itlowly.twenty.R;
import com.itlowly.twenty.activity.AddDataActivity;
import com.itlowly.twenty.activity.DetailActivity;
import com.itlowly.twenty.activity.TimerActivity;
import com.itlowly.twenty.activity.WidgetActivity;
import com.itlowly.twenty.db.LocalNoteDB;
import com.itlowly.twenty.service.WidgetSetService;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * 桌面小控件，广播
 * 
 * @author lowly_pc
 *
 */
public class MyWidgetProvider extends AppWidgetProvider {
	private static final String BROADCAST_STRING = "com.itlowly.twenty.action.APPWIDGET_UPDATE";
	private static final String BROADCAST_TAG = "com.itlowly.twenty.action.CHANCE_TAG";
	private static String mTag = "";
	private static ArrayList<String> allTag;
	private static AppWidgetManager appWidgetManager ;
	private static int[] appWidgetIds;
	private RemoteViews remoteViews;
	private SharedPreferences mPre;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		LocalNoteDB db = new LocalNoteDB(context);

		allTag = db.getAllTag();
		
		mPre = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		mTag = mPre.getString("mTag", allTag.get(0));

		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_view);

		Intent intent = new Intent();
		intent.setClass(context, WidgetSetService.class);
		//准备发送
		intent.putExtra("type", mTag);
//		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		remoteViews.setRemoteAdapter(R.id.lv_widget, intent);

		// 设置添加按钮事件
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, AddDataActivity.class), 0);
		remoteViews.setOnClickPendingIntent(R.id.ib_widget_add, pendingIntent);

		//设置标签按钮文字
		remoteViews.setTextViewText(R.id.tv_widget_tag, mTag);
		
		// 设置标签按钮事件
		Intent tagIntent = new Intent();
		tagIntent.setClass(context, WidgetActivity.class);
		tagIntent.putExtra("type", mTag);
		PendingIntent tagPendingIntent = PendingIntent.getActivity(context, 1,
				tagIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.tv_widget_tag,
				tagPendingIntent);

		// 分界线
		Intent intent2 = new Intent();
		intent2.setAction(BROADCAST_STRING);
		PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(
				context, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

		// 拼接PendingIntent
		remoteViews.setPendingIntentTemplate(R.id.lv_widget,
				pendingIntentTemplate);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.notifyAppWidgetViewDataChanged(appWidgetIds[0], R.id.lv_widget);

		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(BROADCAST_STRING)) {
			System.out.println("BROADCAST_STRING");
			System.out.println("接受到广播1");
			Intent intent1 = new Intent();

			if (intent.getStringExtra("istwenty").equals("1")) { // 倒计时类型的笔记
				intent1.setClass(context, TimerActivity.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent1.putExtra("title", intent.getStringExtra("title"));
				intent1.putExtra("type", intent.getStringExtra("type"));
			} else {
				intent1.setClass(context, DetailActivity.class);// 非倒计时的笔记
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent1.putExtra("title", intent.getStringExtra("title"));
				intent1.putExtra("type", intent.getStringExtra("type"));
			}

			context.startActivity(intent1);
		} else if (intent.getAction().equals(BROADCAST_TAG)) { // 更改标签数据
			mTag = intent.getStringExtra("type");
			System.out.println("接收到了广播：+"+mTag);
			
			mPre = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			
			mPre.edit().putString("mTag", mTag).commit();
			
			appWidgetManager = AppWidgetManager.getInstance(context);
			
			ComponentName thisAppWidName = new ComponentName(context.getPackageName(),MyWidgetProvider.class.getName());
			
			appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidName);
			
			onUpdate(context, appWidgetManager, appWidgetIds);
			
		}

		super.onReceive(context, intent);
	}

}
