package com.tyky.debugger;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.tyky.webviewBase.annotation.ApplicationInit;
import com.tyky.webviewBase.interf.ModuleInit;

@ApplicationInit
public class DebuggerModuleInit implements ModuleInit {

    @Override
    public void init(Application application) {
        //Bugly初始化
        CrashReport.initCrashReport(application);
    }
}
