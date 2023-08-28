package com.tyky.media.bean;

import android.text.TextUtils;

import com.tyky.media.utils.FileDownloadUtil;

import java.io.Serializable;

public class DownloadInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;
    private String name;
    private FileDownloadUtil.OnDownloadListener listener;

    public DownloadInfo(String url, String fileName, FileDownloadUtil.OnDownloadListener listener) {
        this.url = url;
        this.name = fileName;
        this.listener = listener;
    }

    public DownloadInfo() {

    }

    public String getUrl() {
        return TextUtils.isEmpty(url) ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public FileDownloadUtil.OnDownloadListener getListener() {
        return listener;
    }

    public void setListener(FileDownloadUtil.OnDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "url='" + url + '\'' +
                ", fileName='" + name + '\'' +
                ", listener=" + listener +
                '}';
    }
}

