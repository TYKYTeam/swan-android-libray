package com.tencent.qcloud.tim.demo;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.tencent.qcloud.tim.demo.login.LoginForDevActivity;
import com.tyky.webviewBase.annotation.WebViewInterface;

@WebViewInterface("debugger")
public class TimJsInterface {


    /**
     * 跳转到登录页面
     * @return
     */
    @JavascriptInterface
    public void gotoImLogin() {
        Activity topActivity = ActivityUtils.getTopActivity();
        Intent intent = new Intent(topActivity, LoginForDevActivity.class);
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
