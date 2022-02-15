package com.tyky.notification;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("notification")
public class NotificationJsInterface {

    Gson gson = new Gson();


    /**
     * 发送通知
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String sendNotification(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        String title = paramModel.getTitle();
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(title)) {
            return gson.toJson(ResultModel.errorParam());
        }
        NotifyUtil.sendNotification(title,content);
        return gson.toJson(ResultModel.success(""));
    }


}
