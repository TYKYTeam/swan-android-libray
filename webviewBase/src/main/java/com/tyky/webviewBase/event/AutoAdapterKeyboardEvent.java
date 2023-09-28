package com.tyky.webviewBase.event;

/**
 * 解决webview全屏，输入框软件盘遮挡问题
 */
public class AutoAdapterKeyboardEvent {
    // 是否是全屏
    private boolean isFullScreen;

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

}
