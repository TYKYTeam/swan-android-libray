package com.tyky.webviewBase.event;

public class ImagePreviewEvent {
    private String data;

    public ImagePreviewEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
