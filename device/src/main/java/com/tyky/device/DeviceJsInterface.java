package com.tyky.device;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.RomUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebViewInterface("device")
public class DeviceJsInterface {

    Gson gson = GsonUtils.getGson();

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
     *
     * @return
     */
    @JavascriptInterface
    public String isTablet() {
        boolean isTablet = DeviceUtils.isTablet();
        return gson.toJson(ResultModel.success(isTablet));
    }

    /**
     * 是否为模拟器
     *
     * @return
     */
    @JavascriptInterface
    public String isEmulator() {
        boolean isEmulator = DeviceUtils.isEmulator();
        return gson.toJson(ResultModel.success(isEmulator));
    }

    /**
     * 获取设备唯一id
     *
     * @return
     */
    @JavascriptInterface
    public String getUniqueDeviceId() {

        String uniqueDeviceId = DeviceUtils.getUniqueDeviceId();
        return gson.toJson(ResultModel.success(uniqueDeviceId));
    }

    /**
     * 获取当前设备系统信息
     *
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

    List<Method> methods = new ArrayList<>();

    /**
     * 判断是否为某个机型品牌
     *
     * @return
     */
    @JavascriptInterface
    public String isBrand(String json) {
        ParamModel paramModel = gson.fromJson(json, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isBlank(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        int allCount = methods.size();
        //如果没有初始化，则先初始化（通过反射）
        if (allCount == 0) {
            Method[] declaredMethods = RomUtils.class.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                String name = declaredMethod.getName();
                if (name.contains("is") && !name.equals("isRightRom")) {
                    methods.add(declaredMethod);
                }
            }
            allCount = methods.size();
        }

        //转为数字
        int type = Integer.parseInt(content);
        if (type > allCount || type==0) {
            return gson.toJson(ResultModel.errorParam("类型传值失败，请参考文档说明"));
        }
        try {
            Boolean flag = (Boolean) methods.get(type - 1).invoke(null, null);
            return gson.toJson(ResultModel.success(flag));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return gson.toJson(ResultModel.errorParam(e.getMessage()));
        }

    }

}
