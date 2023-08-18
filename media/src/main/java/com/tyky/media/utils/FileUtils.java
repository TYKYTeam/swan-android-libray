package com.tyky.media.utils;

import com.blankj.utilcode.util.PathUtils;

import java.io.File;

public class FileUtils {
    /**
     * 从url中获取文件名
     */
    public static String parseUrlFileName(String url) {
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
        if (fileName.contains("=")) {
            fileName = fileName.substring(fileName.indexOf("=") + 1);
        }
        return fileName;
    }

    /**
     * 获取文件
     */
    public static File getFile(String fileName) {
        return new File(PathUtils.getExternalAppFilesPath(), fileName);
    }

    /**
     * 获取文件
     */
    public static File getFile(String fileName, String relatePath) {
        return new File(PathUtils.getExternalAppFilesPath(),  "/" + relatePath + "/" +  fileName);
    }

    /**
     * 替换文件名后缀
     */
    public static String getTransformFileName(File file, String suffix) {
        String name = file.getName();
        String prefix = name.substring(0, name.indexOf('.') + 1);
        return prefix + suffix;
    }
}
