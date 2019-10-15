package com.glad.downloadlibrary;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener3;

public class DownloadActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_start_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "点击按钮开始下载 ");
                DownloadUtils.download(DownloadActivity.this,
                        ConstantInfo.URL, ConstantInfo.APK_NAME,
                        new DownloadListener3() {
                            @Override
                            protected void started(@NonNull DownloadTask task) {
                                Log.i(TAG, "taskStart: ");
                                progressBar.setProgress(0);
                            }

                            @Override
                            protected void completed(@NonNull DownloadTask task) {
                                Log.i(TAG, "completed: ");
                                resultTV.setText("complete");
                                progressBar.setProgress(100);
                            }

                            @Override
                            protected void canceled(@NonNull DownloadTask task) {

                            }

                            @Override
                            protected void error(@NonNull DownloadTask task, @NonNull Exception e) {
                                Log.i(TAG, "error: " + e);
                            }

                            @Override
                            protected void warn(@NonNull DownloadTask task) {

                            }

                            @Override
                            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

                            }

                            @Override
                            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {
                                Log.i(TAG, "connected: ");
                            }

                            @Override
                            public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
                                Log.i(TAG, "progress: " + currentOffset);
                                progressBar.setProgress((int)(currentOffset * 100/totalLength));
                            }
                        });
            }
        });

        findViewById(R.id.install).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "点击按钮开始安装 ");
                DownloadUtils.install(DownloadActivity.this, ConstantInfo.APK_NAME);
            }
        });
        progressBar = findViewById(R.id.pb_progress);
        progressBar.setMax(100);

        resultTV = findViewById(R.id.tv_result);
    }

    private static final String TAG = "behappy";
}
