package com.tyky.logupload;

import android.content.Context;

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.socks.library.KLog;

import java.io.File;
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
        //进入APP，筛选未上传成功的本地日志，在后面静默调用接口上传
        ThreadUtils.getIoPool().execute(() -> {
            File dirFile = new File(PathUtils.getExternalAppCachePath());
            File[] files = dirFile.listFiles(file -> {
                String name = file.getName();
                //筛选日志上传不成功的日志文件
                if (name.endsWith(".log")) {
                    return !name.startsWith("upload_success_");
                }
                return false;
            });

            for (File file : files) {
                CrashUploader.uploadLog(file.getPath(), new CrashUploader.UploadListener() {
                    @Override
                    public void onUploadSuccess(File file) {
                        KLog.d("上传成功，" + file.getPath());
                    }

                    @Override
                    public void onUploadError(File file) {
                        KLog.d(file.getPath() + " 上传失败，");

                    }
                });
            }
        });
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
