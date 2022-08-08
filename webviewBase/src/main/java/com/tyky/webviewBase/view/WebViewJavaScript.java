package com.tyky.webviewBase.view;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.WebviewEvent;
import com.tyky.webviewBase.model.ResultModel;

import org.greenrobot.eventbus.EventBus;

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
}
