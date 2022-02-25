package com.tyky.debugger;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

public class DebuggerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Bugly初始化
        CrashReport.initCrashReport(getApplicationContext());
    }
}
