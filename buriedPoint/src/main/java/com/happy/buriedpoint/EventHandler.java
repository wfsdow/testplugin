package com.happy.buriedpoint;

import android.content.Context;
import android.text.TextUtils;

import com.happy.buriedpoint.tool.DeviceInfo;
import com.happy.buriedpoint.tool.GlobalConstant;
import com.happy.buriedpoint.tool.IntenetUtil;
import com.happy.buriedpoint.tool.Logger;

import org.json.JSONObject;

import java.io.File;


public class EventHandler
{
    private static EventProvider sEventProvider;


    public static EventSender sender(Context context)
    {
        //使用双重检查锁，保证单例
        if (sEventProvider == null){
            synchronized (EventHandler.class){
                if (sEventProvider == null){
                    init(context);
                }
            }
        }

        return sEventProvider.sender();
    }

    public static EventSender sender(Context context, String appId){
        GlobalConstant.setAppid(appId);
        return sender(context);
    }

    private static void init(Context context)
    {
        File eventinner = new File(context.getFilesDir(), "eventinner");

        if (!eventinner.exists())
            eventinner.mkdirs();

        sEventProvider = EventProvider.provider(context, eventinner,
                new EventProvider.NetEventAdapter(GlobalConstant.logURL, context)
        {

            @Override
            public void log(String msg)
            {
                Logger.dev().i(msg);
            }

            @Override
            public void handle(Throwable e)
            {
                Logger.dev().error("event log", e);
            }

            @Override
            public int getRetryCount()
            {
                return 3;
            }
        });

        setup(context);
    }

    private static void setup(Context context)
    {
        //TODO more setup
        sEventProvider.setInjector(sender -> {
            sender.uid(DeviceInfo.instance(context).getId())
                    .appId(Integer.parseInt(GlobalConstant.getAppid()))
                    .sdkVersion(GlobalConstant.getSdkVersion())
                    .operator(DeviceInfo.instance(context).getImsi())
                    .operator(DeviceInfo.instance(context).getProvider());
        });
    }

    public static EventSender from(Context context, JSONObject json)
    {
        return sender(context)
                .network(IntenetUtil.getNetworkState(context))
                .channel(json.optString("placeid"))
                .business(json.optString("adsid"))
                .f(1, json.optString("tid"))
                .f(2, json.optString("sid"))
                .f(3, json.optString("package"))
                ;
    }
}
