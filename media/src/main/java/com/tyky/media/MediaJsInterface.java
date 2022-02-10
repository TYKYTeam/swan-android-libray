package com.tyky.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

@WebViewInterface("android_media")
public class MediaJsInterface {

    Gson gson = new Gson();

    /**
     * 复制文本到剪切板
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String copyTextToClipboard(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (content == null) {
            return gson.toJson(ResultModel.errorParam());
        }
        ClipboardUtils.copyText(content);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 获取剪切板文本
     */
    @JavascriptInterface
    public String getTextFromClipboard() {
        String result = ClipboardUtils.getText().toString();
        return gson.toJson(ResultModel.success(result));
    }

    /**
     * 打电话
     *
     * @param paramStr
     */
    @SuppressLint("MissingPermission")
    @JavascriptInterface
    public String callPhone(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String phone = paramModel.getContent();
        if (phone == null) {
            return gson.toJson(ResultModel.errorParam());
        }
        Activity topActivity = ActivityUtils.getTopActivity();
        String callPhonePermission = Permission.CALL_PHONE;
        AndPermission.with(topActivity)
                .runtime()
                .permission(callPhonePermission)
                .onDenied(null)
                .onGranted(data -> {
                    PhoneUtils.call(phone);
                })
                .start();

        //ClipboardUtils.copyText(phone);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 打电话
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String goToCall(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (content == null) {
            return gson.toJson(ResultModel.errorParam());
        }
        PhoneUtils.dial(content);
        return gson.toJson(ResultModel.success(""));
    }

    @JavascriptInterface
    public String testCallBack(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String callbackMethod = paramModel.getCallBackMethod();
        if (callbackMethod == null) {
            return gson.toJson(ResultModel.errorParam());
        }
        EventBus.getDefault().post(new JsCallBackEvent(callbackMethod,ResultModel.success("测试。。")));
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 预览文件（不包含图片）
     */
    @JavascriptInterface
    public void previewFile() {

    }


}
