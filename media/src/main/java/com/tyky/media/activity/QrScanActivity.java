package com.tyky.media.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.UriUtils;
import com.google.zxing.Result;
import com.king.zxing.CaptureActivity;
import com.king.zxing.DecodeConfig;
import com.king.zxing.DecodeFormatManager;
import com.king.zxing.analyze.MultiFormatAnalyzer;
import com.king.zxing.util.CodeUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.socks.library.KLog;
import com.tyky.media.R;
import com.tyky.webviewBase.constants.MediaModuleConstants;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.view.GlideEngine;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class QrScanActivity extends CaptureActivity {


    private String methodName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        methodName = getIntent().getStringExtra("methodName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.ibChoose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(QrScanActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .isCamera(false)
                        .isPreviewImage(true)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(1448);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1448) {
            if (resultCode == RESULT_OK) {
                List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
                if (result.size() > 0) {
                    LocalMedia localMedia = result.get(0);

                    String path = localMedia.getPath();

                    KLog.d("?????????????????????" + path);
                    int sdkVersionCode = DeviceUtils.getSDKVersionCode();
                    Uri uri = null;
                    if (sdkVersionCode >= 29) {
                        //android10????????????
                        uri = Uri.parse(path);
                    } else {
                        uri = Uri.fromFile(new File(path));
                    }

                    //Bitmap bitmap = imageSizeCompress(uri);
                    byte[] bytes = UriUtils.uri2Bytes(uri);
                    Bitmap bitmap = ImageUtils.bytes2Bitmap(bytes);
                    String text = CodeUtils.parseCode(bitmap);
                    bitmap.recycle();
                    bitmap = null;
                    KLog.d("???????????????????????????" + text);

                    dealResult(text);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * ???????????????????????????
     * @param text
     */
    private void dealResult(String text) {
        if (StringUtils.isEmpty(text)) {
            Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
        } else {
            if (StringUtils.isEmpty(methodName)) {
                //methodName??????????????????Activity?????????????????????????????????????????????Activity
                Intent intent = new Intent();
                intent.putExtra(MediaModuleConstants.REQUEST_QR_SCAN_RESULT, text);
                setResult(MediaModuleConstants.RESULT_QR_SCAN_CODE,intent);
                finish();
            } else {
                //????????????Activity???EventBus??????????????????js???????????????????????????H5
                finish();
                EventBus.getDefault().post(new JsCallBackEvent(methodName, text));
            }
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_scan;
    }

    @Override
    public void initCameraScan() {
        super.initCameraScan();

        //?????????????????????
        DecodeConfig decodeConfig = new DecodeConfig();
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS)//???????????????????????????????????????????????????????????????????????????????????????DecodeFormatManager.DEFAULT_HINTS
                .setFullAreaScan(false)//????????????????????????????????????false
                .setAreaRectRatio(0.8f)//?????????????????????????????????0.8????????????????????????????????????????????????????????????????????????????????????????????????
                .setAreaRectVerticalOffset(0)//???????????????????????????????????????????????????0??????0??????????????????????????????
                .setAreaRectHorizontalOffset(0);//???????????????????????????????????????????????????0??????0??????????????????????????????

        //????????????????????????????????????????????????????????????
        getCameraScan()
                .setVibrate(true)//??????????????????????????????false
                .setNeedAutoZoom(true)//?????????????????????????????????????????????false
                .setAnalyzer(new MultiFormatAnalyzer(decodeConfig));//???????????????,??????????????????????????????????????????????????????????????????????????????????????????
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ??????????????????
     *
     * @param result
     * @return ??????false???????????????????????????????????????????????????????????????????????????
     * ??????true??????????????????????????????????????????isAnalyze???true?????????????????????????????????????????????????????????
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    public boolean onScanResultCallback(Result result) {
        /*
         * ??????setAnalyzeImage????????????????????????????????????????????????
         *
         * 1. ???????????????????????????true?????????????????????????????????true?????????
         * ?????????????????????????????????????????????????????????????????????getCameraScan().setAnalyzeImage(false)???
         * ??????????????????????????????????????????????????????getCameraScan().setAnalyzeImage(true)????????????????????????
         *
         * 2. ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * ??????getCameraScan().setAnalyzeImage(false)????????????????????????
         */
        String text = result.getText();
        KLog.d("????????????????????????"+text);
        dealResult(text);

        getCameraScan().setAnalyzeImage(false);
        return false;
    }
}