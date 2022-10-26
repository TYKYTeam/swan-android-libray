package com.tyky.imagecrop;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.core.graphics.drawable.IconCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.imagecrop.camera.TestActivity;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("imgprocess")
public class ImgProJsInterface {

    Gson gson = GsonUtils.getGson();

    @JavascriptInterface
    public String setImgProcess(String param) {

        ParamModel paramModel = GsonUtils.fromJson(param, ParamModel.class);
        String content = paramModel.getCallBackMethod();
        if (TextUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }

        SPUtils.getInstance().put("method_img", paramModel.getCallBackMethod());

        SPUtils.getInstance().put("method_imgtype", paramModel.getType() == null ? 1 : paramModel.getType());

        ActivityUtils.startActivity(TestActivity.class);

        return gson.toJson(ResultModel.success(""));  //不传值也是通用的，异步操作

    }



}
