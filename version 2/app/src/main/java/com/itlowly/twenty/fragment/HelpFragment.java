package com.itlowly.twenty.fragment;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.itlowly.twenty.R;
import com.zhy.changeskin.SkinManager;

/**
 * Copyright 2016 Lelight
 * All right reserved.
 * <p>
 * Created by itLowly
 * <p>
 * on 2017/2/6 18:16
 */

public class HelpFragment extends BaseFragment {

    private int themeType = 0;

    @Override
    public void initViews() {
        inflateRootView(R.layout.fragment_help);
        Button btn_change_skin = (Button) mRootView.findViewById(R.id.btn_change_skin);
        btn_change_skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                themeType++;
                switch (themeType) {
                    case 0:
                        SkinManager.getInstance().removeAnySkin();
                        break;
                    case 1:
                        SkinManager.getInstance().changeSkin("night");
                        themeType = -1;
                        break;
                }
            }
        });
    }

}
