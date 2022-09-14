package com.tyky.autostart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.MetaDataUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

public class AutoStartInitializer implements Initializer<Void> {
    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        boolean openAutoStart = Boolean.parseBoolean(MetaDataUtils.getMetaDataInApp("openAutoStart"));
        //如果开启了功能，则需要申请悬浮窗权限
        if (openAutoStart) {
            //检查是否已经授予权限，大于6.0的系统适用，小于6.0系统默认打开，无需理会
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                //没有权限，须要申请权限，由于是打开一个受权页面，因此拿不到返回状态的，因此建议是在onResume方法中重新执行一次校验
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + AppUtils.getAppPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ToastUtils.showShort("设置开机自启需要授予悬浮窗权限！");
            }
        }

        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
