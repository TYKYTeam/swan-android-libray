package com.tyky.page;

public class WxParamModel {
    /**
     * APP在微信平台上的id
     */
    String appId;

    /**
     * 小程序id（gh开头）
     */
    String progressId;

    /**
     * 跳转小程序类型（0正式版 1开发版 2体验版）
     */
    int progressType;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }

    public int getProgressType() {
        return progressType;
    }

    public void setProgressType(int progressType) {
        this.progressType = progressType;
    }
}
