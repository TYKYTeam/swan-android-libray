package com.tyky.page;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tyky.page.utils.WXLaunchMiniUtil;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.StatusBarEvent;
import com.tyky.webviewBase.event.ImmersiveBarEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import org.greenrobot.eventbus.EventBus;

@WebViewInterface("page")
public class PageJsInterface {

    Gson gson = GsonUtils.getGson();

    /**
     * 跳转指定APP的指定Activity页面
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String gotoApp(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String packageName = paramModel.getPackageName();
        String activityName = paramModel.getActivityName();
        if (StringUtils.isEmpty(packageName) || StringUtils.isEmpty(activityName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        Intent intent = new Intent();
        ComponentName cmp = new ComponentName(packageName, activityName);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        intent.setClassName(packageName, activityName);
        ActivityUtils.getTopActivity().startActivity(intent);

        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 使用浏览器打开H5链接
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String openUrlByBrowser(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String url = paramModel.getContent();
        if (StringUtils.isEmpty(url) && url.startsWith("http")) {
            return gson.toJson(ResultModel.errorParam("传递参数有误，要求是一个链接地址"));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        ActivityUtils.getTopActivity().startActivity(intent);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 重启APP
     *
     * @return
     */
    @JavascriptInterface
    public String restartApp() {
        AppUtils.relaunchApp(true);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 更改状态栏颜色
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String changeStatusBar(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        Integer type = paramModel.getType();
        if (type == null || type == 0) {
            return gson.toJson(ResultModel.errorParam());
        }
        //构建事件，通过EventBus发送
        String content = paramModel.getContent();
        StatusBarEvent statusBarEvent = new StatusBarEvent();
        statusBarEvent.setType(type);
        if (type == 2) {
            statusBarEvent.setStatusColor(content);
        }
        EventBus.getDefault().post(statusBarEvent);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 沉浸式状态栏
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String immersiveBar(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        boolean isStatusBarVisible = paramModel.isStatusBarVisible();
        boolean isFitWindow = paramModel.isFitWindow();
        boolean light = paramModel.isLight();
        String color = paramModel.getColor();
        boolean isNavBarVisible = paramModel.isNavBarVisible();
        //构建事件，通过EventBus发送
        ImmersiveBarEvent immersiveBarEvent = new ImmersiveBarEvent(isStatusBarVisible, isFitWindow, light, isNavBarVisible, color);
        EventBus.getDefault().post(immersiveBarEvent);
        return gson.toJson(ResultModel.success(""));
    }


    /**
     * 跳转微信小程序
     *
     * @param param
     * @return
     */
    @JavascriptInterface
    public String gotoWeixinMiniProgress(String param) {
        WxParamModel wxParamModel = gson.fromJson(param, WxParamModel.class);
        //参数判空验证
        String appId = wxParamModel.getAppId();
        String progressId = wxParamModel.getProgressId();
        int progressType = wxParamModel.getProgressType();
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(progressId)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //判断是否安装微信
        if (!AppUtils.isAppInstalled("com.tencent.mm")) {
            ToastUtils.showShort("微信未安装，跳转微信小程序失败！");
            return gson.toJson(ResultModel.errorParam("微信未安装"));
        }
        //跳转微信小程序
        WXLaunchMiniUtil miniUtil = new WXLaunchMiniUtil();
        miniUtil.appId = appId;
        miniUtil.userName = progressId;
        //miniUtil.path = "pages/index/index";
        miniUtil.miniprogramType = "" + progressType;
        miniUtil.sendReq();

        return gson.toJson(ResultModel.success(""));
    }

}
