package com.tyky.debugger;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.tyky.debugger.utils.DoKitUtil;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
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
     * 是否开启调试模式
     * @param param
     * @return
     */
    @JavascriptInterface
    public String openDebuggerMode(String param) {
        ParamModel paramModel = gson.fromJson(param, ParamModel.class);
        Integer type = paramModel.getType();
        DoKitUtil.setOpenOption(type==1);
        return gson.toJson(ResultModel.success(""));
    }
}
