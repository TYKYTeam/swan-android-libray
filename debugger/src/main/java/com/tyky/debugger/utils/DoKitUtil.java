package com.tyky.debugger.utils;

import com.blankj.utilcode.util.SPUtils;
import com.didichuxing.doraemonkit.DoKit;
import com.didichuxing.doraemonkit.kit.core.DoKitManager;
import com.didichuxing.doraemonkit.kit.toolpanel.KitWrapItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DoKitUtil {

    static List<String> openMenuNameList = new ArrayList<>();

    //平台工具和视觉工具，移除掉
    static String[] titles = {"dk_category_platform", "dk_category_ui","悬浮窗模式"};

    //用来判断是否已经修改了菜单，之后不再设置（属于性能优化）
    static boolean isChange = false;

    public static void initData() {

        if (openMenuNameList.isEmpty()) {
            openMenuNameList.add("App信息");
            openMenuNameList.add("沙盒浏览");
            //openMenuNameList.add("H5任意门");
            openMenuNameList.add("清理缓存");
            openMenuNameList.add("日志");
            openMenuNameList.add("帧率");
            openMenuNameList.add("CPU");
            openMenuNameList.add("内存");
            openMenuNameList.add("Crash");
            openMenuNameList.add("卡顿");
            openMenuNameList.add("启动耗时");
        }
    }

    /**
     * 修改菜单数据，隐藏某些菜单
     */
    private static void changeMenuData() {
        //String jsonPath = PathUtils.getInternalAppFilesPath() + "/system_kit_bak_" + BuildConfig.DOKIT_VERSION + ".json";
        //String result = FileIOUtils.readFile2String(jsonPath);
        //KLog.d("本地存储数据：" + result);

        if (!isChange) {
            LinkedHashMap<String, List<KitWrapItem>> globalSystemKits = DoKitManager.GLOBAL_KITS;
            //移除了两个大菜单
            for (String title : titles) {
                globalSystemKits.remove(title);
            }

            for (Map.Entry<String, List<KitWrapItem>> stringListEntry : globalSystemKits.entrySet()) {
                List<KitWrapItem> kitWrapItems = stringListEntry.getValue();
                //KLog.d("Dokit数据：" + stringListEntry.getKey() + ":" + kitWrapItems);
                for (KitWrapItem kitWrapItem : kitWrapItems) {
                    kitWrapItem.setChecked(false);
                    if (openMenuNameList.contains(kitWrapItem.getName())) {
                        kitWrapItem.setChecked(true);
                    }
                }
            }
            isChange = true;
        }
    }

    public static void setOpenOption(boolean flag) {
        SPUtils.getInstance().put("dokitOpen", flag);
        //初始化开放功能的菜单
        initData();
        changeMenuData();
        if (flag) {
            DoKit.show();
        } else {
            DoKit.hide();
        }
    }

    public static boolean getOpenOption() {
        return SPUtils.getInstance().getBoolean("dokitOpen", false);
    }
}
