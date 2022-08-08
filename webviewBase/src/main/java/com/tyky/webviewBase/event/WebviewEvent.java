package com.tyky.webviewBase.event;

public class WebviewEvent {
    public WebviewEvent(int type) {
        this.type = type;
    }

    /**
     * 事件类型
     * 1: 清除数据
     * 2：webview重载地址
     */
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
