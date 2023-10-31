package com.tyky.webviewBase.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.socks.library.KLog;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.utils.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CustomWebView extends WebView {

    private CustomWebViewChrome customWebViewChrome;
    private CustomWebViewClient customWebViewClient;

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
            try {
                Object o = aClass.newInstance();
                addJavascriptInterface(o, value);
                KLog.d("模块：" + value);

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
        //设置ua
        String userAgentString = webSettings.getUserAgentString();
        webSettings.setUserAgentString(userAgentString + " tyky_android");

        //addJavascriptInterface(new WebViewJavaScript(), "android");
        customWebViewChrome = new CustomWebViewChrome(this);
        setWebChromeClient(customWebViewChrome);
        customWebViewClient = new CustomWebViewClient();
        setWebViewClient(customWebViewClient);

        //设置下载文件监听器
        setDownloadListener(new WebviewDownloader(getContext()));
    }

    /**
     * 设置ua
     *
     * @param ua
     */
    public void setUa(String ua) {
        WebSettings webSettings = getSettings();
        webSettings.setUserAgentString(ua + " tyky_android");
    }

    /**
     * 清除webview缓存，localstorage，历史记录和cookies
     */
    public void clearAllData() {
        //清除缓存
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookies(null);
        cookieManager.removeAllCookies(value -> KLog.d("清除缓存是否成功： " + value));
        cookieManager.flush();
        //清除保存在本地的所有内容（清空WebView的localStorage）
        WebStorage.getInstance().deleteAllData();
        //清除历史记录
        clearHistory();
        //清除缓存的数据
        clearCache(true);
    }

    public CustomWebViewChrome getCustomWebViewChrome() {
        return customWebViewChrome;
    }

    public CustomWebViewClient getCustomWebViewClient() {
        return customWebViewClient;
    }

}
