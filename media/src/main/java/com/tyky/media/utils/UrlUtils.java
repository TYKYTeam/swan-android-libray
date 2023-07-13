package com.tyky.media.utils;

import com.blankj.utilcode.util.PathUtils;

import java.io.File;

public class UrlUtils {
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
}
