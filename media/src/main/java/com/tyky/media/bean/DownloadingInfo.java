package com.tyky.media.bean;

public class DownloadingInfo {
    private double progress;
    private String FileName;
    private String errorMsg;

    public DownloadingInfo(int progress, String fileName, String errorMsg) {
        this.progress = progress;
        FileName = fileName;
        this.errorMsg = errorMsg;
    }

    public DownloadingInfo() {
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "DownloadingInfo{" +
                "progress=" + progress +
                ", FileName='" + FileName + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
