package com.tyky.auth;

import android.webkit.JavascriptInterface;

import com.tyky.webviewBase.annotation.WebViewInterface;

@WebViewInterface("auth")
public class AuthJsInterface {
    @JavascriptInterface
    public void auth() {

    }
}
