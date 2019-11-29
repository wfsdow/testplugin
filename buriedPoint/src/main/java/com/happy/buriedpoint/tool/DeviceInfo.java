package com.happy.buriedpoint.tool;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import static android.content.Context.TELEPHONY_SERVICE;

public class DeviceInfo {

    private static DeviceInfo deviceInfo;

    public static DeviceInfo instance(Context context) {
        if (deviceInfo == null)
            synchronized (DeviceInfo.class) {
                if (deviceInfo == null)
                    deviceInfo = new DeviceInfo(context);
            }

        return deviceInfo;
    }

    private Context context;

    public DeviceInfo(Context context) {
        this.context = context;
    }

    public String getImei() {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission")
            String deviceID = tm.getDeviceId();
            if (deviceID == null) {
                return "UnKnow";
            } else {
                return deviceID;
            }
        } catch (Exception e) {
            return "UnKnow";
        }
    }

    public String getPackageName() {
        String packageName = context.getPackageName();
        if (packageName == null) {
            return "UnKnow";
        } else {
            return packageName;
        }
    }

    public String getDeviceModel() {
        return Build.MODEL;
    }

    public String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getDeviceAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public int getDeviceSDK() {
        return Build.VERSION.SDK_INT;
    }

    public long getRam() {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        long memSize = memoryInfo.totalMem / 1024;
        return memSize;
    }

    public int getWitdh() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        return widthPixels;
    }

    public int getHeight() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        int heightPixels = outMetrics.heightPixels;
        return heightPixels;
    }

    @SuppressLint("MissingPermission")
    public String getImsi() {
        String imsi = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            imsi = telephonyManager.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imsi;

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public String getId() {
        try {
            String androidId_key;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                androidId_key = Settings.Secure.ANDROID_ID;
            } else {
                androidId_key = Settings.System.ANDROID_ID;
            }
            return Settings.System.getString(context.getContentResolver(), androidId_key);
        } catch (Exception e) {

        }
        return "";
    }

    public int getProvider() {
        int providerId = 0;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String provider = telephonyManager.getSimOperatorName();
        if (provider.equals("46000") || provider.equals("46002") || provider.equals("46007")) {
            providerId = 1;
        } else if (provider.equals("46001") || provider.equals("46006")) {
            providerId = 2;
        } else if (provider.equals("46003") || provider.equals("46005")) {
            providerId = 4;
        }
        return providerId;
    }
}
