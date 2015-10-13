package com.itlowly.twenty.fragment;

import java.util.ArrayList;

import com.itlowly.twenty.R;
import com.itlowly.twenty.base.ContentBasePager;
import com.itlowly.twenty.base.impl.AboutPager;
import com.itlowly.twenty.base.impl.HelpPager;
import com.itlowly.twenty.base.impl.HomePager;
import com.itlowly.twenty.base.impl.SettingPager;
import com.itlowly.twenty.utils.DensityUtils;
import com.itlowly.twenty.view.NoScrollViewPager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class ContentFragment extends BaseFragment {

	// 主页的viewpager
	private NoScrollViewPager vpHome;

	private ArrayList<ContentBasePager> contentList;

	private static HomePager mHomePager;

	private SettingPager settingPager;

	public SettingPager getSettingPager() {
		return settingPager;
	}

	private HelpPager helpPager;

	private AboutPager aboutPager;

	public  HomePager getHomePager() {
		return mHomePager;
	}

	@Override
	public View initViews() {

		View view = View.inflate(mActivity, R.layout.fragment_content, null);

		vpHome = (NoScrollViewPager) view.findViewById(R.id.vp_home);

		return view;
	}

	public static ContentBasePager getPager() {

		if (mHomePager != null) {
			return mHomePager;
		} else {
			return null;
		}

	}

	@Override
	public void initData() {
		contentList = new ArrayList<ContentBasePager>();

		if (mActivity == null) {
			return;
		}

		mHomePager = new HomePager(mActivity);
		settingPager = new SettingPager(mActivity);
		helpPager = new HelpPager(mActivity);
		aboutPager = new AboutPager(mActivity);
		
		contentList.add(mHomePager);
		contentList.add(helpPager);
		contentList.add(settingPager);
		contentList.add(aboutPager);

		MyPagerAdatper myPagerAdatper = new MyPagerAdatper();

		vpHome.setAdapter(myPagerAdatper);

	}
	
	public void setCurrenerPager(int i){
		if (i<contentList.size()) {
			vpHome.setCurrentItem(i);
		}
	}
	
	/**
	 * 获得当前的显示页面id
	 * @return int
	 */
	public int getCurrenerPager(){
		return vpHome.getCurrentItem();
	}

	class MyPagerAdatper extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contentList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ContentBasePager contentBasePager = contentList.get(position);

			container.addView(contentBasePager.mRootView);

			return contentBasePager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}
	}
}
