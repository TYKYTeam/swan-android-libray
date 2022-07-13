package com.baidu.idl.face.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.idl.face.example.manager.QualityConfigManager;
import com.baidu.idl.face.example.model.Const;
import com.baidu.idl.face.example.model.QualityConfig;
import com.baidu.idl.face.example.utils.SharedPreferencesUtil;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.listener.IInitCallback;
import com.baidu.idl.face.platform.ui.BaseActivity;
import com.tyky.baiduface.BaiduFaceModuleInit;
import com.tyky.baiduface.R;

/**
 * 首页面
 * Created by v_liujialu01 on 2020/3/24.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private CheckBox isCheckBox;
    private Context mContext;
    private boolean mIsInitSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        addActionLive();
        initView();
        initLicense();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放
        FaceSDKManager.getInstance().release();
    }

    private void addActionLive() {
        // 根据需求添加活体动作
        BaiduFaceModuleInit.livenessList.clear();
        BaiduFaceModuleInit.livenessList.add(LivenessTypeEnum.Eye);
        BaiduFaceModuleInit.livenessList.add(LivenessTypeEnum.Mouth);
        BaiduFaceModuleInit.livenessList.add(LivenessTypeEnum.HeadRight);
    }

    private void initLicense() {
        boolean success = setFaceConfig();
        if (!success) {
            showToast("初始化失败 = json配置文件解析出错");
            return;
        }
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(mContext, "swanyddz-face-android",
                "idl-license.face-android", new IInitCallback() {
                    @Override
                    public void initSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "初始化成功");
                                //showToast("初始化成功");
                                mIsInitSuccess = true;
                            }
                        });
                    }

                    @Override
                    public void initFailure(final int errCode, final String errMsg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "初始化失败 = " + errCode + " " + errMsg);
                                showToast("初始化失败 = " + errCode + ", " + errMsg);
                                mIsInitSuccess = false;
                            }
                        });
                    }
                });
    }

    private void initView() {
        // 设置
        ImageView butSetting = (ImageView) findViewById(R.id.but_setting);
        butSetting.setOnClickListener(this);

        isCheckBox = (CheckBox) findViewById(R.id.is_check_box);

        TextView faceAgreement = (TextView) findViewById(R.id.face_agreement);
        faceAgreement.setOnClickListener(this);

        Button butStartGather = (Button) findViewById(R.id.but_start_gather);
        butStartGather.setOnClickListener(this);
    }

    /**
     * 参数配置方法
     */
    private boolean setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），也可以根据实际需求进行数值调整
        // 质量等级（0：正常、1：宽松、2：严格、3：自定义）
        // 获取保存的质量等级
        SharedPreferencesUtil util = new SharedPreferencesUtil(mContext);
        int qualityLevel = (int) util.getSharedPreference(Const.KEY_QUALITY_LEVEL_SAVE, -1);
        if (qualityLevel == -1) {
            qualityLevel = BaiduFaceModuleInit.qualityLevel;
        }
        // 根据质量等级获取相应的质量值（注：第二个参数要与质量等级的set方法参数一致）
        QualityConfigManager manager = QualityConfigManager.getInstance();
        manager.readQualityFile(mContext.getApplicationContext(), qualityLevel);
        QualityConfig qualityConfig = manager.getConfig();
        if (qualityConfig == null) {
            return false;
        }
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
        // 设置可检测的最小人脸阈值
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        // 设置可检测到人脸的阈值
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        // 设置闭眼阈值
        config.setEyeClosedValue(FaceEnvironment.VALUE_CLOSE_EYES);
        // 设置图片缓存数量
        config.setCacheImageNum(FaceEnvironment.VALUE_CACHE_IMAGE_NUM);
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.setLivenessTypeList(BaiduFaceModuleInit.livenessList);
        // 设置动作活体是否随机
        config.setLivenessRandom(BaiduFaceModuleInit.isLivenessRandom);
        // 设置开启提示音
        config.setSound(BaiduFaceModuleInit.isOpenSound);
        // 原图缩放系数
        config.setScale(FaceEnvironment.VALUE_SCALE);
        // 抠图宽高的设定，为了保证好的抠图效果，建议高宽比是4：3
        config.setCropHeight(FaceEnvironment.VALUE_CROP_HEIGHT);
        config.setCropWidth(FaceEnvironment.VALUE_CROP_WIDTH);
        // 抠图人脸框与背景比例
        config.setEnlargeRatio(FaceEnvironment.VALUE_CROP_ENLARGERATIO);
        // 检测超时设置
        config.setTimeDetectModule(FaceEnvironment.TIME_DETECT_MODULE);
        // 检测框远近比率
        config.setFaceFarRatio(FaceEnvironment.VALUE_FAR_RATIO);
        config.setFaceClosedRatio(FaceEnvironment.VALUE_CLOSED_RATIO);
        FaceSDKManager.getInstance().setFaceConfig(config);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        // 设置
        if (v.getId() == R.id.but_setting) {
            startActivity(new Intent(mContext, SettingActivity.class));
        }
        // 协议详情
        if (v.getId() == R.id.face_agreement) {
            startActivity(new Intent(mContext, FaceHomeAgreementActivity.class));
        }
        // 开始采集
        if (v.getId() == R.id.but_start_gather) {
            boolean checked = isCheckBox.isChecked();
            if (!checked) {
                showToast("请先同意《人脸验证协议》");
                return;
            }
            if (!mIsInitSuccess) {
                showToast("初始化中，请稍候...");
                return;
            }
            startCollect();
        }
    }

    public void startCollect() {
        if (BaiduFaceModuleInit.isActionLive) {
            Intent intent = new Intent(mContext, FaceLivenessExpActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, FaceDetectExpActivity.class);
            startActivity(intent);
        }
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(mContext, "",
                Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }
}
