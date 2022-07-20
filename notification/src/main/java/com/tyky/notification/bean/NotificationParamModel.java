package com.tyky.notification.bean;

import com.tyky.webviewBase.model.ParamModel;

public class NotificationParamModel extends ParamModel {
    /**
     * 点击intent跳转地址
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
