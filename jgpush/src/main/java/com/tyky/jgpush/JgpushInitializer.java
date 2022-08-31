package com.tyky.jgpush;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;
import cn.jpush.android.api.JPushInterface;

public class JgpushInitializer implements Initializer<Void> {
    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        //初始化jpush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(context);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
