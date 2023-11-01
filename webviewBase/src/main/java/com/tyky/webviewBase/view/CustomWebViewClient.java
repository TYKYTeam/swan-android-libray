package com.tyky.webviewBase.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;

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
    private final CustomWebView webView;

    public CustomWebViewClient(CustomWebView webView) {
        this.webView = webView;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        KLog.d("onPageFinished: " + url);
        // 有重定向时，onPageFinished方法回执行多次，需要使用以下条件来判断页面是否真正加载完毕
        if (webView.getProgress() == 100) {
            EventBus.getDefault().post(new UrlLoadFinishEvent());
            webView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        KLog.d("onPageStarted" + url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
        KLog.d("页面onPageCommitVisible: " + url);
    }

    /**
     * 重写方法,避免跳转到系统自带浏览器打开网址
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        KLog.d("shouldOverrideUrlLoading: " + url);
        return handleUrl(view, url, null);
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
        KLog.d("shouldOverrideUrlLoading: " + url);
        return handleUrl(view, url, request);
    }

    @SuppressLint("MissingPermission")
    private boolean handleUrl(WebView view, String url, WebResourceRequest request) {
        String phone = url.substring(4);
        if (url.contains("tel:")) {
            PhoneUtils.dial(phone);
            return true;
        }
        if (url.contains("sms")) {
            PhoneUtils.sendSms(phone, "");
            return true;
        }
        if (!(url.startsWith("http") || url.startsWith("https"))) {
            return true;
        }
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        int hitType = hitTestResult.getType();
        if (hitType != WebView.HitTestResult.UNKNOWN_TYPE) {
            KLog.d("WebViewManger", "没有进行重定向操作");
            //这里执行自定义的操作
            view.loadUrl(url);
            return true;
        } else {
            KLog.d("WebViewManger", "进行了重定向操作");
            //重定向时hitType为0 ,执行默认的操作
            if (request == null) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view, request);
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
