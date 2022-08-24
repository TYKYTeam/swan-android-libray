package com.tyky.debugger.bean;

import org.litepal.crud.LitePalSupport;

public class HistoryUrlInfo extends LitePalSupport {

    private String url;

    public HistoryUrlInfo(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
