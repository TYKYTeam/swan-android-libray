package com.tyky.logupload.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.tyky.logupload.R;
import com.tyky.logupload.bean.CrashModel;
import com.tyky.webviewBase.utils.LibraryInfoUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SpiderManUtils {

    /**
     * 把Throwable解析成CrashModel实体
     */
    public static CrashModel parseCrash(Throwable ex) {
        CrashModel model = new CrashModel();
        try {
            model.setEx(ex);
            model.setTime(new Date().getTime());
            if (ex.getCause() != null) {
                ex = ex.getCause();
            }
            model.setExceptionMsg(ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.flush();
            String exceptionType = ex.getClass().getName();

            StackTraceElement element = parseThrowable(ex);
            if (element == null) return model;

            model.setLineNumber(element.getLineNumber());
            model.setClassName(element.getClassName());
            model.setFileName(element.getFileName());
            model.setMethodName(element.getMethodName());
            model.setExceptionType(exceptionType);

            model.setFullException(sw.toString());
            //设置版本基座版本信息
            model.setBaseLibraryVersion(LibraryInfoUtils.getCurrentLibraryVersion());
            model.setPackageName(AppUtils.getAppPackageName());
            model.setVersionCode(AppUtils.getAppVersionCode());
            model.setVersionName(AppUtils.getAppVersionName());
        } catch (Exception e) {
            return model;
        }
        return model;
    }

    /**
     * 把Throwable解析成StackTraceElement
     */
    public static StackTraceElement parseThrowable(Throwable ex) {
        if (ex == null || ex.getStackTrace().length == 0) return null;
        StackTraceElement element;
        String packageName = AppUtils.getAppPackageName();
        for (StackTraceElement ele : ex.getStackTrace()) {
            if (ele.getClassName().contains(packageName)) {
                element = ele;
                return element;
            }
        }
        element = ex.getStackTrace()[0];
        return element;
    }


    /**
     * 用反射获取Application
     */
    public static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("you should init first");
            }
            return (Application) app;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException("you should init first");
    }


    /**
     * 获取分享的文本
     */
    public static String getShareText(CrashModel model) {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm", Locale.getDefault());

        Activity context = ActivityUtils.getTopActivity();
        builder.append(context.getString(R.string.simpleCrashInfo))
                .append("\n")
                .append(model.getExceptionMsg())
                .append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append(context.getString(R.string.simpleClassName))
                .append(model.getFileName()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append(context.getString(R.string.simpleFunName)).append(model.getMethodName()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append(context.getString(R.string.simpleLineNum)).append(model.getLineNumber()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append(context.getString(R.string.simpleExceptionType)).append(model.getExceptionType()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append(context.getString(R.string.simpleTime)).append(df.format(model.getTime())).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        CrashModel.Device device = model.getDevice();

        builder.append(context.getString(R.string.simpleModel)).append(model.getDevice().getModel()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append(context.getString(R.string.simpleBrand)).append(model.getDevice().getBrand()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        String platform = "Android " + device.getRelease() + "-" + device.getVersion();
        builder.append(context.getString(R.string.simpleVersion)).append(platform).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append("CPU-ABI:").append(device.getCpuAbi()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append("versionCode:").append(model.getVersionCode()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append("versionName:").append(model.getVersionName()).append("\n");
        builder.append("\n");//空一行，好看点，(#^.^#)

        builder.append(context.getString(R.string.simpleAllInfo))
                .append("\n")
                .append(model.getFullException()).append("\n");

        return builder.toString();
    }

    /**
     * 保存文本到文件
     */
    public static void saveTextToFile(String text, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.flush();
        writer.close();
    }

    /**
     * 保存文本到文件
     */
    public static void saveTextToFile(String text, String path) throws IOException {
        File file = new File(path);
        saveTextToFile(text, file);
    }
}
