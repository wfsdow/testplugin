package com.behappy.commmonutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView downloadIV;
    private ImageView pluginIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        //跳转到二维码页面
        findViewById(R.id.btn1).setOnClickListener(v -> {
            goQRCode();
        });

        //日期间隔计算页面
        findViewById(R.id.btn2).setOnClickListener(v -> {
            goDate();

        });

        //倒计时页面
        findViewById(R.id.btn3).setOnClickListener(v -> {
            goCountDown();

        });

        //配色页面
        findViewById(R.id.btn4).setOnClickListener(v -> {
            goColorsList();

        });

        findViewById(R.id.btn4).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (downloadIV.getVisibility() != View.VISIBLE) {
                    downloadIV.setVisibility(View.VISIBLE);
                    pluginIV.setVisibility(View.VISIBLE);
                }else {
                    downloadIV.setVisibility(View.GONE);
                    pluginIV.setVisibility(View.GONE);
                }
                return true;
            }
        });

        downloadIV = findViewById(R.id.download);
        pluginIV = findViewById(R.id.plugin);

        downloadIV.setOnClickListener(v -> {
            goDownload();
        });

        downloadIV.setVisibility(View.GONE);
        pluginIV.setVisibility(View.GONE);
    }

    private void goColorsList() {
        GuuideaDispatcher.goColorsListActivity(this);
    }

    private void goCountDown() {
        GuuideaDispatcher.goCountDownActivity(this);
    }

//    private void loadPlugin() {
//        String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Test.apk");
//        File plugin = new File(pluginPath);
//        try {
//            PluginManager.getInstance(this).loadPlugin(plugin);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        Intent intent = new Intent();
//        intent.setClassName("com.ning.myplugin", "com.ning.myplugin.TestPluginActivity");
//        startActivity(intent);
//    }

    public void goQRCode() {
        GuuideaDispatcher.goQRCodeActivity(this);
    }

    public void goDate() {
        GuuideaDispatcher.goDateActivity(this);
    }

    public void goDownload() {
        GuuideaDispatcher.goDownloadActivity(this);
    }

}