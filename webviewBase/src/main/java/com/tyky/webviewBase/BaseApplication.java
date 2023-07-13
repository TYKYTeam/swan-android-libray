package com.tyky.webviewBase;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.kongzue.dialogx.DialogX;
import com.socks.library.KLog;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

public class BaseApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        /*List<Class<?>> classes = ReflectUtil.scanClassListByAnnotation(BaseApplication.this, "com.tyky", ApplicationInit.class);

        for (Class<?> aClass : classes) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if ("init".equals(declaredMethod.getName())) {
                    try {
                        Object o = aClass.newInstance();
                        declaredMethod.invoke(o, BaseApplication.this);

                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/

        // 系统android版本低于android6的使用腾讯X5内核
        if (android.os.Build.VERSION.SDK_INT <= 23) {
            initX5();
        }

        //解决android 7.0以上版本 exposed beyond app through ClipData.Item.getUri()问题
        // 即共享文件时；

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        //初始化dialog
        DialogX.init(BaseApplication.this);
    }

    private void initX5() {

        /* 设置允许移动网络下进行内核下载。默认不下载，会导致部分一直用移动网络的用户无法使用x5内核 */
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.setCoreMinVersion(QbSdk.CORE_VER_ENABLE_202112);

        /* SDK内核初始化周期回调，包括 下载、安装、加载 */
        QbSdk.setTbsListener(new TbsListener() {

            /**
             * @param stateCode 用户可处理错误码请参考{@link com.tencent.smtt.sdk.TbsCommonCode}
             */
            @Override
            public void onDownloadFinish(int stateCode) {
                KLog.d("onDownloadFinished: " + stateCode);
            }

            /**
             * @param stateCode 用户可处理错误码请参考{@link com.tencent.smtt.sdk.TbsCommonCode}
             */
            @Override
            public void onInstallFinish(int stateCode) {
                KLog.d("onInstallFinished: " + stateCode);
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            @Override
            public void onDownloadProgress(int progress) {
                KLog.d("Core Downloading: " + progress);
            }
        });

        /* 此过程包括X5内核的下载、预初始化，接入方不需要接管处理x5的初始化流程，希望无感接入 */
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖wifi网络下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * 内核下发请求发起有24小时间隔，卸载重装、调整系统时间24小时后都可重置
             * 调试阶段建议通过 WebView 访问 debugtbs.qq.com -> 安装线上内核 解决
             * @param isX5 是否使用X5内核
             */
            @Override
            public void onViewInitFinished(boolean isX5) {
                KLog.d("onViewInitFinished: " + isX5);
                // hint: you can use QbSdk.getX5CoreLoadHelp(context) anytime to get help.
            }
        });
    }

    public static Context getAppContext() {
        return context;
    }

}
