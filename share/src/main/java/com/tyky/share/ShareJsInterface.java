package com.tyky.share;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("share")
public class ShareJsInterface {

    Gson gson = new Gson();

    /**
     * 分享文本
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String shareText(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        Intent shareTextIntent = IntentUtils.getShareTextIntent(content);
        ActivityUtils.getTopActivity().startActivity(shareTextIntent);

        return gson.toJson(ResultModel.success(""));
    }
}
