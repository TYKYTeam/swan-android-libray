package com.tyky.webviewBase;

import android.app.Application;

import com.blankj.utilcode.util.ThreadUtils;
import com.kongzue.dialogx.DialogX;
import com.tyky.webviewBase.annotation.ApplicationInit;
import com.tyky.webviewBase.utils.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ThreadUtils.getCpuPool().execute(new Runnable() {
            @Override
            public void run() {
                List<Class<?>> classes = ReflectUtil.scanClassListByAnnotation(BaseApplication.this, "com.tyky", ApplicationInit.class);

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
                }

                //初始化dialog
                DialogX.init(BaseApplication.this);
            }
        });

    }
}
