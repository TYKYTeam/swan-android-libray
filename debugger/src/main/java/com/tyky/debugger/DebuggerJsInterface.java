package com.tyky.debugger;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.gson.Gson;
import com.tyky.debugger.utils.DoKitUtil;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("debugger")
public class DebuggerJsInterface {
    Gson gson = GsonUtils.getGson();

    /**
     * 进入到调试设置页面
     * @return
     */
    @JavascriptInterface
    public String goSettingPage() {
        Activity topActivity = ActivityUtils.getTopActivity();
        Intent intent = new Intent(topActivity, SettingActivity.class);
        topActivity.startActivity(intent);

        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 开启调试悬浮球功能
     * @return
     */
    @JavascriptInterface
    public String openDebuggerMode() {
        ThreadUtils.runOnUiThread(() -> DoKitUtil.setOpenOption(true));
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 关闭调试悬浮球功能
     * @return
     */
    @JavascriptInterface
    public String closeDebuggerMode() {
        ThreadUtils.runOnUiThread(() -> DoKitUtil.setOpenOption(false));
        return gson.toJson(ResultModel.success(""));
    }
}
