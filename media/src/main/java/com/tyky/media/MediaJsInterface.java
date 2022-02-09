package com.tyky.media;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

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
    @JavascriptInterface
    public String callPhone(String paramStr) {
        Activity topActivity = ActivityUtils.getTopActivity();
        String callPhonePermission = Permission.CALL_PHONE;
        AndPermission.with(topActivity)
                .runtime()
                .permission(callPhonePermission)
                .onDenied(null).onGranted(data -> {
            ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
            String content = paramModel.getContent();
            PhoneUtils.call(content);
        })
                .start();

        //ClipboardUtils.copyText(content);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 打电话
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String goToCall(String paramStr) {
        Log.e("--test", "拨打电话");
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        PhoneUtils.dial(content);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 预览文件（不包含图片）
     */
    @JavascriptInterface
    public void previewFile() {

    }


}
