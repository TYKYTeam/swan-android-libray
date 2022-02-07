package com.tyky.webviewBase.view;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ToastUtils;

public class WebViewJavaScript {
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

}
