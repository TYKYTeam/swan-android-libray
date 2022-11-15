package com.tyky.ocr;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

@WebViewInterface("ocr")
public class OcrJsInterface {

    /**
     * 文字识别
     */
    @JavascriptInterface
    public String generalBasicOCR(String param) {
        ParamModel paramModel = GsonUtils.fromJson(param, ParamModel.class);
        String callBackMethod = paramModel.getCallBackMethod();
        Integer type = paramModel.getType();
        if (type == null || type == 0) {
            //跳转到图片选择页面（含拍照），然后再调用接口
        } else {
            Activity topActivity = ActivityUtils.getTopActivity();
            String filePath = "/sdcard/Download/Snipaste_2022-11-14_19-03-20.jpg";
            GeneralBasicParams generalBasicParams = new GeneralBasicParams();
            generalBasicParams.setDetectDirection(true);
            generalBasicParams.setImageFile(new File(filePath));
            OCR.getInstance(topActivity).recognizeGeneralBasic(generalBasicParams, new OnResultListener<GeneralResult>() {
                @Override
                public void onResult(GeneralResult generalResult) {
                    EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, generalResult));
                }

                @Override
                public void onError(OCRError ocrError) {
                    EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, ocrError.getMessage()));
                }
            });
        }
        return GsonUtils.toJson(ResultModel.success(""));
    }

    /**
     * 身份证正面拍照识别
     */
    @JavascriptInterface
    public String idcardOCROnlineFront(String param) {
        return GsonUtils.toJson(ResultModel.success(""));
    }

    /**
     * 身份证反面拍照识别
     */
    @JavascriptInterface
    public String idcardOCROnlineBack(String param) {
        return GsonUtils.toJson(ResultModel.success(""));
    }
}
