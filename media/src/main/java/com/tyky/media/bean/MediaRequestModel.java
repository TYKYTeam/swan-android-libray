package com.tyky.media.bean;

import com.tyky.webviewBase.model.ParamModel;

public class MediaRequestModel extends ParamModel {
    /**
     * 电话号码
     */
    private String phone;

    /**
     * 下载文件信息（以对象方式接收）
     */
    private DownloadInfo[] downloadInfos;

    /**
     * 预览文件信息（以对象方式接收）
     */
    private DownloadInfo previewFileInfo;

    /**
     * 文件下载地址（只传url情景，兼容之前版本）
     */
    private String[] downloadUrls;

    /**
     * 文件预览地址（只传url场景，兼容之前版本）
     */
    private String downloadUrl;

    public DownloadInfo[] getDownloadInfos() {
        return downloadInfos;
    }

    public void setDownloadInfos(DownloadInfo[] downloadInfos) {
        this.downloadInfos = downloadInfos;
    }

    public String[] getDownloadUrls() {
        return downloadUrls;
    }

    public void setDownloadUrls(String[] downloadUrls) {
        this.downloadUrls = downloadUrls;
    }

    public DownloadInfo getPreviewFileInfo() {
        return previewFileInfo;
    }

    public void setPreviewFileInfo(DownloadInfo previewFileInfo) {
        this.previewFileInfo = previewFileInfo;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
