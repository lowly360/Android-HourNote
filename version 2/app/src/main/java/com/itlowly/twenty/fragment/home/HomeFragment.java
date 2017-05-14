package com.itlowly.twenty.fragment.home;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.itlowly.twenty.R;
import com.itlowly.twenty.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Lelight
 * All right reserved.
 * <p>
 * Created by itLowly
 * <p>
 * on 2017/2/6 17:54
 */

public class HomeFragment extends BaseFragment {

    private ListView lv_groups;
    private ListView lv_notes;

    @Override
    public void initViews() {
        inflateRootView(R.layout.fragment_home);

        lv_groups = (ListView) mRootView.findViewById(R.id.lv_groups);
        lv_notes = (ListView) mRootView.findViewById(R.id.lv_notes);

        List<String> mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("test:" + i);
        }

        TestAdapter testAdapter = new TestAdapter(mActivity, mData, R.layout.item_lv_test);
        TestAdapter tagAdapter = new TestAdapter(mActivity,mData,R.layout.item_lv_tag);
        lv_groups.setAdapter(tagAdapter);
        lv_notes.setAdapter(testAdapter);

    }
}
