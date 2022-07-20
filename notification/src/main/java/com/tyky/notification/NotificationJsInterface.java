package com.tyky.notification;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.notification.bean.NotificationParamModel;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("notification")
public class NotificationJsInterface {

   Gson gson = GsonUtils.getGson();


    /**
     * 发送通知
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String sendNotification(String paramStr) {
        NotificationParamModel paramModel = gson.fromJson(paramStr, NotificationParamModel.class);
        String content = paramModel.getContent();
        String title = paramModel.getTitle();
        String url = paramModel.getUrl();
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(title) || StringUtils.isEmpty(url)) {
            return gson.toJson(ResultModel.errorParam());
        }
        NotifyUtil.sendNotification(title,content,url);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 设置桌面小红点
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String setBargeCount(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        int count = Integer.parseInt(content);
        Activity topActivity = ActivityUtils.getTopActivity();
        BadgeUtils.setCount(topActivity, count);
        return gson.toJson(ResultModel.success(""));
    }
}
