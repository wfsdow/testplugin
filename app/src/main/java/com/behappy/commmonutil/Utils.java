package com.behappy.commmonutil;

import android.content.Context;
import android.content.Intent;

import com.didi.virtualapk.PluginManager;

import java.io.File;

public class Utils {
    private void loadPlg(Context context, String filePath, String packageName, String className) {
        File plugin = new File(filePath);
        try {
            PluginManager.getInstance(context).loadPlugin(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        context.startActivity(intent);
    }
}
