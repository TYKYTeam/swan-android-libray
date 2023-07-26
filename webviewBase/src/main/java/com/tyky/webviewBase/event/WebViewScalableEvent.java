package com.tyky.webviewBase.event;

/**
 * webView是否可缩放
 */
public class WebViewScalableEvent {
    private boolean isWebViewScalable;

    public WebViewScalableEvent() {
    }

    public WebViewScalableEvent(boolean isWebViewScalable) {
        this.isWebViewScalable = isWebViewScalable;
    }

    public boolean isWebViewScalable() {
        return isWebViewScalable;
    }

    public void setWebViewScalable(boolean webViewScalable) {
        isWebViewScalable = webViewScalable;
    }
}
