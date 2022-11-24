package com.tyky.baiduSpeechRecognition;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("baiduSpeechRecognition")
public class BaiduSpeechRecognitionJsInterface {

    /**
     * 语音识别
     *
     * @return
     */
    @JavascriptInterface
    public String speechRecognition(String param) {
        ParamModel paramModel = GsonUtils.fromJson(param, ParamModel.class);
        String callBackMethod = paramModel.getCallBackMethod();
        if (StringUtils.isEmpty(callBackMethod)) {
            return GsonUtils.toJson(ResultModel.errorParam("callBackMethod参数未传！"));
        }
        Bundle bundle = new Bundle();
        bundle.putString("callBackMethod", callBackMethod);
        ActivityUtils.startActivity(bundle, VoiceActivity.class);
        return GsonUtils.toJson(ResultModel.success(""));
    }
}
