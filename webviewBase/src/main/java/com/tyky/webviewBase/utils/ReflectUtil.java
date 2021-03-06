package com.tyky.webviewBase.utils;

import android.content.Context;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class ReflectUtil {

    public static List<Class<?>> scanClassListByAnnotation(Context ctx, String entityPackage,Class<? extends Annotation> annotationClass) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            PathClassLoader classLoader = (PathClassLoader) Thread
                    .currentThread().getContextClassLoader();

            DexFile dex = new DexFile(ctx.getPackageResourcePath());
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement();
                if (entryName.contains(entityPackage)) {
                    Class<?> entryClass = classLoader.loadClass(entryName);
                    Annotation annotation = entryClass.getAnnotation(annotationClass);

                    if (annotation != null) {
                        classes.add(entryClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

}
