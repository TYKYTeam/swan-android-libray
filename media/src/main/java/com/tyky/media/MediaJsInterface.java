package com.tyky.media;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ClipboardUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

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
     * 预览文件（不包含图片）
     */
    @JavascriptInterface
    public void previewFile() {

    }


}
