package com.tyky.device;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("device")
public class DeviceJsInterface {

    Gson gson = new Gson();

    /**
     * 获取mac地址
     */
    @JavascriptInterface
    public String getMacAddress() {
        String macAddress = DeviceUtils.getMacAddress();
        return gson.toJson(ResultModel.success(macAddress));
    }


}
