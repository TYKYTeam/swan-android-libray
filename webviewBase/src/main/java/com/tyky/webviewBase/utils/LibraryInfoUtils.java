package com.tyky.webviewBase.utils;

import com.blankj.utilcode.util.MetaDataUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryInfoUtils {
    //获取当前依赖模块（在app里的AndroidManifest中定义）
    private static List<String> baseLibraryDependencyList = null;
    //获取当前依赖版本（在app里的AndroidManifest中定义）
    private static String baseLibraryVersion = null;

    /**
     * 获取当前所有基座模块数组
     *
     * @return
     */
    public static List<String> getCurrentLibraryDependencyList() {
        if (baseLibraryDependencyList == null) {
            String baseLibraryDependency = MetaDataUtils.getMetaDataInApp("base_library_dependency");
            String[] baseLibraryDependencyArr = baseLibraryDependency.split(",");
            baseLibraryDependencyList = new ArrayList<>();
            Collections.addAll(baseLibraryDependencyList, baseLibraryDependencyArr);
        }
        return baseLibraryDependencyList;
    }

    /**
     * 获取当前基座库版本
     *
     * @return
     */
    public static String getCurrentLibraryVersion() {
        if (baseLibraryVersion == null) {
            baseLibraryVersion = MetaDataUtils.getMetaDataInApp("base_library_version");
        }
        return baseLibraryVersion;
    }


}
