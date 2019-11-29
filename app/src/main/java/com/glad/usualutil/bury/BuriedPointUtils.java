package com.glad.usualutil.bury;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.glad.usualutil.MyApplication;
import com.happy.buriedpoint.EventHandler;
import com.happy.buriedpoint.EventSender;
import com.happy.buriedpoint.tool.IntenetUtil;

import java.util.Map;

/**
 *@date 2019/10/21
 *@author ningwei
 *@description  埋点工具类
 */
public class BuriedPointUtils {
    /**
     * 保存埋点到数据库
     * @param type  埋点的名称
     * @param params  埋点的额外参数
     */
    public static void saveInfo(String type, Map<Integer, Object> params) {
        Context context = MyApplication.getApplication();
        EventSender sender = EventHandler.sender(context)
                .name(type)
                .network(IntenetUtil.getNetworkState(context));

        if (params != null && params.entrySet().size() > 0) {
            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                sender.f(entry.getKey(), entry.getValue().toString());
            }
        }

        if (TextUtils.isEmpty(ConstantString.eventID)) {
            ConstantString.eventID = String.valueOf(-System.currentTimeMillis());
        }
        sender.f(6, ConstantString.eventID);
        sender.send();
    }

    /**
     * 设置应用不是第一次打开
     * @param context
     */
    public static void setFirstOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("first_open", false);

        edit.commit();
    }

    /**
     * 判断应用是否是第一次打开
     * @param context
     * @return
     */
    public static boolean isFirstOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean("first_open", true);
    }
}
