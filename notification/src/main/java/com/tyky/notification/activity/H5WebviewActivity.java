package com.tyky.notification.activity;

import android.os.Bundle;

import com.tyky.notification.R;
import com.tyky.webviewBase.view.CustomWebView;

import androidx.appcompat.app.AppCompatActivity;

public class H5WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_webview);
        String url = getIntent().getStringExtra("url");
        CustomWebView view = (CustomWebView) findViewById(R.id.webview);
        view.loadUrl(url);

    }
}