package com.tyky.debugger;

import android.content.Context;

import com.blankj.utilcode.util.MetaDataUtils;
import com.tencent.bugly.Bugly;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

 public class DebuggerInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        //Bugly初始化
        String appId = MetaDataUtils.getMetaDataInApp("BUGLY_APPID");
        //最后的参数代表是否开启debuger模式
        Bugly.init(context,appId,true);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
