package com.itlowly.twenty;

import android.app.Application;

import com.zhy.changeskin.SkinManager;

/**
 * Created by lowly on 2017/5/14.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
