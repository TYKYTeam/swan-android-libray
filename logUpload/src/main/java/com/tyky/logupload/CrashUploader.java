package com.tyky.logupload;

import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MetaDataUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.socks.library.KLog;
import com.tyky.logupload.activity.CrashActivity;
import com.tyky.logupload.activity.ErrorTipActivity;
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
                //调用上传日志
                //uploadLog(crashModel);

                //先存本地，之后再进行上传
                CrashModel crashModel = SpiderManUtils.parseCrash(ex);
                File file = new File(PathUtils.getExternalAppCachePath(), System.currentTimeMillis() + ".log");
                FileIOUtils.writeFileFromString(file, GsonUtils.toJson(crashModel));
                KLog.d("文件写入" + file.getPath());

                showActivity(file.getPath());
                AppUtils.exitApp();
            }
        });
    }

    /**
     * 展示异常错误
     *
     * @param filePath 日志的本地路径
     */
    public static void showActivity(String filePath) {

        Activity topActivity = ActivityUtils.getTopActivity();
        String isProdEnv = MetaDataUtils.getMetaDataInApp("is_prod_env");
        boolean flag = Boolean.parseBoolean(isProdEnv);

        if (!flag) {
            //ActivityUtils.startActivity(MyTestActivity.class);
            //测试环境
            Intent intent = new Intent(topActivity.getApplicationContext(), CrashActivity.class);
            intent.putExtra(CrashActivity.CRASH_FILE_PATH, filePath);
            //intent.putExtra(CrashActivity.CRASH_MODEL, crashModel);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            topActivity.getApplicationContext().startActivity(intent);
        } else {
            //正式环境
            Intent intent = new Intent(topActivity.getApplicationContext(), ErrorTipActivity.class);
            intent.putExtra(CrashActivity.CRASH_FILE_PATH, filePath);
            ActivityUtils.startActivity(intent);
        }
        topActivity.finish();
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

    public static void uploadLog(String path, UploadListener listener) {
        //String url = "http://183.62.130.45:35603/mobile/swanPkgSystem/swan-business/crash/uploadCrash";
        String url = "http://10.232.107.44:9060/swan-business/crash/uploadCrash";
        File file = new File(path);
        if (file.exists()) {
            String data = FileIOUtils.readFile2String(file);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), data);
            Request build = new Request.Builder()
                    .url(url)
                    .post(requestBody).build();
            ThreadUtils.getIoPool().execute(() -> new OkHttpClient().newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    FileUtils.rename(file, "upload_error_" + file.getName());
                    if (listener != null) {
                        listener.onUploadError(file);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //更改文件名，作为标识，避免重复上传（在启动APP的时候，也会重新检测并重新上传）
                    FileUtils.rename(file, "upload_success_" + file.getName());
                    KLog.d("日志上传成功：" + file.getPath());
                    if (listener != null) {
                        listener.onUploadSuccess(file);
                    }
                }
            }));
        }
    }

    /**
     * 日志上传接口
     */
    public interface UploadListener {
        /**
         * 上传成功
         * @param file
         */
        void onUploadSuccess(File file);

        /**
         * 上传失败
         * @param file
         */
        void onUploadError(File file);
    }
}
