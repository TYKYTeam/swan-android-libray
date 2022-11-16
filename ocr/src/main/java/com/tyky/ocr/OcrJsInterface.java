package com.tyky.ocr;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.socks.library.KLog;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.tyky.webviewBase.view.GlideEngine;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        String content = paramModel.getContent();
        GeneralBasicParams generalBasicParams = new GeneralBasicParams();
        generalBasicParams.setDetectDirection(true);

        if (type == null) {
            type = 0;
        }

        //默认情况，content参数可为空
        if (type != 0) {
            if (StringUtils.isEmpty(content)) {
                return GsonUtils.toJson(ResultModel.errorParam("content参数不能为空"));
            }
        }
        if (StringUtils.isEmpty(callBackMethod)) {
            return GsonUtils.toJson(ResultModel.errorParam("callBackMethod参数不能为空"));
        }

        switch (type) {
            case 1:
                //本机的文件路径
                generalBasicParams.setDetectDirection(true);
                generalBasicParams.setImageFile(new File(content));
                break;
            case 2:
                //base64数据
                //判断如果有base64开头，处理一下
                if (content.contains("base64,")) {
                    content = org.apache.commons.lang3.StringUtils.substringAfter(content, "base64,");
                }
                byte[] bytes = EncodeUtils.base64Decode(content);
                Bitmap bitmap = ImageUtils.bytes2Bitmap(bytes);
                File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.PNG, true);
                generalBasicParams.setImageFile(file);
                generalBasicParams.setDetectDirection(true);
                break;
            default:
                //其他情况，都是打开进入页面选择照片（含拍照）来识别文字
                Activity topActivity = ActivityUtils.getTopActivity();
                PictureSelector.create(topActivity)
                        .openGallery(PictureMimeType.ofImage())
                        .queryMimeTypeConditions(PictureMimeType.ofJPEG(), PictureMimeType.ofPNG())
                        .maxSelectNum(1)
                        .isCamera(true)
                        .isPreviewImage(true)
                        .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                if (!result.isEmpty()) {
                                    LocalMedia localMedia = result.get(0);
                                    String filePath = localMedia.getRealPath();
                                    KLog.d("图片选择：", filePath);
                                    generalBasicParams.setDetectDirection(true);
                                    generalBasicParams.setImageFile(new File(filePath));
                                    startOcr(callBackMethod, generalBasicParams);
                                }
                            }

                            @Override
                            public void onCancel() {
                                ToastUtils.showShort("已取消选择图片");
                            }
                        });
                break;
        }
        return GsonUtils.toJson(ResultModel.success(""));
    }

    private void startOcr(String callBackMethod, GeneralBasicParams generalBasicParams) {
        Activity topActivity = ActivityUtils.getTopActivity();
        OCR.getInstance(topActivity).recognizeGeneralBasic(generalBasicParams, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                List<? extends WordSimple> wordList = generalResult.getWordList();
                List<String> result = new ArrayList<>();
                for (WordSimple wordSimple : wordList) {
                    result.add(wordSimple.getWords());
                }
                EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, result));
            }

            @Override
            public void onError(OCRError ocrError) {
                KLog.e("通用文字识别失败：" + ocrError.getMessage());
                EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, ocrError.getMessage()));
            }
        });
    }

    /**
     * 身份证正面拍照识别
     */
    @JavascriptInterface
    public String idcardOCROnlineFront(String param) {
        ParamModel paramModel = GsonUtils.fromJson(param, ParamModel.class);
        String callBackMethod = paramModel.getCallBackMethod();
        if (StringUtils.isEmpty(callBackMethod)) {
            return GsonUtils.toJson(ResultModel.errorParam());
        }

        Bundle bundle = new Bundle();
        bundle.putString(TestActivity.CALLBACK_METHOD, callBackMethod);
        bundle.putBoolean(TestActivity.IS_FRONT, true);
        ActivityUtils.startActivity(bundle, TestActivity.class);
        return GsonUtils.toJson(ResultModel.success(""));
    }

    /**
     * 身份证反面拍照识别
     */
    @JavascriptInterface
    public String idcardOCROnlineBack(String param) {
        ParamModel paramModel = GsonUtils.fromJson(param, ParamModel.class);
        String callBackMethod = paramModel.getCallBackMethod();
        if (StringUtils.isEmpty(callBackMethod)) {
            return GsonUtils.toJson(ResultModel.errorParam());
        }

        Bundle bundle = new Bundle();
        bundle.putString(TestActivity.CALLBACK_METHOD, callBackMethod);
        bundle.putBoolean(TestActivity.IS_FRONT, false);
        ActivityUtils.startActivity(bundle, TestActivity.class);
        return GsonUtils.toJson(ResultModel.success(""));
    }
}
