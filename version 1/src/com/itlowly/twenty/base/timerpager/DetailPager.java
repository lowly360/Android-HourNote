package com.itlowly.twenty.base.timerpager;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itlowly.twenty.R;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.bean.DataBean;
import com.itlowly.twenty.db.LocalNoteDB;

public class DetailPager extends ContentBasePager {
	
	
	
	private TextView tv_timer_title;
	private TextView tv_timer_content;
	
	
	
	public DetailPager(Activity activity, String title, String type) {
		super(activity, title, type);
	}



	@Override
	public View initView() {
		mRootView = View.inflate(mActivity, R.layout.timer_pagerdetail, null);
		
		tv_timer_title = (TextView) mRootView.findViewById(R.id.tv_timer_title);
		tv_timer_content = (TextView) mRootView.findViewById(R.id.tv_timer_content);
		
		//Toast.makeText(mActivity, "===="+title+type, 0).show();
		
		
		LocalNoteDB db = new LocalNoteDB(mActivity);
		DataBean dataBean = db.getDataBean(title, type);
		
		tv_timer_title.setText(dataBean.getTitle());
		tv_timer_content.setText(dataBean.getContent());
		
		return mRootView;
	}
	

}
