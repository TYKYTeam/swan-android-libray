package com.tyky.webviewBase.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.PhoneUtils;
import com.socks.library.KLog;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tyky.webviewBase.event.UrlLoadFinishEvent;

import org.greenrobot.eventbus.EventBus;

public class CustomWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        KLog.d("页面加载结束", url);
        EventBus.getDefault().post(new UrlLoadFinishEvent());
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        KLog.d("页面开始加载", url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
        KLog.d("页面onPageCommitVisible",url);

    }

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

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        //取消ssl证书验证，避免加载白屏
        if (sslErrorHandler != null) {
            sslErrorHandler.proceed();
        }
    }
}
