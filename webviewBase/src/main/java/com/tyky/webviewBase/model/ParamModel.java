package com.tyky.webviewBase.model;

import java.util.List;

public class ParamModel {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 类型
     */
    private Integer type;

    /**
     *  1：横屏 0：竖屏
     */
    private Integer orientation;

    /**
     * 回调方法
     */
    private String callBackMethod;

    //-------------沉浸式状态栏----------
    /**
     * 是否忽略状态栏（时间、电量、信号）占位高度
     * true  : DecorView 内容位于状态栏和导航栏之间（不占用状态栏）
     * false : contentView 内容可以伸到状态栏位置（沉浸式）
     */
    private boolean isFitWindow;

    /**
     * 是否显示状态栏（时间、电量、信号）
     * true：显示状态栏
     * false：隐藏状态栏
     */
    private boolean isStatusBarVisible;

    /**
     *  light 模式（状态栏字体颜色变灰，导航栏内部按钮颜色变灰）
     *  true  : 状态栏字体灰色，导航栏按钮灰色
     *  false : 状态栏字体白色，导航栏按钮白色
     */
    private boolean isLight;

    /**
     * 背景颜色
     * 设置状态栏或导航栏颜色
     */
    private String color;

    /**
     * 是否显示导航栏
     *  true  : 显示底部导航栏
     *  false : 隐藏底部导航栏
     */
    private boolean isNavBarVisible;
    //-------------沉浸式状态栏----------

    /**
     * shareperfence存储需要用到的key和value
     */
    private String key;
    private String value;

    /**
     * 应用包名
     */
    private String packageName;
    /**
     * activity全类名
     */
    private String activityName;

    //-------------IM的Module传参字段开始----------
    /**
     * 登录userId
     */
    private String userId;
    /**
     * 登录签名
     */
    private String userSig;
    /**
     * 登录成功胡是否跳转IM首页
     */
    private boolean isGotoPage;
    //-------------IM的Module传参字段结束----------

    // webView是否支持缩放
    private boolean isWebViewScalable;

    public boolean isWebViewScalable() {
        return isWebViewScalable;
    }

    public void setWebViewScalable(boolean webViewScalable) {
        isWebViewScalable = webViewScalable;
    }

    public boolean isFitWindow() {
        return isFitWindow;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean light) {
        isLight = light;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFitWindow(boolean fitWindow) {
        isFitWindow = fitWindow;
    }

    public boolean isStatusBarVisible() {
        return isStatusBarVisible;
    }

    public void setStatusBarVisible(boolean statusBarVisible) {
        isStatusBarVisible = statusBarVisible;
    }

    public boolean isNavBarVisible() {
        return isNavBarVisible;
    }

    public void setNavBarVisible(boolean navBarVisible) {
        isNavBarVisible = navBarVisible;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public boolean isGotoPage() {
        return isGotoPage;
    }

    public void setGotoPage(boolean gotoPage) {
        isGotoPage = gotoPage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private List<String> dataList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public String getCallBackMethod() {
        return callBackMethod;
    }

    public void setCallBackMethod(String callBackMethod) {
        this.callBackMethod = callBackMethod;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
