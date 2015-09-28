package com.itlowly.twenty.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment基类
 * @author Administrator
 *
 */
public abstract class BaseFragment extends Fragment{
	
	public Activity mActivity; //所依附的activity
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = getActivity();
		
	}
	
	//处理fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		return initViews();
	}
	
	//fragment依附的activity创建完毕
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
		
	}
	
	public abstract View initViews();

	public void initData() {
		// TODO Auto-generated method stub
		
	}
	

}
