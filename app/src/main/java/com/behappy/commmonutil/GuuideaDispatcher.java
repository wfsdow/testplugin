package com.behappy.commmonutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.behappy.datelibrary.CountdownActivity;
import com.behappy.datelibrary.DateCalculationActivity;
import com.behappy.downloadlibrary.DownloadActivity;
import com.behappy.qrcode.QRCodeActivity;
//import com.happywork.colorlibrary.ColorListActivity;

/**
 *@date 2019/10/9
 *@author ningwei
 *@description 调度器
 */
public class GuuideaDispatcher {
    public static void goActivity(Context context, Class<? extends Activity> clazz) {
        if (context == null || clazz == null) {
            Log.e(TAG, "context or clazz is null");
            return;
        }

        context.startActivity(new Intent(context, clazz));
    }

    public static void goQRCodeActivity(Context context) {
        goActivity(context, QRCodeActivity.class);
    }

    public static void goDownloadActivity(Context context) {
        goActivity(context, DownloadActivity.class);
    }

    public static void goDateActivity(Context context) {
        goActivity(context, DateCalculationActivity.class);
    }

    public static void goCountDownActivity(Context context) {
        goActivity(context, CountdownActivity.class);
    }

    public static void goColorsListActivity(Context context) {
//        goActivity(context, ColorListActivity.class);
        String plgPath = context.getExternalFilesDir(null) + "/color.apk";
//        Utils.loadPlg(context, plgPath, "com.behappy.colorplg",
//                "com.behappy.colorlibrary.ColorListActivity");

        Utils.loadPlg(context, plgPath, "com.behappy.colorplg",
                "com.behappy.colorplg.ColorMainActivity");
//        Utils.loadPlg(context, plgPath, "com.behappy.myplugin",
//                "com.behappy.myplugin.TestPluginActivity");
    }

    private static final String TAG = "GuuideaDispatcher";
}
