package com.tyky.webviewBase;

import android.app.Application;

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

        //初始化dialog
        DialogX.init(BaseApplication.this);
    }
}
