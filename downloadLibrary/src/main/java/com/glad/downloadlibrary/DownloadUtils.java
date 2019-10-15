package com.glad.downloadlibrary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;

import java.io.File;

public class DownloadUtils {
//    public static void download(Context context, String url, DownloadListener listener1) {
//        File dir = context.getExternalFilesDir(null);
//        DownloadTask task = new DownloadTask.Builder(url, dir)
//                .setFilename(ConstantInfo.APK_NAME)
//                // the minimal interval millisecond for callback progress
//                .setMinIntervalMillisCallbackProcess(30)
//                // do re-download even if the task has already been completed in the past.
//                .setPassIfAlreadyCompleted(false)
//                .build();
//
//        task.enqueue(listener1);
//    }

    /**
     *
     * @param context
     * @param url  下载文件的url
     * @param fileName  下载保存的文件名（不含路径。文件保存在context.getExternalFilesDir目录中）
     * @param listener1  下载的回调监听
     */
    public static void download(Context context, String url, String fileName,DownloadListener listener1) {
        File dir = context.getExternalFilesDir(null);
        DownloadTask task = new DownloadTask.Builder(url, dir)
                .setFilename(fileName)
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(30)
                // do re-download even if the task has already been completed in the past.
                .setPassIfAlreadyCompleted(false)
                .build();

        task.enqueue(listener1);
    }

    private static Uri getUri(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT > 24) {
            String packageName = context.getPackageName();
            uri = FileProvider.getUriForFile(context, packageName+".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 安装apk.
     * @param context
     * @param fileName  文件名(不含路径。文件保存在context.getExternalFilesDir目录中)
     */
    public static void install(Context context, String fileName) {
        File apkFile = new File(context.getExternalFilesDir(null), fileName);
        if (!apkFile.exists()) {
            Log.i(TAG, "要安装的文件不存在");
            return;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(getUri(context, apkFile),"application/vnd.android.package-archive");
        context.startActivity(intent);
        Log.i(TAG, "开始安装");
    }

    private static final String TAG = "behappy";
}
