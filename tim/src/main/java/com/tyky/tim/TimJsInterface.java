package com.tyky.tim;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tencent.qcloud.tim.demo.SplashActivity;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("tim")
public class TimJsInterface {

    Gson gson = GsonUtils.getGson();

    /**
     * 跳转到登录页面
     * @return
     */
    @JavascriptInterface
    public String gotoImLogin() {
        Activity topActivity = ActivityUtils.getTopActivity();
        Intent intent = new Intent(topActivity, SplashActivity.class);
        topActivity.startActivity(intent);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * IM登录
     * @return
     */
    @JavascriptInterface
    public String login(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String userId = paramModel.getUserId();
        String userSig = paramModel.getUserSig();
        boolean gotoPage = paramModel.isGotoPage();

        if (StringUtils.isEmpty(userId)) {
            return gson.toJson(ResultModel.errorParam());
        }

        TIMLoginUtils.login(userId,userSig,gotoPage);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * IM退出
     * @return
     */
    @JavascriptInterface
    public String login() {
        TIMLoginUtils.logout();
        return gson.toJson(ResultModel.success(""));
    }

}
