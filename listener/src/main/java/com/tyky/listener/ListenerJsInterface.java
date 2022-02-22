package com.tyky.listener;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import androidx.annotation.RequiresPermission;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

@WebViewInterface("listener")
public class ListenerJsInterface {

    Gson gson = GsonUtils.getGson();
    private NetWorkListener netWorkListener;

    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String registerNetworkListener(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);

        String method = paramModel.getCallBackMethod();

        if (!StringUtils.isEmpty(method) && method.contains(",")) {
            String[] split = method.split(",");
            String callback1 = split[0];//网络断开的回调方法
            String callback2 = split[1];//网络连接的回调方法
            if (netWorkListener == null) {
                netWorkListener = new NetWorkListener(callback1, callback2);
                NetworkUtils.registerNetworkStatusChangedListener(netWorkListener);
            }
            //不为空，说明之前已经注册过了，没必要重新注册监听

            return gson.toJson(ResultModel.success(""));
        } else {
            return gson.toJson(ResultModel.errorParam());
        }

    }


}
