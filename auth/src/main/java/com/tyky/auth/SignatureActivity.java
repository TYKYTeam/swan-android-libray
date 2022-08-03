package com.tyky.auth;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.tyky.auth.view.PaintView;
import com.tyky.webviewBase.event.JsCallBackEvent;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;

public class SignatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        String callBackMethod = getIntent().getStringExtra("callBackMethod");
        PaintView paintView = findViewById(R.id.paintView);

        Button btnClear = findViewById(R.id.btnClear);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        btnClear.setOnClickListener((v)->{
            paintView.clear();
        });

        btnConfirm.setOnClickListener((v)->{
            //签名图片转为base64
            Bitmap cachebBitmap = paintView.getCachebBitmap();
            String base64 = EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(cachebBitmap));
            cachebBitmap.recycle();
            cachebBitmap = null;
            //回调js的方法
            EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, base64));
            finish();
        });
    }
}