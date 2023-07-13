package com.tyky.media;

import com.tyky.media.utils.FileUtils;
import com.tyky.media.utils.HotfixUtils;
import com.tyky.webviewBase.BaseApplication;

import java.io.File;

public class MediaApplication extends BaseApplication {

    @Override
    public void onCreate() {
        replaceMethod();
        super.onCreate();
    }

    private void replaceMethod() {
        File patchDir = FileUtils.getFile("patch");
        if (!patchDir.exists()) {
            patchDir.mkdirs();
        }
        try {
            new HotfixUtils().doHotFix(this);
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
