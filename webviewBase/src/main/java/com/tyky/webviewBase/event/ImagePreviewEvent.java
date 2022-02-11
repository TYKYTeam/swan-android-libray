package com.tyky.webviewBase.event;

public class ImagePreviewEvent {
    private String data;
    private int type;

    public ImagePreviewEvent(String data, int type) {
        this.data = data;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
