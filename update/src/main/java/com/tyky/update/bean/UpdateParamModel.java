package com.tyky.update.bean;

public class UpdateParamModel {
    /**
     * 更新类型，1为热更新包，2为全量更新（apk）
     */
    private int type;
    /**
     * H5版本号
     */
    private int h5VersionCode;
    /**
     * 版本号
     */
    private int versionCode;
    /**
     * 版本名
     */
    private String versionName;
    /**
     * 更新内容
     */
    private String updateContent;
    /**
     * 文件下载地址
     */
    private String downloadUrl;
    /**
     * 全量更新包，是否强制更新
     */
    private boolean forceUpdate;
    /**
     * 热更新包下载解压后，是否要提示对话框
     */
    private boolean needTip;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getH5VersionCode() {
        return h5VersionCode;
    }

    public void setH5VersionCode(int h5VersionCode) {
        this.h5VersionCode = h5VersionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public UpdateParamModel(int type, int h5VersionCode, int versionCode, String versionName, String updateContent, String downloadUrl, boolean forceUpdate, boolean needTip) {
        this.type = type;
        this.h5VersionCode = h5VersionCode;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.updateContent = updateContent;
        this.downloadUrl = downloadUrl;
        this.forceUpdate = forceUpdate;
        this.needTip = needTip;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isNeedTip() {
        return needTip;
    }

    public void setNeedTip(boolean needTip) {
        this.needTip = needTip;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}
