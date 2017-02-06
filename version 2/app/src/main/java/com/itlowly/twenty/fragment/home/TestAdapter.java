package com.itlowly.twenty.fragment.home;

import android.content.Context;

import com.itlowly.twenty.baseadapter.CommonAdapter;
import com.itlowly.twenty.baseadapter.ViewHolder;

import java.util.List;

/**
 * Copyright 2016 Lelight
 * All right reserved.
 * <p>
 * Created by itLowly
 * <p>
 * on 2017/2/6 18:02
 */

public class TestAdapter extends CommonAdapter<String> {

    public TestAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String str) {

    }
}
