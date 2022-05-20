package com.tyky.listener;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tyky.webviewBase.event.JsCallBackEvent;

import org.greenrobot.eventbus.EventBus;

public class NetWorkListener implements NetworkUtils.OnNetworkStatusChangedListener {


    @Override
    public void onDisconnected() {
        String disconnectCallbackMethodName = SPUtils.getInstance().getString("disconnectCallbackMethodName", "");
        if (!StringUtils.isTrimEmpty(disconnectCallbackMethodName)) {
            EventBus.getDefault().post(new JsCallBackEvent(disconnectCallbackMethodName, ""));
        }
    }

    @Override
    public void onConnected(NetworkUtils.NetworkType networkType) {
        String connectCallbackMethodName = SPUtils.getInstance().getString("connectCallbackMethodName", "");
        if (!StringUtils.isTrimEmpty(connectCallbackMethodName)) {
            EventBus.getDefault().post(new JsCallBackEvent(connectCallbackMethodName, ""));
        }
    }
}
