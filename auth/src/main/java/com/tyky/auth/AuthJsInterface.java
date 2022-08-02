package com.tyky.auth;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("auth")
public class AuthJsInterface {
    Gson gson = GsonUtils.getGson();
    /**
     * 手势解锁
     */
    @JavascriptInterface
    public String  gestureUnlocking(String paramStr) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        ActivityUtils.startActivity(bundle, UnLockActivity.class);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 设置手势解锁
     */
    @JavascriptInterface
    public String setGesture(String paramStr) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        ActivityUtils.startActivity(bundle, UnLockActivity.class);
        return gson.toJson(ResultModel.success(""));
    }
}
