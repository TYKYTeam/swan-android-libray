package com.tyky.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tyky.share.utils.WxUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

public class ShareInitializer implements Initializer<Void> {


    private void regToWx(Context context) {
        String APP_ID = WxUtils.appId;

        // 通过 WXAPIFactory 工厂，获取 IWXAPI 的实例
        WxUtils.api = WXAPIFactory.createWXAPI(context, APP_ID, true);

        // 将应用的 appId 注册到微信
        WxUtils.api.registerApp(APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该 app 注册到微信
                WxUtils.api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

    }

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        regToWx(context);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
