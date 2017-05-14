package com.itlowly.twenty.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment基类
 *
 * @author Administrator
 */
public abstract class BaseFragment extends Fragment {
    public Activity mActivity; //所依附的activity
    public View mRootView;
    private LayoutInflater inflater;
    private ViewGroup container;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(getClass().getName(),"onCreate");
        mActivity = getActivity();
    }

    //处理fragment的布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(getClass().getName(),"onCreateView");
        this.inflater = inflater;
        this.container = container;
        initViews();
        return mRootView;
    }

    //fragment依附的activity创建完毕
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        Log.e(getClass().getName(),"onActivityCreated");
        initData();
    }

    public abstract void initViews();

    public void initData() {
        // TODO Auto-generated method stub
    }

    public void inflateRootView(@LayoutRes int resId) {
        Log.e(getClass().getName(),"inflateRootView");
        mRootView = inflater.inflate(resId, container, false);
    }

}
