package com.baidu.idl.face.example;

import android.app.Activity;
import android.app.Application;

import com.baidu.idl.face.example.model.Const;
import com.baidu.idl.face.platform.LivenessTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExampleApplication extends Application {
    // 动作活体条目集合
    public static List<LivenessTypeEnum> livenessList = new ArrayList<>();
    // 活体随机开关
    public static boolean isLivenessRandom = true;
    // 语音播报开关
    public static boolean isOpenSound = true;
    // 活体检测开关
    public static boolean isActionLive = true;
    // 质量等级（0：正常、1：宽松、2：严格、3：自定义）
    public static int qualityLevel = Const.QUALITY_NORMAL;

    private static Map<String, Activity> destroyMap = new HashMap<>();

    /**
     * 添加到销毁队列
     * @param activity 要销毁的activity
     */
    public static void addDestroyActivity(Activity activity, String activityName) {
        destroyMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destroyMap.keySet();
        for (String key : keySet) {
            destroyMap.get(key).finish();
        }
    }
}
