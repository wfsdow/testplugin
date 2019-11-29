package com.glad.usualutil.bury;

import android.util.Log;

import com.glad.usualutil.BuildConfig;

public class Logger {
    private static final String TAG = "behappy";
    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }
}
