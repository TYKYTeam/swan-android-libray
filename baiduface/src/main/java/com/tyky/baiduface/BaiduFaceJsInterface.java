package com.tyky.baiduface;

import android.webkit.JavascriptInterface;

import com.baidu.idl.face.example.HomeActivity;
import com.baidu.idl.face.example.manager.QualityConfigManager;
import com.baidu.idl.face.example.model.Const;
import com.baidu.idl.face.example.model.QualityConfig;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.tyky.bean.FaceParamModel;
import com.tyky.bean.GlobalConstant;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

import java.util.List;

@WebViewInterface("baiduface")
public class BaiduFaceJsInterface {
    Gson gson = GsonUtils.getGson();


    /**
     * 保存数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String facialRecognite(String paramStr) {
        FaceParamModel faceParamModel = gson.fromJson(paramStr, FaceParamModel.class);

        //todo 字段必填验证

        GlobalConstant.methodName = faceParamModel.getCallBackMethod();
        //设置人脸质量
        int qualityLevel = faceParamModel.getQualityLevel();
        if (qualityLevel == Const.QUALITY_NORMAL) {
            setFaceConfig(Const.QUALITY_NORMAL);
        } else if (qualityLevel == Const.QUALITY_LOW) {
            setFaceConfig(Const.QUALITY_LOW);
        } else if (qualityLevel == Const.QUALITY_HIGH) {
            setFaceConfig(Const.QUALITY_HIGH);
        }

        //设置人脸的相关配置
        BaiduFaceModuleInit.isLivenessRandom = faceParamModel.isIsLivenessRandom();
        BaiduFaceModuleInit.isOpenSound = faceParamModel.isIsOpenSound();
        BaiduFaceModuleInit.isActionLive = faceParamModel.isIsActionLive();

        //活体检测动作集合
        List<Integer> list = faceParamModel.getIivenessAction();
        List<LivenessTypeEnum> livenessList = BaiduFaceModuleInit.livenessList;
        livenessList.clear();

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == 0) {
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                }
                if (list.get(i) == 1) {
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                }
                if (list.get(i) == 2) {
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                }
                if (list.get(i) == 3) {
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                }
                if (list.get(i) == 4) {
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                }
                if (list.get(i) == 5) {
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                }
            }
        }

        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.setLivenessTypeList(BaiduFaceModuleInit.livenessList);
        // 设置动作活体是否随机
        config.setLivenessRandom(BaiduFaceModuleInit.isLivenessRandom);
        // 设置开启提示音
        config.setSound(BaiduFaceModuleInit.isOpenSound);
        FaceSDKManager.getInstance().setFaceConfig(config);

        ActivityUtils.startActivity(HomeActivity.class);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 参数配置方法
     */
    private void setFaceConfig(int qualityLevel) {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        config.setQualityLevel(qualityLevel);
        // 根据质量等级获取相应的质量值（注：第二个参数要与质量等级的set方法参数一致）
        QualityConfigManager manager = QualityConfigManager.getInstance();
        manager.readQualityFile(ActivityUtils.getTopActivity().getApplicationContext(), qualityLevel);
        QualityConfig qualityConfig = manager.getConfig();
        // 设置模糊度阈值
        config.setBlurnessValue(qualityConfig.getBlur());
        // 设置最小光照阈值（范围0-255）
        config.setBrightnessValue(qualityConfig.getMinIllum());
        // 设置最大光照阈值（范围0-255）
        config.setBrightnessMaxValue(qualityConfig.getMaxIllum());
        // 设置左眼遮挡阈值
        config.setOcclusionLeftEyeValue(qualityConfig.getLeftEyeOcclusion());
        // 设置右眼遮挡阈值
        config.setOcclusionRightEyeValue(qualityConfig.getRightEyeOcclusion());
        // 设置鼻子遮挡阈值
        config.setOcclusionNoseValue(qualityConfig.getNoseOcclusion());
        // 设置嘴巴遮挡阈值
        config.setOcclusionMouthValue(qualityConfig.getMouseOcclusion());
        // 设置左脸颊遮挡阈值
        config.setOcclusionLeftContourValue(qualityConfig.getLeftContourOcclusion());
        // 设置右脸颊遮挡阈值
        config.setOcclusionRightContourValue(qualityConfig.getRightContourOcclusion());
        // 设置下巴遮挡阈值
        config.setOcclusionChinValue(qualityConfig.getChinOcclusion());
        // 设置人脸姿态角阈值
        config.setHeadPitchValue(qualityConfig.getPitch());
        config.setHeadYawValue(qualityConfig.getYaw());
        config.setHeadRollValue(qualityConfig.getRoll());
        FaceSDKManager.getInstance().setFaceConfig(config);
    }
}
