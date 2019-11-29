package com.glad.usualutil;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.didi.virtualapk.PluginManager;
import com.glad.usualutil.account.AccountHelper;
import com.glad.usualutil.bury.BuriedPointUtils;
import com.glad.usualutil.bury.ConstantString;
import com.glad.usualutil.bury.Logger;

public class MyApplication extends Application {
    private static Application globalApplication;
    private int mFinalCount;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                //如果mFinalCount ==1，说明是从后台到前台
                if (mFinalCount == 1) {
                    //说明从后台回到了前台
                    Logger.i("切换到了前台");
                    BuriedPointUtils.saveInfo(ConstantString.APP_FRONT, null);

                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                //如果mFinalCount == 0，说明是前台到后台
                if (mFinalCount == 0) {
                    //说明从前台回到了后台
                    Logger.i("切换到了后台");
                    BuriedPointUtils.saveInfo(ConstantString.APP_BACKGROND, null);

                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });

        globalApplication = this;
        //初始化事件id
        ConstantString.eventID = String.valueOf(System.currentTimeMillis());
        //保存启动应用的埋点
        BuriedPointUtils.saveInfo(ConstantString.APP_START, null);
        //应用第一次打开，代表是初始化安装
        if (BuriedPointUtils.isFirstOpen(this)){
            Logger.i("第一次启动");
            BuriedPointUtils.setFirstOpen(this);
            BuriedPointUtils.saveInfo(ConstantString.APP_INSTALL, null);

        }
        AccountHelper.addAccount(this);
        AccountHelper.autoSyncAccount();
    }

    public static Application getApplication() {
        return globalApplication;
    }
}
