package com.tyky.logupload;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

public class CrashInitializer implements Initializer<Void> {
    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        //Context application = context.getApplicationContext();
        //if (application == null) {
        //    application = SpiderManUtils.getApplicationByReflect();
        //}
        //初始化框架
        CrashUploader.init();
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
