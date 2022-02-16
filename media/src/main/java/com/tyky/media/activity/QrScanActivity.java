package com.tyky.media.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ImageUtils;
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
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ResultModel;
import com.tyky.webviewBase.view.GlideEngine;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

                    KLog.d("版本号：" + DeviceUtils.getSDKVersionCode());
                    int sdkVersionCode = DeviceUtils.getSDKVersionCode();
                    Uri uri = null;
                    if (sdkVersionCode >= 29) {
                        //android10及其以上
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
                    KLog.d("图库照片扫码数据：" + text);
                    finish();
                    EventBus.getDefault().post(new JsCallBackEvent(methodName, ResultModel.success(text)));
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_scan;
    }

    private Bitmap imageSizeCompress(Uri uri) {
        InputStream Stream = null;
        InputStream inputStream = null;
        try {
            //根据uri获取图片的流
            inputStream = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options的in系列的设置了，injustdecodebouond只解析图片的大小，而不加载到内存中去
            options.inJustDecodeBounds = true;
            //1.如果通过options.outHeight获取图片的宽高，就必须通过decodestream解析同options赋值
            //否则options.outheight获取不到宽高
            BitmapFactory.decodeStream(inputStream, null, options);
            //2.通过 btm.getHeight()获取图片的宽高就不需要1的解析，我这里采取第一张方式
//            Bitmap btm = BitmapFactory.decodeStream(inputStream);
            //以屏幕的宽高进行压缩
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int heightPixels = displayMetrics.heightPixels;
            int widthPixels = displayMetrics.widthPixels;
            //获取图片的宽高
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
            //heightPixels就是要压缩后的图片高度，宽度也一样
            int a = (int) Math.ceil((outHeight / (float) heightPixels));
            int b = (int) Math.ceil(outWidth / (float) widthPixels);
            //比例计算,一般是图片比较大的情况下进行压缩
            int max = Math.max(a, b);
            if (max > 1) {
                options.inSampleSize = max;
            }
            //解析到内存中去
            options.inJustDecodeBounds = false;
//            根据uri重新获取流，inputstream在解析中发生改变了
            Stream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(Stream, null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (Stream != null) {
                    Stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public void initCameraScan() {
        super.initCameraScan();

        //初始化解码配置
        DecodeConfig decodeConfig = new DecodeConfig();
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS)//如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
                .setFullAreaScan(false)//设置是否全区域识别，默认false
                .setAreaRectRatio(0.8f)//设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
                .setAreaRectVerticalOffset(0)//设置识别区域垂直方向偏移量，默认为0，为0表示居中，可以为负数
                .setAreaRectHorizontalOffset(0);//设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        //在启动预览之前，设置分析器，只识别二维码
        getCameraScan()
                .setVibrate(true)//设置是否震动，默认为false
                .setNeedAutoZoom(true)//二维码太小时可自动缩放，默认为false
                .setAnalyzer(new MultiFormatAnalyzer(decodeConfig));//设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 扫码结果回调
     *
     * @param result
     * @return 返回false表示不拦截，将关闭扫码界面并将结果返回给调用界面；
     * 返回true表示拦截，需自己处理逻辑。当isAnalyze为true时，默认会继续分析图像（也就是连扫）。
     * 如果只是想拦截扫码结果回调，并不想继续分析图像（不想连扫），请在拦截扫码逻辑处通过调
     */
    @Override
    public boolean onScanResultCallback(Result result) {
        /*
         * 因为setAnalyzeImage方法能动态控制是否继续分析图像。
         *
         * 1. 因为分析图像默认为true，如果想支持连扫，返回true即可。
         * 当连扫的处理逻辑比较复杂时，请在处理逻辑前调用getCameraScan().setAnalyzeImage(false)，
         * 来停止分析图像，等逻辑处理完后再调用getCameraScan().setAnalyzeImage(true)来继续分析图像。
         *
         * 2. 如果只是想拦截扫码结果回调自己处理逻辑，但并不想继续分析图像（即不想连扫），可通过
         * 调用getCameraScan().setAnalyzeImage(false)来停止分析图像。
         */
        String text = result.getText();
        KLog.d("二维码扫描结果" + text);
        //通知首页回调JS
        EventBus.getDefault().post(new JsCallBackEvent(methodName, ResultModel.success(text)));

        getCameraScan().setAnalyzeImage(false);
        return false;
    }
}