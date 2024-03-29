package com.tyky.logupload.utils;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class CrashCatcherHelper implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashCatcherHelper";

    private static Thread.UncaughtExceptionHandler mAlreadyExistedExceptionHandler;

    private static final List<OnCrashListener> onCrashListeners = new ArrayList<>();

    private CrashCatcherHelper() {
        if (Thread.getDefaultUncaughtExceptionHandler() != null && Thread.getDefaultUncaughtExceptionHandler() != this) {
            mAlreadyExistedExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static void init() {
        new CrashCatcherHelper();
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        callbackCrash(t, ex);
        //transmitException(t, ex);
        //AppUtils.exitApp();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void addOnCrashListener(OnCrashListener listener) {
        onCrashListeners.add(listener);
    }

    public interface OnCrashListener {
        void onCrash(Thread t, Throwable ex);
    }

    /**
     * 回调处理
     * @param t
     * @param ex
     */
    private static void callbackCrash(Thread t, Throwable ex) {
        //将数据转为实体类对象
        if (!onCrashListeners.isEmpty()) {
            for (OnCrashListener onCrashListener : onCrashListeners) {
                onCrashListener.onCrash(t, ex);
            }
        }
    }

    /**
     * 如果存在
     * @param t
     * @param ex
     */
    private void transmitException(Thread t, Throwable ex) {
        if (mAlreadyExistedExceptionHandler == null) return;
        if (mAlreadyExistedExceptionHandler == this) return;
        mAlreadyExistedExceptionHandler.uncaughtException(t, ex);
    }
}