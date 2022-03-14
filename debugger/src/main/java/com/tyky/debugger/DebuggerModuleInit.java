package com.tyky.debugger;

import android.app.Application;

import com.blankj.utilcode.util.MetaDataUtils;
import com.tencent.bugly.Bugly;
import com.tyky.webviewBase.annotation.ApplicationInit;
import com.tyky.webviewBase.interf.ModuleInit;

@ApplicationInit
public class DebuggerModuleInit implements ModuleInit {

    @Override
    public void init(Application application) {
        //Bugly初始化
        String appId = MetaDataUtils.getMetaDataInApp("BUGLY_APPID");
        //最后的参数代表是否开启debuger模式
        Bugly.init(application,appId,true);
    }
}
