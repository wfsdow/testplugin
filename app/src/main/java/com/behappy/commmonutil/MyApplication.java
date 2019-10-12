package com.behappy.commmonutil;

import android.app.Application;
import android.content.Context;

import com.didi.virtualapk.PluginManager;

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }
}
