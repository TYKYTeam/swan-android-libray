package com.tyky.tim;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.tencent.qcloud.tim.demo.SplashActivity;
import com.tyky.webviewBase.annotation.WebViewInterface;

@WebViewInterface("tim")
public class TimJsInterface {


    /**
     * 跳转到登录页面
     * @return
     */
    @JavascriptInterface
    public void gotoImLogin() {
        Activity topActivity = ActivityUtils.getTopActivity();
        Intent intent = new Intent(topActivity, SplashActivity.class);
        topActivity.startActivity(intent);
    }

    /**
     * 跳转到主页
     * @return
     */
    @JavascriptInterface
    public void gotoChatMainPage() {

    }
}
