package com.tyky.webviewBase.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.tyky.webviewBase.R;
import com.tyky.webviewBase.constants.RequestCodeConstants;
import com.tyky.webviewBase.view.CustomWebView;
import com.tyky.webviewBase.view.CustomWebViewChrome;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CustomWebViewActivity extends AppCompatActivity {

    //String url = "https://stars-one.site/";
    String url = "https://www.wenshushu.cn/";
    //String url = "http://10.232.241.118:8080/#/pages/index/index";
    private CustomWebView customWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_webview);

        AndPermission.with(this).runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(permissions -> {

                })
                .onDenied(permission -> {

                }).start();
        

        customWebView = findViewById(R.id.webview);
        customWebView.loadUrl(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode >= RequestCodeConstants.PHOTO && requestCode <= RequestCodeConstants.FILE) {
            CustomWebViewChrome customWebViewChrome = customWebView.getCustomWebViewChrome();
            customWebViewChrome.handleDataFromIntent(requestCode, resultCode, data);
        } else if (requestCode == 45) {
            Uri data1 = data.getData();
            Log.d("--test", "onActivityResult: " + data1);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 设置加载的url并加载
     * @param path
     */
    public  void loadLocalUrl(String path) {
        String url = "file:///android_asset"+path;
        loadWebUrl(url);
    }

    /**
     * 加载url
     * @param url
     */
    public void loadWebUrl(String url) {
        this.url = url;
        customWebView.loadUrl(url);
    }

}