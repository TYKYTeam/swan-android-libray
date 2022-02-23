package com.tyky.handware;

import android.content.Intent;
import android.content.IntentFilter;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("hardware")
public class HardwareJsInterface {
    Gson gson = GsonUtils.getGson();

    /**
     * 获取电量
     *
     * @return
     */
    @JavascriptInterface
    public String getBattery() {
        Intent intent = ActivityUtils.getTopActivity().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //判断它是否是为电量变化的Broadcast Action
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            //获取当前电量
            int level = intent.getIntExtra("level", 0);
            //电量的总刻度
            int scale = intent.getIntExtra("scale", 100);
            //把它转成百分比
            String result = ((level * 100) / scale) + "%";
            return gson.toJson(ResultModel.success(result));
        }
        return gson.toJson(ResultModel.errorParam(""));

    }



}
