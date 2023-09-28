package com.tyky.webviewBase.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tencent.smtt.sdk.QbSdk;
import com.tyky.webviewBase.R;
import com.tyky.webviewBase.constants.PreviewPicture;
import com.tyky.webviewBase.constants.RequestCodeConstants;
import com.tyky.webviewBase.event.AutoAdapterKeyboardEvent;
import com.tyky.webviewBase.event.ImagePreviewEvent;
import com.tyky.webviewBase.event.ImmersiveBarEvent;
import com.tyky.webviewBase.event.IntentEvent;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.event.ScreenOrientationEvent;
import com.tyky.webviewBase.event.WebViewScalableEvent;
import com.tyky.webviewBase.event.StatusBarEvent;
import com.tyky.webviewBase.event.TakeScreenshotEvent;
import com.tyky.webviewBase.event.UrlLoadEvent;
import com.tyky.webviewBase.event.UrlLoadFinishEvent;
import com.tyky.webviewBase.event.WebviewEvent;
import com.tyky.webviewBase.model.ResultModel;
import com.tyky.webviewBase.utils.AndroidBug5497Workaround;
import com.tyky.webviewBase.utils.LibraryInfoUtils;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
        //语音初始化
        SpeechService.init(this);

        if (!AndPermission.hasPermissions(this, Permission.WRITE_EXTERNAL_STORAGE)) {
            AndPermission.with(this).runtime()
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(permissions -> {

                    })
                    .onDenied(permission -> {

                    }).start();
        }


        customWebView = findViewById(R.id.webview);
        ivPreview = (ImageView) findViewById(R.id.ivPreview);
        ivPreview.setOnClickListener(view -> ivPreview.setVisibility(View.GONE));

        clLoading = findViewById(R.id.clLoading);

        customWebView.loadUrl(url);

        //如果包含有debugger模块，调用上传错误日志方法
        if (LibraryInfoUtils.getCurrentLibraryDependencyList().contains("debugger")) {
            try {
                Class<?> aClass = Class.forName("com.tyky.logupload.CrashUploader");
                for (Method declaredMethod : aClass.getDeclaredMethods()) {
                    if (declaredMethod.getName().equals("uploadAll")) {
                        declaredMethod.invoke(null);
                        break;
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                KLog.e("反射调用上传日志失败！！");
            }
        }

        // 系统android版本高于android6这个版本无需使用X5内核
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            QbSdk.forceSysWebView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是否设置为横屏
        boolean isLandscape = getIntent().getBooleanExtra("isLandscape", false);
        if (isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setStatusBar(StatusBarEvent event) {
        int type = event.getType();
        if (type == 1) {
            //自动取色
            Bitmap drawingCache = ScreenUtils.screenShot(this);
            Palette.from(drawingCache)
                    .setRegion(0, 0, drawingCache.getWidth(), 400)
                    .maximumColorCount(5)
                    .generate(new Palette.PaletteAsyncListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            List<Palette.Swatch> swatches = palette.getSwatches();
                            Palette.Swatch mostSwatch = null;
                            if (!swatches.isEmpty()) {
                                for (Palette.Swatch swatch : swatches) {
                                    if (mostSwatch == null) {
                                        mostSwatch = swatch;
                                    } else {
                                        if (mostSwatch.getPopulation() < swatch.getPopulation()) {
                                            mostSwatch = swatch;
                                        }
                                    }
                                }
                            }

                            int color = mostSwatch.getRgb();
                            //int color = Color.parseColor("#eb1c63");

                            BarUtils.setStatusBarColor(CustomWebViewActivity.this, color, true);
                            BarUtils.setStatusBarLightMode(CustomWebViewActivity.this, ColorUtils.isLightColor(color));
                            //设置导航条颜色
                            BarUtils.setNavBarColor(CustomWebViewActivity.this, color);

                            drawingCache.recycle();
                        }
                    });
        } else {
            int color = Color.parseColor(event.getStatusColor());
            //int color = Color.parseColor("#eb1c63");
            BarUtils.setStatusBarColor(CustomWebViewActivity.this, color, true);
            BarUtils.setStatusBarLightMode(CustomWebViewActivity.this, ColorUtils.isLightColor(color));
            //设置导航条颜色
            BarUtils.setNavBarColor(CustomWebViewActivity.this, color);
        }

        //修复Android10及以上版本出现状态栏遮挡页面问题
        ViewGroup contentParent = CustomWebViewActivity.this.findViewById(android.R.id.content);
        contentParent.getChildAt(0).setFitsSystemWindows(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void immersiveBar(ImmersiveBarEvent event) {
        boolean isStatusBarVisible = event.isStatusBarVisible();
        boolean isFitWindow = event.isFitWindow();
        boolean isLight = event.isLight();
        boolean isNavBarVisible = event.isNavBarVisible();
        String colorStr = event.getColor();
        int color = TextUtils.isEmpty(colorStr) ? Color.TRANSPARENT : Color.parseColor(colorStr);

        BarUtils.setStatusBarVisibility(this, isStatusBarVisible);
        if (isStatusBarVisible) {
            BarUtils.setStatusBarColor(CustomWebViewActivity.this, color, isFitWindow);
            BarUtils.setStatusBarLightMode(CustomWebViewActivity.this, isLight);
        }

        BarUtils.setNavBarVisibility(this, isNavBarVisible);
        if (isNavBarVisible) {
            BarUtils.setNavBarColor(this, color);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setWebViewScalable(WebViewScalableEvent event) {
        boolean isWebViewScalable = event.isWebViewScalable();
        customWebView.getSettings().setSupportZoom(isWebViewScalable);
        customWebView.getSettings().setBuiltInZoomControls(isWebViewScalable);
        customWebView.getSettings().setDisplayZoomControls(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setScreenOrientation(ScreenOrientationEvent event) {
        Integer orientation = event.getScreenOrientation();
        if (orientation == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AutoAdapterKeyboard(AutoAdapterKeyboardEvent event) {
        // 解决webview全屏遮挡输入框问题
        AndroidBug5497Workaround.assistActivity(this, event.isFullScreen());
    }

   /* @RequiresApi(api = Build.VERSION_CODES.M)
    private void setLightStatusBar() {
        View decorView = getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDarkStatusBar() {
        View decorView = getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        decorView.setSystemUiVisibility(flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }*/

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
     * 开启启动页的效果
     */
    public void openLoading() {
        clLoading.setVisibility(View.VISIBLE);
    }

    /**
     * 加载地址
     *
     * @param url 以`/`开头，即为加载本地html（放在assets文件夹中）,如`/index.html`;
     *            或者以http开头的链接，会直接加载
     */
    public void loadUrl(String url) {
        if (url.startsWith("/")) {
            //加载本地地址
            loadLocalUrl(url);
        } else {
            if (url.contains("?")) {
                loadWebUrl(url + "&tykyTime=" + System.currentTimeMillis());
            } else {
                loadWebUrl(url + "?tykyTime=" + System.currentTimeMillis());
            }
        }
    }

    /**
     * 加载地址（通过EventBus来实现）
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadUrl(UrlLoadEvent event) {
        String url = event.getUrl();
        //清除缓存，加载数据
        customWebView.clearCache(true);
        customWebView.loadUrl(url);
    }

    /**
     * 取消启动图效果
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

        //暂时取消网页加载完自动取色功能
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //    setStatusBar(new StatusBarEvent());
        //}
    }

    /**
     * 设置加载的url并加载
     *
     * @param path
     */
    private void loadLocalUrl(String path) {
        String url = "file:///android_asset" + path;
        loadWebUrl(url);
    }

    /**
     * 加载url
     *
     * @param url
     */
    private void loadWebUrl(String url) {
        this.url = url;
        customWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        //退出APP移除网络状态监听器
        try {
            Class<?> aClass = Class.forName("com.tyky.listener.GlobalListenerStorage");
            Field netWorkListener = aClass.getDeclaredField("netWorkListener");
            Object o = netWorkListener.get(null);
            NetworkUtils.unregisterNetworkStatusChangedListener((NetworkUtils.OnNetworkStatusChangedListener) o);

        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException ignored) {
        }
        //设置返回键监听方法为空
        SPUtils.getInstance().put("OnBackPressMethod", "");
        //释放EventBus和语音TTS
        EventBus.getDefault().unregister(this);
        SpeechService.release();
        super.onDestroy();
    }

    Gson gson = GsonUtils.getGson();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealWebViewEvent(WebviewEvent event) {
        int type = event.getType();
        if (type == 1) {
            //清除webview缓存等信息
            customWebView.clearAllData();
        }
        if (type == 2) {
            //重新加载首页地址
            customWebView.loadUrl(url);
        }
        if (type == 3) {
            //设置webview的ua
            customWebView.setUa(event.getData());
        }
    }

    /**
     * 回调页面的Js
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
        KLog.d("--回调JS方法", jsScript);
        customWebView.evaluateJavascript(jsScript, null);
    }

    /**
     * Activity跳转
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
     * 预览图片
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void previewImg(ImagePreviewEvent event) {
        String data = event.getData();
        int type = event.getType();

        //base64类型的需要解码
        if (type == PreviewPicture.TYPE_BASE64) {
            //判断如果有base64开头，处理一下
            if (data.contains("base64,")) {
                data = StringUtils.substringAfter(data, "base64,");
            }
            byte[] bytes = EncodeUtils.base64Decode(data);
            ivPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(bytes).into(ivPreview);
        }

        //链接直接预览
        if (type == PreviewPicture.TYPE_URL) {
            ivPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(data).into(ivPreview);
        }
    }

    /**
     * 显示测试按钮并设置点击事件
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
        Bitmap bitmap = ScreenUtils.screenShot(this, false);
        File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG);
        EventBus.getDefault().post(new JsCallBackEvent(callBackMethod, file.getPath()));
    }


    /**
     * 是否开启双击返回
     *
     * @param flag
     */
    public void openDoubleBack(boolean flag) {
        doubleBackFlag = flag;
    }

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {

        String onBackPressMethodName = SPUtils.getInstance().getString("OnBackPressMethod");
        if (!TextUtils.isEmpty(onBackPressMethodName)) {
            EventBus.getDefault().post(new JsCallBackEvent(onBackPressMethodName,""));
        }

        if (customWebView.canGoBack()) {
            customWebView.goBack();
        } else {
            //是否开启双击退出
            if (doubleBackFlag) {
                long mNowTime = System.currentTimeMillis();//获取第一次按键时间
                if ((mNowTime - mPressedTime) > 2000) {
                    ToastUtils.showShort("再按一次退出程序");
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