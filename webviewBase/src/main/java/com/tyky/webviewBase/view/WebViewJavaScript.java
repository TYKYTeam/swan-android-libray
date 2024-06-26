package com.tyky.webviewBase.view;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.WebviewEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.tyky.webviewBase.utils.SpeechService;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

@WebViewInterface("webviewBase")
public class WebViewJavaScript {
    Gson gson = GsonUtils.getGson();
    /**
     * 设置视频文件录制的大小
     * @param videoSize
     */
    @JavascriptInterface
    public  void setVideoMaxSize(int videoSize) {
        VideoConfig.setVideoMaxSize(videoSize);
    }

    /**
     * 设置视频文件录制的时长
     * @param videoDuration
     */
    @JavascriptInterface
    public  void setVideoMaxDuration(int videoDuration) {
        VideoConfig.setVideoMaxDuration(videoDuration);
    }

    @JavascriptInterface
    public String test(String string) {
        ToastUtils.showShort(string);
        return "这是android返回结果";
    }

    /**
     * 清除webview的数据
     * @return
     */
    @JavascriptInterface
    public String clearWebviewData() {
        EventBus.getDefault().post(new WebviewEvent(1));
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * webview重载当前页面
     */
    @JavascriptInterface
    public String reloadWebview() {
        EventBus.getDefault().post(new WebviewEvent(2));
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 获取APP应用信息
     */
    @JavascriptInterface
    public String getAppInfo() {
        AppUtils.AppInfo appInfo = AppUtils.getAppInfo();
        Map<String,Object> map = new HashMap<>();
        map.put("appName", appInfo.getName());
        map.put("pkgName", appInfo.getPackageName());
        map.put("versionName", appInfo.getVersionName());
        map.put("versionCode", appInfo.getVersionCode());
        return gson.toJson(ResultModel.success(map));
    }

    /**
     * 获取APP应用信息
     */
    @JavascriptInterface
    public String setWebviewUa(String param) {
        ParamModel paramModel = gson.fromJson(param, ParamModel.class);
        String content = paramModel.getContent();
        EventBus.getDefault().post(new WebviewEvent(content,3));
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 文字转语音
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String speakTextInBase(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        SpeechService.speakText(content);
        return gson.toJson(ResultModel.success(""));
    }

}
