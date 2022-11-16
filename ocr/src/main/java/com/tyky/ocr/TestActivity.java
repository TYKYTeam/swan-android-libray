package com.tyky.ocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.socks.library.KLog;
import com.tyky.ocr.bean.IdCardInfoBack;
import com.tyky.ocr.bean.IdCardInfoFront;
import com.tyky.webviewBase.event.JsCallBackEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 7102;

    public static final String IS_FRONT = "isFront";
    public static final String CALLBACK_METHOD = "callBackMethod";

    String callBackMethod = "";
    boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        callBackMethod = getIntent().getStringExtra(CALLBACK_METHOD);
        isFront = getIntent().getBooleanExtra(IS_FRONT, true);

        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(), new CameraNativeHelper.CameraNativeInitCallback() {
            @Override
            public void onError(int errorCode, Throwable e) {
                String msg;
                switch (errorCode) {
                    case CameraView.NATIVE_SOLOAD_FAIL:
                        msg = "加载so失败，请确保apk中存在ui部分的so";
                        break;
                    case CameraView.NATIVE_AUTH_FAIL:
                        msg = "授权本地质量控制token获取失败";
                        break;
                    case CameraView.NATIVE_INIT_FAIL:
                        msg = "本地质量控制";
                        break;
                    default:
                        msg = String.valueOf(errorCode);
                }
                ToastUtils.showShort("本地质量控制初始化错误，错误原因： " + msg);
            }

            @Override
            public void onSuccess() {
                //初始化，跳转拍照页面
                startIdCard(isFront);
            }
        });

    }

    File tempFile = new File(PathUtils.getExternalAppDcimPath(), "pic.jpg");

    /**
     * 正面身份证扫描
     *
     * @param isFront 是否正面
     */
    private void startIdCard(boolean isFront) {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, tempFile.getPath());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
        // 请手动使用CameraNativeHelper初始化和释放模型
        // 推荐这样做，可以避免一些activity切换导致的不必要的异常
        intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
        //是身份证正面扫描还是反面
        if (isFront) {
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        } else {
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
        }
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    /**
     * 调用身份证
     *
     * @param idCardSide
     */
    private void recIDCard(String idCardSide) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(tempFile);
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);
        param.setDetectRisk(true);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    //转为对应的参数
                    if (idCardSide.equals(IDCardParams.ID_CARD_SIDE_FRONT)) {
                        //身份证正面识别信息
                        IdCardInfoFront idCardInfoFront = IdCardInfoFront.fromIdCardResult(result);
                        EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, idCardInfoFront));
                    } else {
                        //身份证反面识别信息
                        IdCardInfoBack idCardInfoFront = IdCardInfoBack.fromIdCardResult(result);
                        EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, idCardInfoFront));
                    }
                } else {
                    EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, "解析失败"));
                }
                finish();
            }

            @Override
            public void onError(OCRError error) {
                EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, error));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                KLog.d("身份证正反面：" + contentType);

                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK);
                    }
                }
            }
        }
    }
}