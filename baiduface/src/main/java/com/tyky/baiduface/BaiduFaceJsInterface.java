package com.tyky.baiduface;

import android.webkit.JavascriptInterface;

import com.baidu.idl.face.example.HomeActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("baiduface")
public class BaiduFaceJsInterface {
    Gson gson = GsonUtils.getGson();


    /**
     * 保存数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String facialRecognite(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        ActivityUtils.startActivity(HomeActivity.class);
        return gson.toJson(ResultModel.success(""));
    }


}
