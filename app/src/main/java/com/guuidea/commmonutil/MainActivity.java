package com.guuidea.commmonutil;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.didi.virtualapk.PluginManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.load).setOnClickListener(v -> {
            loadPlugin();
        });
    }

    private void loadPlugin() {
        String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Test.apk");
        File plugin = new File(pluginPath);
        try {
            PluginManager.getInstance(this).loadPlugin(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = new Intent();
        intent.setClassName("com.ning.myplugin", "com.ning.myplugin.TestPluginActivity");
        startActivity(intent);
    }
}
