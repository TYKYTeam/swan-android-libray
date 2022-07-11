package com.baidu.idl.face.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.idl.face.example.model.Const;
import com.baidu.idl.face.example.utils.SharedPreferencesUtil;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.BaseActivity;
import com.tyky.baiduface.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SettingActivity extends BaseActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private static final int VALUE_MIN_ACTIVE_NUM = 1;  // 设置最少动作活体数量

    private Switch announcementsSwitch;
    private Switch liveDetectSwitch;
    private Switch actionliveSwitch;
    private CheckBox blinkCheckbox;
    private CheckBox leftTurnCheckbox;
    private CheckBox rightTurnCheckbox;
    private CheckBox nodCheckbox;
    private CheckBox lookUpCheckbox;
    private CheckBox openMouthCheckbox;
    private RelativeLayout relativeActionType;
    private RelativeLayout relativeActionRandom;
    private List<LivenessTypeEnum> livenessList = new ArrayList<>();
    private TextView mTextEnterQuality;

    private RelativeLayout mRelativeEye;
    private RelativeLayout mRelativeShake;
    private RelativeLayout mRelativeLeft;
    private RelativeLayout mRelativeRight;
    private RelativeLayout mRelativeNod;
    private RelativeLayout mRelativeUp;
    private RelativeLayout mRelativeMouth;
    private RelativeLayout mRelativeQuality;

    private SharedPreferencesUtil mSharedPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_setting);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        // 返回键
        ImageView butSettingReturn = (ImageView) findViewById(R.id.btnBack);
        butSettingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (livenessList.size() < VALUE_MIN_ACTIVE_NUM && liveDetectSwitch.isChecked()) {
                    showCustomToast("至少需要选择一项活体动作");
                    return;
                }
                finish();
            }
        });
        // 语音播报开关
        announcementsSwitch = (Switch) findViewById(R.id.announcements_switch);
        // 活体检测开关
        liveDetectSwitch = (Switch) findViewById(R.id.live_detect_switch);
        // 动作活体随机开关
        actionliveSwitch = (Switch) findViewById(R.id.actionlive_switch);
        relativeActionType = (RelativeLayout) findViewById(R.id.layout_active_type);
        relativeActionRandom = (RelativeLayout) findViewById(R.id.actionlive_layout);
        // 眨眨眼
        blinkCheckbox = (CheckBox) findViewById(R.id.actionlive_blink_checkbox);
        // 张嘴
        openMouthCheckbox = (CheckBox) findViewById(R.id.actionlive_open_mouth_checkbox);
        // 向右摇头
        rightTurnCheckbox = (CheckBox) findViewById(R.id.actionlive_right_turn_checkbox);
        // 向左摇头
        leftTurnCheckbox = (CheckBox) findViewById(R.id.actionlive_left_turn_checkbox);
        // 向上抬头
        lookUpCheckbox = (CheckBox) findViewById(R.id.actionlive_look_up_checkbox);
        // 点点头
        nodCheckbox = (CheckBox) findViewById(R.id.actionlive_nod_checkbox);
        // 左右摇头
        // shakeHeadCheckbox = (CheckBox) findViewById(R.id.actionlive_shake_head_checkbox);

        mRelativeEye = (RelativeLayout) findViewById(R.id.blink_layout);
        mRelativeShake = (RelativeLayout) findViewById(R.id.shake_head_layout);
        mRelativeLeft = (RelativeLayout) findViewById(R.id.left_turn_layout);
        mRelativeRight = (RelativeLayout) findViewById(R.id.right_turn_layout);
        mRelativeNod = (RelativeLayout) findViewById(R.id.nod_layout);
        mRelativeUp = (RelativeLayout) findViewById(R.id.look_up_layout);
        mRelativeMouth = (RelativeLayout) findViewById(R.id.open_mouth_layout);
        mRelativeQuality = (RelativeLayout) findViewById(R.id.quality_layout);

        blinkCheckbox.setTag(LivenessTypeEnum.Eye);
        // shakeHeadCheckbox.setTag(LivenessTypeEnum.HeadLeftOrRight);
        leftTurnCheckbox.setTag(LivenessTypeEnum.HeadLeft);
        rightTurnCheckbox.setTag(LivenessTypeEnum.HeadRight);
        nodCheckbox.setTag(LivenessTypeEnum.HeadDown);
        lookUpCheckbox.setTag(LivenessTypeEnum.HeadUp);
        openMouthCheckbox.setTag(LivenessTypeEnum.Mouth);
        mTextEnterQuality = (TextView) findViewById(R.id.text_enter_quality);

        settingChecked();
    }

    private void initData() {
        mSharedPreferencesUtil = new SharedPreferencesUtil(SettingActivity.this);
        int qualityLevel = (int) mSharedPreferencesUtil.getSharedPreference(Const.KEY_QUALITY_LEVEL_SAVE,
                -1);
        if (qualityLevel == -1) {
            // 默认正常
            mTextEnterQuality.setText(getResources().getString(R.string.setting_quality_normal_txt));
        } else {
            if (qualityLevel == Const.QUALITY_NORMAL) {
                mTextEnterQuality.setText(getResources().getString(R.string.setting_quality_normal_txt));
            } else if (qualityLevel == Const.QUALITY_LOW) {
                mTextEnterQuality.setText(getResources().getString(R.string.setting_quality_low_txt));
            } else if (qualityLevel == Const.QUALITY_HIGH) {
                mTextEnterQuality.setText(getResources().getString(R.string.setting_quality_high_txt));
            } else if (qualityLevel == Const.QUALITY_CUSTOM) {
                mTextEnterQuality.setText(getResources().getString(R.string.setting_quality_custom_txt));
            }
        }
    }

    private void settingChecked() {
        // 语音播报开关
        announcementsSwitch.setChecked(ExampleApplication.isOpenSound);
        // 活体检测开关
        liveDetectSwitch.setChecked(ExampleApplication.isActionLive);
        // 动作活体随机开关
        actionliveSwitch.setChecked(ExampleApplication.isLivenessRandom);
        if (!liveDetectSwitch.isChecked()) {
            relativeActionRandom.setVisibility(View.GONE);
            relativeActionType.setVisibility(View.GONE);
        } else {
            relativeActionRandom.setVisibility(View.VISIBLE);
            relativeActionType.setVisibility(View.VISIBLE);
        }

        List<LivenessTypeEnum> list = ExampleApplication.livenessList;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == LivenessTypeEnum.Eye) {
                    blinkCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.Mouth) {
                    openMouthCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadRight) {
                    rightTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadLeft) {
                    leftTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadUp) {
                    lookUpCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                }
                if (list.get(i) == LivenessTypeEnum.HeadDown) {
                    nodCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                }
//                if (list.get(i) == LivenessTypeEnum.HeadLeftOrRight) {
//                    shakeHeadCheckbox.setChecked(true);
//                    if (!livenessList.contains(LivenessTypeEnum.HeadLeftOrRight)) {
//                        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
//                    }
//                }
            }
        }
    }

    private void initListener() {
        liveDetectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    relativeActionRandom.setVisibility(View.GONE);
                    relativeActionType.setVisibility(View.GONE);
                } else {
                    relativeActionRandom.setVisibility(View.VISIBLE);
                    relativeActionType.setVisibility(View.VISIBLE);
                }
            }
        });

        mRelativeEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!blinkCheckbox.isChecked()) {
                    blinkCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    blinkCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Eye);
                }
            }
        });

        blinkCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blinkCheckbox.isChecked()) {
                    blinkCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Eye)) {
                        livenessList.add(LivenessTypeEnum.Eye);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        blinkCheckbox.setChecked(true);
//                        return;
//                    }
                    blinkCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Eye);
                }
            }
        });

//        mRelativeShake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!shakeHeadCheckbox.isChecked()) {
//                    shakeHeadCheckbox.setChecked(true);
//                    if (!livenessList.contains(LivenessTypeEnum.HeadLeftOrRight)) {
//                        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
//                    }
//                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
//                    shakeHeadCheckbox.setChecked(false);
//                    livenessList.remove(LivenessTypeEnum.HeadLeftOrRight);
//                }
//            }
//        });

//        shakeHeadCheckbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (shakeHeadCheckbox.isChecked()) {
//                    shakeHeadCheckbox.setChecked(true);
//                    if (!livenessList.contains(LivenessTypeEnum.HeadLeftOrRight)) {
//                        livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
//                    }
//                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        shakeHeadCheckbox.setChecked(true);
//                        return;
//                    }
//                    shakeHeadCheckbox.setChecked(false);
//                    livenessList.remove(LivenessTypeEnum.HeadLeftOrRight);
//                }
//            }
//        });

        mRelativeLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!leftTurnCheckbox.isChecked()) {
                    leftTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    leftTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadLeft);
                }
            }
        });

        leftTurnCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftTurnCheckbox.isChecked()) {
                    leftTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadLeft)) {
                        livenessList.add(LivenessTypeEnum.HeadLeft);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        leftTurnCheckbox.setChecked(true);
//                        return;
//                    }
                    leftTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadLeft);
                }
            }
        });

        mRelativeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rightTurnCheckbox.isChecked()) {
                    rightTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    rightTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadRight);
                }
            }
        });

        rightTurnCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightTurnCheckbox.isChecked()) {
                    rightTurnCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadRight)) {
                        livenessList.add(LivenessTypeEnum.HeadRight);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        rightTurnCheckbox.setChecked(true);
//                        return;
//                    }
                    rightTurnCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadRight);
                }
            }
        });

        mRelativeNod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nodCheckbox.isChecked()) {
                    nodCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    nodCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadDown);
                }
            }
        });

        nodCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nodCheckbox.isChecked()) {
                    nodCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadDown)) {
                        livenessList.add(LivenessTypeEnum.HeadDown);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        nodCheckbox.setChecked(true);
//                        return;
//                    }
                    nodCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadDown);
                }
            }
        });

        mRelativeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lookUpCheckbox.isChecked()) {
                    lookUpCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    lookUpCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadUp);
                }
            }
        });

        lookUpCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lookUpCheckbox.isChecked()) {
                    lookUpCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.HeadUp)) {
                        livenessList.add(LivenessTypeEnum.HeadUp);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        lookUpCheckbox.setChecked(true);
//                        return;
//                    }
                    lookUpCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.HeadUp);
                }
            }
        });

        mRelativeMouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!openMouthCheckbox.isChecked()) {
                    openMouthCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        return;
//                    }
                    openMouthCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Mouth);
                }
            }
        });

        openMouthCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openMouthCheckbox.isChecked()) {
                    openMouthCheckbox.setChecked(true);
                    if (!livenessList.contains(LivenessTypeEnum.Mouth)) {
                        livenessList.add(LivenessTypeEnum.Mouth);
                    }
                } else {
//                    if (livenessList.size() <= VALUE_MIN_ACTIVE_NUM) {
//                        showCustomToast("至少需要选择一项活体动作");
//                        openMouthCheckbox.setChecked(true);
//                        return;
//                    }
                    openMouthCheckbox.setChecked(false);
                    livenessList.remove(LivenessTypeEnum.Mouth);
                }
            }
        });

        mRelativeQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, QualityControlActivity.class);
                intent.putExtra(Const.INTENT_QUALITY_LEVEL, mTextEnterQuality.getText().toString().trim());
                startActivityForResult(intent, Const.REQUEST_QUALITY_CONTROL);
            }
        });
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        LivenessTypeEnum type = (LivenessTypeEnum) buttonView.getTag();
//        if (isChecked) {
//            if (!livenessList.contains(type)) {
//                livenessList.add(type);
//            }
//        } else {
//            livenessList.remove(type);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExampleApplication.livenessList.clear();
        Collections.sort(this.livenessList, new ComparatorValues());
        ExampleApplication.livenessList = this.livenessList;
        ExampleApplication.isLivenessRandom = actionliveSwitch.isChecked();
        ExampleApplication.isOpenSound = announcementsSwitch.isChecked();
        ExampleApplication.isActionLive = liveDetectSwitch.isChecked();
        setFaceConfig();
    }

    @Override
    public void onBackPressed() {
        if (livenessList.size() < VALUE_MIN_ACTIVE_NUM && liveDetectSwitch.isChecked()) {
            showCustomToast("至少需要选择一项活体动作");
            return;
        }
        super.onBackPressed();
    }

    /**
     * 参数配置方法
     */
    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // 设置活体动作，通过设置list，LivenessTypeEunm.Eye, LivenessTypeEunm.Mouth,
        // LivenessTypeEunm.HeadUp, LivenessTypeEunm.HeadDown, LivenessTypeEunm.HeadLeft,
        // LivenessTypeEunm.HeadRight
        config.setLivenessTypeList(ExampleApplication.livenessList);
        // 设置动作活体是否随机
        config.setLivenessRandom(ExampleApplication.isLivenessRandom);
        // 设置开启提示音
        config.setSound(ExampleApplication.isOpenSound);
        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    public static class ComparatorValues implements Comparator<LivenessTypeEnum> {

        @Override
        public int compare(LivenessTypeEnum object1, LivenessTypeEnum object2) {
            int m1 = object1.ordinal();
            int m2 = object2.ordinal();
            int result = 0;
            if (m1 > m2) {
                result = 1;
            } else if (m1 < m2) {
                result = -1;
            } else {
                result = 0;
            }
            return result;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_QUALITY_CONTROL
                && resultCode == Const.RESULT_QUALITY_CONTROL) {
            if (data != null) {
                mTextEnterQuality.setText(data.getStringExtra(Const.INTENT_QUALITY_LEVEL));
            }
        }
    }
}
