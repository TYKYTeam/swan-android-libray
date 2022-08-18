package com.tyky.update.bean;

public class UpdateParamModel {
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
     * 发布时间
     */
    private String updateTime;

    /**
     * apk文件大小
     */
    private String fileSize;

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public UpdateParamModel() {
    }

    public UpdateParamModel(int versionCode, String versionName, String updateContent, String downloadUrl, boolean forceUpdate) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.updateContent = updateContent;
        this.downloadUrl = downloadUrl;
        this.forceUpdate = forceUpdate;
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

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }
}
