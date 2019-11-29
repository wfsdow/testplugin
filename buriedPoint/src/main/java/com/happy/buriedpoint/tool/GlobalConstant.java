package com.happy.buriedpoint.tool;


public class GlobalConstant
{
    public static String logURL = "https://api.itoolbox.top/dcs/log";
    private static String serverUrl = "default";
    private static String rendingType = "js";
    private static String appid = "10001";
    private static String sdkVersion = "1.0.0";
    public static boolean isDebug = false;
    public static boolean isDev = false;
    /**
     * 重新申请以上两个配置的时间间隔  单位：分钟
     */
    private static int intervel = 24*60;


    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        GlobalConstant.serverUrl = serverUrl;
    }

    public static String getRendingType() {
        return rendingType;
    }

    public static void setRendingType(String rendingType) {
        GlobalConstant.rendingType = rendingType;
    }

    public static int getIntervel() {
        return intervel;
    }

    public static void setIntervel(int intervel) {
        GlobalConstant.intervel = intervel;
    }

    public static String getAppid() {
        return appid;
    }

    public static void setAppid(String appid) {
        GlobalConstant.appid = appid;
    }

    public static String getSdkVersion() {
        return sdkVersion;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean isDebug) {
        GlobalConstant.isDebug = isDebug;
    }

    public static boolean isIsDev() {
        return isDev;
    }

    public static void setIsDev(boolean isDev) {
        GlobalConstant.isDev = isDev;
    }
}
