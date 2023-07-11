package com.tyky.media.bean;

public class DownloadInfo {
    private String url;
    private String FileName;

    public DownloadInfo(String url, String fileName) {
        this.url = url;
        FileName = fileName;
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
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "url='" + url + '\'' +
                ", FileName='" + FileName + '\'' +
                '}';
    }
}

