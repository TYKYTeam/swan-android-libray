package com.tyky.debugger.utils;

import com.blankj.utilcode.util.SPUtils;
import com.didichuxing.doraemonkit.DoKit;

public class DoKitUtil {
    public static void setOpenOption(boolean flag) {
        SPUtils.getInstance().put("dokitOpen", flag);
        if (flag) {
            DoKit.show();
        } else {
            DoKit.hide();
        }
    }

    public static boolean getOpenOption() {
        return SPUtils.getInstance().getBoolean("dokitOpen", false);
    }
}
