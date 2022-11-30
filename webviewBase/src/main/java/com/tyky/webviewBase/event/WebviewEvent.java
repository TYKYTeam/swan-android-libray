package com.tyky.webviewBase.event;

public class WebviewEvent {

    public String data;

    public WebviewEvent(int type) {
        this.type = type;
    }

    /**
     * 事件类型
     * 1: 清除数据
     * 2：webview重载地址
     * 3:设置ua，data为ua数值
     */
    private int type;

    public WebviewEvent(String data, int type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
