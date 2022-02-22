package com.tyky.listener;

import com.blankj.utilcode.util.NetworkUtils;
import com.tyky.webviewBase.event.JsCallBackEvent;

import org.greenrobot.eventbus.EventBus;

public class NetWorkListener implements NetworkUtils.OnNetworkStatusChangedListener {

    //网络未连接回调方法
    private String disconnectCallbackMethodName = "";
    private String connectCallbackMethodName = "";

    public NetWorkListener(String disconnectCallbackMethodName, String connectCallbackMethodName) {
        this.disconnectCallbackMethodName = disconnectCallbackMethodName;
        this.connectCallbackMethodName = connectCallbackMethodName;
    }

    @Override
    public void onDisconnected() {
        EventBus.getDefault().post(new JsCallBackEvent(disconnectCallbackMethodName,null));
    }

    @Override
    public void onConnected(NetworkUtils.NetworkType networkType) {
        EventBus.getDefault().post(new JsCallBackEvent(connectCallbackMethodName,null));
    }
}
