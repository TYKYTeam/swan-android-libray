package com.tyky.webviewBase.event;

/**
 * 状态栏沉浸式
 */
public class StatusBarImmersiveEvent {
    private boolean isFitWindow;

    public StatusBarImmersiveEvent() {
    }

    public StatusBarImmersiveEvent(boolean isFitWindow) {
        this.isFitWindow = isFitWindow;
    }

    public boolean isFitWindow() {
        return isFitWindow;
    }

    public void setFitWindow(boolean fitWindow) {
        isFitWindow = fitWindow;
    }
}
