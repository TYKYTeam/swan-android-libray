package com.tyky.media.bean;

/**
 * 下载文件结果类
 */
public class DownloadResult {
    // 是否下载成功
    private boolean isSuccess;
    // 已下载文件名
    private String downloadFileName;

    public DownloadResult(boolean isSuccess, String downloadFileName) {
        this.isSuccess = isSuccess;
        this.downloadFileName = downloadFileName;
    }

    public DownloadResult() {
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    @Override
    public String toString() {
        return "DownloadResult{" +
                "isSuccess=" + isSuccess +
                ", downloadFileName='" + downloadFileName + '\'' +
                '}';
    }
}
