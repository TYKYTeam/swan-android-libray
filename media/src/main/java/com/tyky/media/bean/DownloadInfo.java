package com.tyky.media.bean;

import com.tyky.media.utils.FileDownloadUtil;

public class DownloadInfo {
    private String url;
    private String fileName;
    private FileDownloadUtil.OnDownloadListener listener;

    public DownloadInfo(String url, String fileName, FileDownloadUtil.OnDownloadListener listener) {
        this.url = url;
        this.fileName = fileName;
        this.listener = listener;
    }

    public DownloadInfo() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
                ", fileName='" + fileName + '\'' +
                ", listener=" + listener +
                '}';
    }
}

