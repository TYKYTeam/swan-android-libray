package com.tyky.webviewBase.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.PhoneUtils;

import androidx.annotation.RequiresApi;

public class CustomWebViewClient extends WebViewClient {
    /**
     * 重写方法,避免跳转到系统自带浏览器打开网址
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        handleUrl(view, url);
        return true;
    }

    /**
     * 5.0版本以上都走这个方法
     *
     * @param view
     * @param request
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        handleUrl(view, url);
        return true;
    }

    @SuppressLint("MissingPermission")
    private void handleUrl(WebView view, String url) {
        String phone = url.substring(4);
        if (url.contains("tel")) {
            PhoneUtils.dial(phone);
        } else if (url.contains("sms")) {
            PhoneUtils.sendSms(phone, "");
        } else {
            view.loadUrl(url);
        }
    }
}
