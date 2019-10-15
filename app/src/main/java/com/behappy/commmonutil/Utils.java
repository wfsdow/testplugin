package com.behappy.commmonutil;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.behappy.downloadlibrary.ConstantInfo;
import com.behappy.downloadlibrary.DownloadUtils;
import com.didi.virtualapk.PluginManager;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener3;

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
            if (!plugin.exists()) {

                return;
            }
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
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }


    }


    public static void loadPlg2(Context context, String filePath, String packageName, String className) {
        File plugin = new File(filePath);
        //判断插件是否存在，不存在则先下载。如果存在，则加载插件。
        if (!plugin.exists()) {
            DownloadUtils.download(context, ConstantInfo.PLUGIN_URL, ConstantInfo.PLUGIN_NAME,
                    new DownloadListener3() {
                        @Override
                        protected void started(@NonNull DownloadTask task) {
                            Log.i(TAG, "started: ");
                        }

                        @Override
                        protected void completed(@NonNull DownloadTask task) {
                            Log.i(TAG, "completed: ");
                            loadPlg(context, filePath, packageName, className);
                        }

                        @Override
                        protected void canceled(@NonNull DownloadTask task) {

                        }

                        @Override
                        protected void error(@NonNull DownloadTask task, @NonNull Exception e) {
                            Log.i(TAG, "error: " + e);
                        }

                        @Override
                        protected void warn(@NonNull DownloadTask task) {

                        }

                        @Override
                        public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

                        }

                        @Override
                        public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

                        }

                        @Override
                        public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {

                        }
                    });
        }else {
            loadPlg(context, filePath, packageName, className);
        }


    }

    private static final String TAG = "behappy";
}
