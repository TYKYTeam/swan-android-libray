package com.tyky.media;

import android.webkit.JavascriptInterface;

import com.tyky.webviewBase.annotation.WebViewInterface;

@WebViewInterface("android_media")
public class MediaJsInterface {
    @JavascriptInterface
    public String test() {
        return "hello test！";
    }
}
