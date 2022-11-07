package com.tyky.webviewBase;

import android.app.Application;
import android.os.StrictMode;

import com.kongzue.dialogx.DialogX;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

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

        //解决android 7.0以上版本 exposed beyond app through ClipData.Item.getUri()问题
        // 即共享文件时；

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        //初始化dialog
        DialogX.init(BaseApplication.this);
    }
}
