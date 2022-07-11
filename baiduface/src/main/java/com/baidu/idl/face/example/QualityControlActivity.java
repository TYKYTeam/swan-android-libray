package com.baidu.idl.face.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.idl.face.example.manager.QualityConfigManager;
import com.baidu.idl.face.example.model.Const;
import com.baidu.idl.face.example.model.QualityConfig;
import com.baidu.idl.face.example.utils.SharedPreferencesUtil;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.ui.BaseActivity;
import com.tyky.baiduface.R;

/**
 * 质量控制设置
 */
public class QualityControlActivity extends BaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private static final String TAG = QualityControlActivity.class.getSimpleName();

    private RelativeLayout mRelativeLow;
    private RelativeLayout mRelativeNormal;
    private RelativeLayout mRelativeHigh;
    private RelativeLayout mRelativeCustom;

    private RadioButton mRadioLow;
    private RadioButton mRadioNormal;
    private RadioButton mRadioHigh;
    private RadioButton mRadioCustom;

    private TextView mTextLowEnter;
    private TextView mTextNormalEnter;
    private TextView mTextHighEnter;
    private TextView mTextCustomEnter;

    private String mSelectQuality;
    private Context mContext;
    private SharedPreferencesUtil mSharedPreferencesUtil;

    private int mIntentCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_control);
        mContext = this;
        initView();
        getIntentData();
        initListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIntentCount = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    private void initView() {
        // 返回键
        ImageView buttonQualityReturn = (ImageView) findViewById(R.id.but_quality_return);
        buttonQualityReturn.setOnClickListener(this);
        mRelativeLow = (RelativeLayout) findViewById(R.id.relative_low);
        mRelativeNormal = (RelativeLayout) findViewById(R.id.relative_normal);
        mRelativeHigh = (RelativeLayout) findViewById(R.id.relative_high);
        mRelativeCustom = (RelativeLayout) findViewById(R.id.relative_custom);

        mRadioLow = (RadioButton) findViewById(R.id.radio_low);
        mRadioNormal = (RadioButton) findViewById(R.id.radio_normal);
        mRadioHigh = (RadioButton) findViewById(R.id.radio_high);
        mRadioCustom = (RadioButton) findViewById(R.id.radio_custom);

        mTextLowEnter = (TextView) findViewById(R.id.text_low_enter);
        mTextNormalEnter = (TextView) findViewById(R.id.text_normal_enter);
        mTextHighEnter = (TextView) findViewById(R.id.text_high_enter);
        mTextCustomEnter = (TextView) findViewById(R.id.text_custom_enter);
    }

    /**
     * 接受上一页面跳转传过来的值
     */
    private void getIntentData() {
        mSharedPreferencesUtil = new SharedPreferencesUtil(mContext);
        Intent intent = getIntent();
        if (intent != null) {
            String qualityLevel = intent.getStringExtra(Const.INTENT_QUALITY_LEVEL);
            mSelectQuality = qualityLevel;
            // 正常
            if (getResources().getString(R.string.setting_quality_normal_txt).equals(qualityLevel)) {
                mRadioNormal.setChecked(true);
                mTextNormalEnter.setVisibility(View.VISIBLE);
                // 宽松
            } else if (getResources().getString(R.string.setting_quality_low_txt).equals(qualityLevel)) {
                mRadioLow.setChecked(true);
                mTextLowEnter.setVisibility(View.VISIBLE);
                // 严格
            } else if (getResources().getString(R.string.setting_quality_high_txt).equals(qualityLevel)) {
                mRadioHigh.setChecked(true);
                mTextHighEnter.setVisibility(View.VISIBLE);
                // 自定义
            } else if (getResources().getString(R.string.setting_quality_custom_txt).equals(qualityLevel)) {
                mRadioCustom.setChecked(true);
                mTextCustomEnter.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initListener() {
        mRelativeLow.setOnClickListener(this);
        mRelativeNormal.setOnClickListener(this);
        mRelativeHigh.setOnClickListener(this);
        mRelativeCustom.setOnClickListener(this);
        mRadioLow.setOnCheckedChangeListener(this);
        mRadioNormal.setOnCheckedChangeListener(this);
        mRadioHigh.setOnCheckedChangeListener(this);
        mRadioCustom.setOnCheckedChangeListener(this);
        mTextLowEnter.setOnClickListener(this);
        mTextNormalEnter.setOnClickListener(this);
        mTextHighEnter.setOnClickListener(this);
        mTextCustomEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.relative_low) {       // 点击宽松
            disableOthers(R.id.radio_low);
            handleSelectStatus(Const.QUALITY_LOW);
        } else if (id == R.id.relative_normal) {    // 点击正常
            disableOthers(R.id.radio_normal);
            handleSelectStatus(Const.QUALITY_NORMAL);
        } else if (id == R.id.relative_high) {      // 点击严格
            disableOthers(R.id.radio_high);
            handleSelectStatus(Const.QUALITY_HIGH);
        } else if (id == R.id.relative_custom) {    // 点击自定义
            disableOthers(R.id.radio_custom);
            handleSelectStatus(Const.QUALITY_CUSTOM);
            startIntent(R.string.setting_quality_custom_params_txt);
        } else if (id == R.id.but_quality_return) { // 点击返回
            Intent intent = getIntent();
            intent.putExtra(Const.INTENT_QUALITY_LEVEL, mSelectQuality);
            setResult(Const.RESULT_QUALITY_CONTROL, intent);
            finish();
        } else if (id == R.id.text_low_enter) {       // 点击宽松跳转
            startIntent(R.string.setting_quality_low_params_txt);
        } else if (id == R.id.text_normal_enter) {    // 点击正常跳转
            startIntent(R.string.setting_quality_normal_params_txt);
        } else if (id == R.id.text_high_enter) {      // 点击严格跳转
            startIntent(R.string.setting_quality_high_params_txt);
        } else if (id == R.id.text_custom_enter) {    // 点击自定义跳转
            startIntent(R.string.setting_quality_custom_params_txt);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.radio_low) {
                handleSelectStatus(Const.QUALITY_LOW);
            }

            if (buttonView.getId() == R.id.radio_normal) {
                handleSelectStatus(Const.QUALITY_NORMAL);
            }

            if (buttonView.getId() == R.id.radio_high) {
                handleSelectStatus(Const.QUALITY_HIGH);
            }

            if (buttonView.getId() == R.id.radio_custom) {
                handleSelectStatus(Const.QUALITY_CUSTOM);
            }
            disableOthers(buttonView.getId());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            intent.putExtra(Const.INTENT_QUALITY_LEVEL, mSelectQuality);
            setResult(Const.RESULT_QUALITY_CONTROL, intent);
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void handleSelectStatus(int qualityLevel) {
        if (qualityLevel == Const.QUALITY_NORMAL) {
            mRadioNormal.setChecked(true);
            mSelectQuality = mRadioNormal.getText().toString();
            mTextNormalEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_NORMAL);
            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_NORMAL);
        } else if (qualityLevel == Const.QUALITY_LOW) {
            mRadioLow.setChecked(true);
            mSelectQuality = mRadioLow.getText().toString();
            mTextLowEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_LOW);
            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_LOW);
        } else if (qualityLevel == Const.QUALITY_HIGH) {
            mRadioHigh.setChecked(true);
            mSelectQuality = mRadioHigh.getText().toString();
            mTextHighEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_HIGH);
            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_HIGH);
        } else if (qualityLevel == Const.QUALITY_CUSTOM) {
            mRadioCustom.setChecked(true);
            mSelectQuality = mRadioCustom.getText().toString();
            mTextCustomEnter.setVisibility(View.VISIBLE);
            setFaceConfig(Const.QUALITY_CUSTOM);
            mSharedPreferencesUtil.put(Const.KEY_QUALITY_LEVEL_SAVE, Const.QUALITY_CUSTOM);
        }
    }


    /**
     * 单选时，其它状态置成false
     * @param viewId
     */
    private void disableOthers(int viewId) {
        if (R.id.radio_low != viewId && mRadioLow.isChecked()) {
            mRadioLow.setChecked(false);
            mTextLowEnter.setVisibility(View.GONE);
        }

        if (R.id.radio_normal != viewId && mRadioNormal.isChecked()) {
            mRadioNormal.setChecked(false);
            mTextNormalEnter.setVisibility(View.GONE);
        }

        if (R.id.radio_high != viewId && mRadioHigh.isChecked()) {
            mRadioHigh.setChecked(false);
            mTextHighEnter.setVisibility(View.GONE);
        }

        if (R.id.radio_custom != viewId && mRadioCustom.isChecked()) {
            mRadioCustom.setChecked(false);
            mTextCustomEnter.setVisibility(View.GONE);
        }
    }

    /**
     * 跳转至参数配置页面
     */
    private void startIntent(int qualityId) {
        if (mIntentCount >= 1) {
            return;
        }
        mIntentCount++;
        Intent intent = new Intent(mContext, QualityParamsActivity.class);
        intent.putExtra(Const.INTENT_QUALITY_TITLE, getResources().getString(qualityId));
        startActivityForResult(intent, Const.REQUEST_QUALITY_PARAMS);
    }

    /**
     * 参数配置方法
     */
    private void setFaceConfig(int qualityLevel) {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        config.setQualityLevel(qualityLevel);
        // 根据质量等级获取相应的质量值（注：第二个参数要与质量等级的set方法参数一致）
        QualityConfigManager manager = QualityConfigManager.getInstance();
        manager.readQualityFile(mContext.getApplicationContext(), qualityLevel);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_QUALITY_PARAMS && resultCode == Const.RESULT_QUALITY_PARAMS) {
            if (data != null) {
                int qualityLevel = data.getIntExtra(Const.INTENT_QUALITY_LEVEL_PARAMS, Const.QUALITY_NORMAL);
                if (qualityLevel == Const.QUALITY_LOW) {
                    disableOthers(R.id.radio_low);
                } else if (qualityLevel == Const.QUALITY_NORMAL) {
                    disableOthers(R.id.radio_normal);
                } else if (qualityLevel == Const.QUALITY_HIGH) {
                    disableOthers(R.id.radio_high);
                } else if (qualityLevel == Const.QUALITY_CUSTOM) {
                    disableOthers(R.id.radio_custom);
                }
                handleSelectStatus(qualityLevel);
            }
        }
    }

}
