package com.tyky.media.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tyky.media.R;
import com.tyky.webviewBase.view.CustomWebView;

import androidx.appcompat.app.AppCompatActivity;

public class OnlinePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_preview);

        String url = getIntent().getStringExtra("url");
        CustomWebView webView = findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());

        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("file:///android_asset/index_old.html");
            }
        });

        //url = "https://stars-one.site";
        //if (!StringUtils.isEmpty(url)) {
        //}
        Button button = findViewById(R.id.btnTest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("点击。。");
                String my = "https://www.cnblogs.com/stars-one";
                webView.loadUrl(my);
            }
        });

    }
}