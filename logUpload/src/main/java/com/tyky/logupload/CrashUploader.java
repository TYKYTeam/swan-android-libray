package com.tyky.logupload;

import com.socks.library.KLog;
import com.tyky.logupload.bean.CrashModel;
import com.tyky.logupload.utils.CrashCatcherHelper;

public class CrashUploader {

    public static void init() {
        CrashCatcherHelper.init();
        CrashCatcherHelper.addOnCrashListener(new CrashCatcherHelper.OnCrashListener() {
            @Override
            public void onCrash(Thread t, Throwable ex, CrashModel crashModel) {
                KLog.e(t.getName());
                KLog.e(ex.getMessage());
                KLog.e(crashModel);
            }
        });
    }




}
