package com.tyky.media.activity;

import android.os.Bundle;

import com.tencent.smtt.sdk.WebSettings;
import com.tyky.media.R;
import com.tyky.webviewBase.view.CustomWebView;

import androidx.appcompat.app.AppCompatActivity;

public class OnlinePreviewActivity extends AppCompatActivity {

    private CustomWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_preview);
        String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);
        webView.loadUrl(url);
    }
}