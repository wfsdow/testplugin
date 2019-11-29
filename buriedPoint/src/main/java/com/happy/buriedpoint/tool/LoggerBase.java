package com.happy.buriedpoint.tool;

import android.util.Log;

import com.happy.buriedpoint.BuildConfig;


public class LoggerBase
{

    public static final String LOG_TAG = "SharkSdk";
    public static final String ENV_CONFIG = "/mnt/sdcard/sharkadsdk/env.cfg";

//    private boolean isOpen = false;
    private boolean isOpen = BuildConfig.DEBUG;


    public void open()
    {
        isOpen = true;
    }

    public void close()
    {
        isOpen = false;
    }

    public void i(String msg)
    {
        if (isOpen)
            Log.i(LOG_TAG, msg);
    }

    public void e(String msg)
    {
        if (isOpen)
            Log.e(LOG_TAG, msg);
    }

    public void d(String msg)
    {
        if (isOpen)
            Log.d(LOG_TAG, msg);
    }

    public void w(String msg)
    {
        if (isOpen)
            Log.w(LOG_TAG, msg);
    }

    public void error(Throwable e)
    {
        if (isOpen)
            e.printStackTrace();
    }

    public void error(String msg,Throwable e)
    {
        e(msg);
        if (isOpen)
            e.printStackTrace();
    }

    public void log(int code, String msg)
    {
        i("code:" + code + "  msg:" + msg);
    }
}
