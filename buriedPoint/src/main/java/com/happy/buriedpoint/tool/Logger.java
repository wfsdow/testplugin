package com.happy.buriedpoint.tool;

import android.os.Environment;

import java.io.File;


public class Logger extends LoggerBase
{

    private static boolean isDebug = false;

    public static void setDebug(boolean debug)
    {
        isDebug = debug;
    }

    private static Logger sLogger;

    private LoggerBase devLogger = new LoggerBase();

    private LoggerBase debugLogger = new LoggerBase();

    //logger for the dveloper who plugin this sdk.

    public static boolean isDevelop()
    {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();

        File debugDir = new File(externalStorageDirectory, "shark.debug");

        return new File(debugDir, "dev").exists();
    }

    public static Logger rel()
    {
        if (sLogger == null)
        {
            sLogger = new Logger();

//            if (isDevelop())
//                sLogger.devLogger.open();
//            else
//                sLogger.devLogger.close();
//
//            if (isDebug)
//                sLogger.debugLogger.open();
//            else
//                sLogger.debugLogger.close();
        }
        return sLogger;
    }


    public static LoggerBase dev()
    {
        return rel().devLogger;
    }

    public static LoggerBase debug()
    {
        return rel().debugLogger;
    }
}
