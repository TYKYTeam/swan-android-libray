package com.tyky.page;

import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("page")
public class PageJsInterface {

    Gson gson = GsonUtils.getGson();

    /**
     * 跳转指定APP的指定Activity页面
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String gotoApp(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String packageName = paramModel.getPackageName();
        String activityName = paramModel.getActivityName();
        if (StringUtils.isEmpty(packageName) || StringUtils.isEmpty(activityName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        Intent intent = new Intent();
        intent.setClassName(packageName, activityName);
        ActivityUtils.getTopActivity().startActivity(intent);

        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 使用浏览器打开H5链接
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String openUrlByBrowser(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String url = paramModel.getContent();
        if (StringUtils.isEmpty(url) && url.startsWith("http")) {
            return gson.toJson(ResultModel.errorParam("传递参数有误，要求是一个链接地址"));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        ActivityUtils.getTopActivity().startActivity(intent);
        return gson.toJson(ResultModel.success(""));
    }

}
