package com.tyky.debugger;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("debugger")
public class DebuggerJsInterface {
    Gson gson = GsonUtils.getGson();

    @JavascriptInterface
    public String goSettingPage() {
        Activity topActivity = ActivityUtils.getTopActivity();
        Intent intent = new Intent(topActivity, SettingActivity.class);
        topActivity.startActivity(intent);

        return gson.toJson(ResultModel.success(""));
    }
}
