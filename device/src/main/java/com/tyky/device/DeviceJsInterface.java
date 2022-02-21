package com.tyky.device;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.DeviceUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 是否为平板
     * @return
     */
    @JavascriptInterface
    public String isTablet() {
        boolean isTablet = DeviceUtils.isTablet();
        return gson.toJson(ResultModel.success(isTablet));
    }

    /**
     * 是否为模拟器
     * @return
     */
    @JavascriptInterface
    public String isEmulator() {
        boolean isEmulator = DeviceUtils.isEmulator();
        return gson.toJson(ResultModel.success(isEmulator));
    }

    /**
     * 获取设备唯一id
     * @return
     */
    @JavascriptInterface
    public String getUniqueDeviceId() {
        String uniqueDeviceId = DeviceUtils.getUniqueDeviceId();
        return gson.toJson(ResultModel.success(uniqueDeviceId));
    }

    /**
     * 获取当前设备系统信息
     * @return
     */
    @JavascriptInterface
    public String getDeviceInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("versionName", DeviceUtils.getSDKVersionName());//系统版本名
        map.put("versionCode", DeviceUtils.getSDKVersionCode());//系统版本号
        map.put("manufacturer", DeviceUtils.getManufacturer()); //设备厂商
        map.put("model", DeviceUtils.getModel()); //设备型号
        return gson.toJson(ResultModel.success(map));
    }

}
