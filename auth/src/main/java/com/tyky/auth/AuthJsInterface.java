package com.tyky.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("auth")
public class AuthJsInterface {
    Gson gson = GsonUtils.getGson();

    /**
     * 手势解锁
     */
    @JavascriptInterface
    public String gestureUnlocking(String paramStr) {
        //获取本地存储的密码
        String lockPwd = SPUtils.getInstance().getString("lockPwd");
        if (TextUtils.isEmpty(lockPwd)) {
            return gson.toJson(ResultModel.errorParam("请先设置手势密码"));
        }

        //构造传递参数的bundle容器，传到activity中
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putString("colorParam", paramStr);
        ActivityUtils.startActivity(bundle, UnLockActivity.class);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 设置手势解锁
     */
    @JavascriptInterface
    public String setGesture(String paramStr) {
        //构造传递参数的bundle容器，传到activity中
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putString("colorParam", paramStr);
        ActivityUtils.startActivity(bundle, UnLockActivity.class);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 电子签名
     */
    @JavascriptInterface
    public String signature(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        //参数验证
        String callBackMethod = paramModel.getCallBackMethod();
        if (TextUtils.isEmpty(callBackMethod)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //构造传递参数的bundle容器，传到activity中
        Bundle bundle = new Bundle();
        bundle.putString("callBackMethod",callBackMethod);
        ActivityUtils.startActivity(bundle,SignatureActivity.class);
        return gson.toJson(ResultModel.success(""));
    }


}
