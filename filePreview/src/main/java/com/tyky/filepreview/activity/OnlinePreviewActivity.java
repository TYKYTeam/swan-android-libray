package com.tyky.filepreview.activity;

import android.os.Bundle;
import android.view.View;

import com.tencent.smtt.sdk.WebSettings;
import com.tyky.filepreview.R;
import com.tyky.webviewBase.view.CustomWebView;

import androidx.appcompat.app.AppCompatActivity;

public class OnlinePreviewActivity extends AppCompatActivity {

    private CustomWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_preview);

        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webView.loadUrl(url);
    }
}