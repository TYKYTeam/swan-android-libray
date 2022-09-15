package com.tyky.autostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.MetaDataUtils;
import com.blankj.utilcode.util.ToastUtils;


public class AutoStartReceiver extends BroadcastReceiver {
    private final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        ToastUtils.showShort(intent.getAction());
        Log.e("---test", intent.getAction());

        //开机启动
        if (ACTION.equals(intent.getAction())) {
            boolean openAutoStart = Boolean.parseBoolean(MetaDataUtils.getMetaDataInApp("openAutoStart"));
            if (openAutoStart) {

                String mainPkgName = MetaDataUtils.getMetaDataInApp("mainPkgName");
                String mainPage = MetaDataUtils.getMetaDataInApp("mainPage");

                Intent thisIntent = IntentUtils.getComponentIntent(mainPkgName, mainPage, true);

                //是否为横屏
                boolean isLandscape = Boolean.parseBoolean(MetaDataUtils.getMetaDataInApp("isLandscape"));
                thisIntent.putExtra("isLandscape", isLandscape);

                thisIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                thisIntent.setAction("android.intent.action.MAIN");
                thisIntent.addCategory("android.intent.category.LAUNCHER");
                context.startActivity(thisIntent);
            } else {
                Log.d("AutoStartReceiver", "未开启自动开机自启功能！！");
            }
            //Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage("com.tyky.nhmanager");
            //context.startActivity(launchIntentForPackage);
        }
    }
}