package com.tyky.webviewBase.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tyky.webviewBase.R;
import com.tyky.webviewBase.constants.PreviewPicture;
import com.tyky.webviewBase.constants.RequestCodeConstants;
import com.tyky.webviewBase.event.ImagePreviewEvent;
import com.tyky.webviewBase.event.IntentEvent;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.event.TakeScreenshotEvent;
import com.tyky.webviewBase.event.UrlLoadEvent;
import com.tyky.webviewBase.event.UrlLoadFinishEvent;
import com.tyky.webviewBase.model.ResultModel;
import com.tyky.webviewBase.utils.SpeechService;
import com.tyky.webviewBase.view.CustomWebView;
import com.tyky.webviewBase.view.CustomWebViewChrome;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Field;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomWebViewActivity extends AppCompatActivity {

    //String url = "https://stars-one.site/";
    String url = "https://www.wenshushu.cn/";
    //String url = "http://10.232.241.118:8080/#/pages/index/index";
    private CustomWebView customWebView;
    private ImageView ivPreview;

    private ConstraintLayout clLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_webview);

        EventBus.getDefault().register(this);
        //???????????????
        SpeechService.init(this);

        AndPermission.with(this).runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(permissions -> {

                })
                .onDenied(permission -> {

                }).start();


        customWebView = findViewById(R.id.webview);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        ivPreview.setOnClickListener(view -> ivPreview.setVisibility(View.GONE));

        clLoading = findViewById(R.id.clLoading);

        customWebView.loadUrl(url);

        //??????android 7.0???????????? exposed beyond app through ClipData.Item.getUri()??????
        // ?????????????????????
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode >= RequestCodeConstants.PHOTO && requestCode <= RequestCodeConstants.FILE) {
            CustomWebViewChrome customWebViewChrome = customWebView.getCustomWebViewChrome();
            customWebViewChrome.handleDataFromIntent(requestCode, resultCode, data);
        } else if (requestCode == 45) {
            Uri data1 = data.getData();
            Log.d("--test", "onActivityResult: " + data1);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * ????????????????????????
     */
    public void openLoading() {
        clLoading.setVisibility(View.VISIBLE);
    }

    /**
     * ????????????
     *
     * @param url ???`/`???????????????????????????html?????????assets???????????????,???`/index.html`;
     *            ?????????http?????????????????????????????????
     */
    public void loadUrl(String url) {
        if (url.startsWith("/")) {
            loadLocalUrl(url);
        } else {
            loadWebUrl(url);
        }
    }

    /**
     * ?????????????????????EventBus????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadUrl(UrlLoadEvent event) {
        String url = event.getUrl();
        //???????????????????????????
        customWebView.clearCache(true);
        loadUrl(url);
    }

    /**
     * ?????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadUrl(UrlLoadFinishEvent event) {
        if (clLoading.getVisibility() == View.VISIBLE) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(500);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clLoading.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            clLoading.startAnimation(alphaAnimation);
        }

    }

    /**
     * ???????????????url?????????
     *
     * @param path
     */
    private void loadLocalUrl(String path) {
        String url = "file:///android_asset" + path;
        loadWebUrl(url);
    }

    /**
     * ??????url
     *
     * @param url
     */
    private void loadWebUrl(String url) {
        this.url = url;
        customWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        //??????APP???????????????????????????
        try {
            Class<?> aClass = Class.forName("com.tyky.listener.GlobalListenerStorage");
            Field netWorkListener = aClass.getDeclaredField("netWorkListener");
            Object o = netWorkListener.get(null);
            NetworkUtils.unregisterNetworkStatusChangedListener((NetworkUtils.OnNetworkStatusChangedListener) o);

        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException ignored) {
        }
        //??????EventBus?????????TTS
        EventBus.getDefault().unregister(this);
        SpeechService.release();
        super.onDestroy();
    }

    Gson gson = GsonUtils.getGson();

    /**
     * ???????????????Js
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void evaluateJavascript(JsCallBackEvent event) {
        Object object = event.getObject();
        String method = event.getMethod();
        String jsScript;
        if (object == null) {
            jsScript = "javascript:" + method + "()";
        } else {
            jsScript = "javascript:" + method + "('" + gson.toJson(ResultModel.success(object)) + "')";
        }
        KLog.d("--??????JS??????", jsScript);
        customWebView.evaluateJavascript(jsScript, null);
    }

    /**
     * Activity??????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToActivity(IntentEvent event) {
        Class<?> goToActivity = event.getActivityClass();
        Intent intent = new Intent(this, goToActivity);
        intent.putExtra("methodName", event.getMethodName());
        startActivity(intent);
    }

    /**
     * ????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void previewImg(ImagePreviewEvent event) {
        String data = event.getData();
        int type = event.getType();

        //base64?????????????????????
        if (type == PreviewPicture.TYPE_BASE64) {
            //???????????????base64?????????????????????
            if (data.contains("base64,")) {
                data = StringUtils.substringAfter(data, "base64,");
            }
            byte[] bytes = EncodeUtils.base64Decode(data);
            ivPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(bytes).into(ivPreview);
        }

        //??????????????????
        if (type == PreviewPicture.TYPE_URL) {
            ivPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(data).into(ivPreview);
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param listener
     */
    public void showTestBtn(View.OnClickListener listener) {
        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setVisibility(View.VISIBLE);
        btnTest.setOnClickListener(listener);
    }

    private boolean doubleBackFlag = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void takeScreenshot(TakeScreenshotEvent event) {
        String callBackMethod = event.getCallBackMethod();
        Bitmap bitmap = ScreenUtils.screenShot(this,false);
        File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG);
        EventBus.getDefault().post(new JsCallBackEvent(callBackMethod,file.getPath()));
    }


    /**
     * ????????????????????????
     *
     * @param flag
     */
    public void openDoubleBack(boolean flag) {
        doubleBackFlag = flag;
    }

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {
        if (customWebView.canGoBack()) {
            customWebView.goBack();
        } else {
            //????????????????????????
            if (doubleBackFlag) {
                long mNowTime = System.currentTimeMillis();//???????????????????????????
                if ((mNowTime - mPressedTime) > 2000) {
                    ToastUtils.showShort("????????????????????????");
                    mPressedTime = mNowTime;
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }

        }

    }
}