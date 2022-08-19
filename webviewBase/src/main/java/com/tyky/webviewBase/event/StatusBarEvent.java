package com.tyky.webviewBase.event;

public class StatusBarEvent {
    /**
     * 1:自动取色 2：根据用户颜色设置
     */
    private int type = 1;

    private String statusColor;

    public StatusBarEvent() {
    }

    public StatusBarEvent(int type, String statusColor) {
        this.type = type;
        this.statusColor = statusColor;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
