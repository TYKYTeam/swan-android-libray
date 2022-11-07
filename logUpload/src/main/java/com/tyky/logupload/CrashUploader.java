package com.tyky.logupload;

import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.socks.library.KLog;
import com.tyky.logupload.activity.CrashActivity;
import com.tyky.logupload.bean.CrashModel;
import com.tyky.logupload.utils.CrashCatcherHelper;
import com.tyky.logupload.utils.SpiderManUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CrashUploader {

    static final String tag = "CrashUploader";

    public static void init() {
        CrashCatcherHelper.init();
        CrashCatcherHelper.addOnCrashListener(new CrashCatcherHelper.OnCrashListener() {
            @Override
            public void onCrash(Thread t, Throwable ex) {

                //todo 考虑先传本地，之后进入APP再进行上传
                CrashModel crashModel = SpiderManUtils.parseCrash(ex);
                KLog.d("转换中。。。");
                showActivity(0, crashModel);

                /*ThreadUtils.getIoPool().execute(new Runnable() {
                    @Override
                    public void run() {


                        File file = new File(PathUtils.getExternalAppCachePath(), "test.log");
                        FileIOUtils.writeFileFromString(file, GsonUtils.toJson(crashModel));
                        KLog.d("文件写入" + file.getPath());
                        AppUtils.exitApp();
                    }
                });*/

                //调用上传日志
                //uploadLog(crashModel);
            }
        });
    }

    /**
     * 展示异常错误
     *
     * @param type       0：测试环境 1：正式环境
     * @param crashModel
     */
    public static void showActivity(int type, CrashModel crashModel) {
        if (type == 0) {
            //ActivityUtils.startActivity(MyTestActivity.class);
            //测试环境
            Activity topActivity = ActivityUtils.getTopActivity();
            Intent intent = new Intent(topActivity.getApplicationContext(), CrashActivity.class);
            intent.putExtra(CrashActivity.CRASH_MODEL, crashModel);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            topActivity.getApplicationContext().startActivity(intent);
            topActivity.finish();
        } else {
            //正式环境
        }
    }

    public static void uploadLog(CrashModel crashModel) {
        //String url = "http://183.62.130.45:35603/mobile/swanPkgSystem/swan-business/crash/uploadCrash";
        String url = "http://10.232.107.44:9060/swan-business/crash/uploadCrash";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), GsonUtils.toJson(crashModel));
        Request build = new Request.Builder()
                .url(url)
                .post(requestBody).build();
        ThreadUtils.getIoPool().execute(() -> new OkHttpClient().newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                KLog.d(tag, e.getMessage());
                AppUtils.exitApp();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                KLog.d("请求成功");
                AppUtils.exitApp();
            }
        }));

    }

    public static void uploadLog() {
        //String url = "http://183.62.130.45:35603/mobile/swanPkgSystem/swan-business/crash/uploadCrash";
        String url = "http://localhost:9060/swan-business/crash/uploadCrash";
        File file = new File(PathUtils.getExternalAppCachePath(), "test.log");
        if (file.exists()) {
            String data = FileIOUtils.readFile2String(file);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data);
            Request build = new Request.Builder()
                    .url(url)
                    .post(requestBody).build();
            ThreadUtils.getIoPool().execute(() -> new OkHttpClient().newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    KLog.d(tag, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    KLog.d("请求成功");
                }
            }));
        }

    }


}
