package com.tyky.map;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.IntentEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

@WebViewInterface("map")
public class MapJsInterface {

    Gson gson = GsonUtils.getGson();


    /**
     * 开始定位
     *
     * @return
     */
    @JavascriptInterface
    public String startLocation(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String methodName = paramModel.getCallBackMethod();
        if (StringUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        Context applicationContext = ActivityUtils.getTopActivity().getApplicationContext();
        if (AndPermission.hasPermissions(applicationContext, Permission.ACCESS_FINE_LOCATION)) {
            BaiduMapUtils.startBdLocation(methodName);
        } else {
            AndPermission.with(applicationContext).runtime()
                    .permission(Permission.ACCESS_FINE_LOCATION)
                    .onGranted(data -> BaiduMapUtils.startBdLocation(methodName))
                    .start();
            return gson.toJson(ResultModel.errorPermission());
        }
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 在地图显示当前位置
     * @return
     */
    @JavascriptInterface
    public String showLocationInMap() {
        EventBus.getDefault().post(new IntentEvent(MapActivity.class));
        return gson.toJson(ResultModel.success(""));
    }




}
