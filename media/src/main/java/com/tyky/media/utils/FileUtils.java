package com.tyky.media.utils;

import com.blankj.utilcode.util.PathUtils;

import java.io.File;

public class FileUtils {
    /**
     * 从url中获取文件名
     */
    public static String parseUrlFileName(String url) {
        /**
         * http://wuhantaiji.tpddns.cn:8008/projectlibrary-business/fileServer/downloadFile?filePath=/2023/6/26/202306261528193AED92EB.docx&fileName=平板登录页需求.docx",
         */
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
        String[] params = fileName.split("&");
        for (String param : params) {
            if (!param.contains("=")) {
                continue;
            }
            if (!param.contains(".")) {
                continue;
            }

            fileName = fileName.substring(fileName.indexOf("=") + 1);
            break;
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
