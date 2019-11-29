package com.glad.usualutil.account;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.glad.usualutil.bury.BuriedPointUtils;
import com.glad.usualutil.bury.ConstantString;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class SyncService extends Service {
    private SyncAdapter mSyncAdapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSyncAdapter = new SyncAdapter(getApplicationContext(), true);
    }

    static class SyncAdapter extends AbstractThreadedSyncAdapter {

        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


            Map<Integer, Object> map = new HashMap<>();
            map.put(1, "account sync");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(System.currentTimeMillis());
            map.put(2,time);
            map.put(3,Build.MODEL);
            BuriedPointUtils.saveInfo(ConstantString.APP_ACCOUNT_LIVE,map);
            //Log.i("sdsdsdsvcvv","同步账户回调"+Build.BRAND+"time:"+time+"modle:"+Build.MODEL);
        }
    }

}
