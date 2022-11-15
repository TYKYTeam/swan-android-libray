package com.tyky.ocr;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.blankj.utilcode.util.MetaDataUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

public class OcrInitializer implements Initializer<Void> {
    //百度云官网获取的 API Key
    final String ak = MetaDataUtils.getMetaDataInApp("baidu_ocr_api_key");
    //百度云官网获取的 Securet Key
    final String sk = MetaDataUtils.getMetaDataInApp("baidu_ocr_secret_key");

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        //初始化百度的OCR
        OCR.getInstance(context).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {

            }

            @Override
            public void onError(OCRError ocrError) {
                KLog.e("百度OCR初始化失败: ",ocrError.getMessage());
            }
        }, context, ak, sk);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
