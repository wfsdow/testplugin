package com.glad.qrcode;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class QRCodeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView mImageView;
    private LinearLayout updateContainer;
    private int clickCount = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        initView();
    }

    private void initView() {
//        initUpdateViews();

        mImageView = findViewById(R.id.img);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount --;
                if (clickCount == 0){
                    updateContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        final EditText urlET = findViewById(R.id.urlET);
        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlStr = urlET.getText().toString().trim();
                if(TextUtils.isEmpty(urlStr)){
                    Toast.makeText(QRCodeActivity.this, "您输入的url为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap("https://www.baidu.com", 480, 480);
                mImageView.setImageBitmap(mBitmap);
            }
        });
    }





    private static final String TAG = "MainActivity";
}
