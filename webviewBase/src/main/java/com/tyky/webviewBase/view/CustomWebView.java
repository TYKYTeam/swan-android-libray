package com.tyky.webviewBase.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.utils.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomWebView extends WebView {

    private CustomWebViewChrome customWebViewChrome;

    public CustomWebView(@NonNull Context context) {
        super(context);
        initConfig();
    }

    public CustomWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initConfig();
    }

    public CustomWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    private void initConfig() {
        WebSettings webSettings = getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccessFromFileURLs(true);
        // 视频播放需要使用
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        webSettings.setSupportZoom(false);//支持缩放
        requestFocusFromTouch();

        //遍历循环，筛选指定包名下含有指定注解的类，之后统一追加到webview中去
        List<Class<?>> classes = ReflectUtil.scanClassListByAnnotation(getContext(), "com.tyky", WebViewInterface.class);

        for (Class<?> aClass : classes) {
            String value = aClass.getAnnotation(WebViewInterface.class).value();
            Object o = null;
            try {
                o = aClass.newInstance();
                addJavascriptInterface(o, value);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        //跨域取消
        try {
            Class<?> clazz = getSettings().getClass();
            Method method = clazz.getMethod(
                    "setAllowUniversalAccessFromFileURLs", boolean.class);
            if (method != null) {
                method.invoke(getSettings(), true);
            }
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //addJavascriptInterface(new WebViewJavaScript(), "android");
        customWebViewChrome = new CustomWebViewChrome(this);
        setWebChromeClient(customWebViewChrome);
        setWebViewClient(new CustomWebViewClient());
    }

    public CustomWebViewChrome getCustomWebViewChrome() {
        return customWebViewChrome;
    }
}
