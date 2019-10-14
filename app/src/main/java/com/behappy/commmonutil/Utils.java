package com.behappy.commmonutil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.didi.virtualapk.PluginManager;

import java.io.File;

public class Utils {
    /**
     * 加载插件中指定的类
     * @param context
     * @param filePath
     * @param packageName
     * @param className
     */
    public static void loadPlg(Context context, String filePath, String packageName, String className) {
        try {
            File plugin = new File(filePath);
            if (PluginManager.getInstance(context).getLoadedPlugin(packageName) == null) {
                Log.i(TAG, "插件还未加载");
                PluginManager.getInstance(context).loadPlugin(plugin);
            }else {
                Log.i(TAG, "插件已经加载");
            }
            Intent intent = new Intent();
            intent.setClassName(packageName, className);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("behappy", "loadPlg: " + e);
        }


    }

    private static final String TAG = "behappy";
}
