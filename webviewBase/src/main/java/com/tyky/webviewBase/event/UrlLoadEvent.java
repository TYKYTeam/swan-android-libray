package com.tyky.webviewBase.event;

public class UrlLoadEvent {
    private String url;

    public UrlLoadEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
