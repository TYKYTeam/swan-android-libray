package com.tyky.baiduSpeechRecognition;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("baiduSpeechRecognition")
public class BaiduSpeechRecognitionJsInterface {

    /**
     * 语音识别
     * @return
     */
    @JavascriptInterface
    public String speechRecognition(String param) {
        ActivityUtils.startActivity(VoiceActivity.class);
        return GsonUtils.toJson(ResultModel.success(""));
    }
}
