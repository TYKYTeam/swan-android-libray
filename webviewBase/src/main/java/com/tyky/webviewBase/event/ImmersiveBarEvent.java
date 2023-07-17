package com.tyky.webviewBase.event;

/**
 * 状态栏沉浸式
 */
public class ImmersiveBarEvent {
    private boolean isFitWindow;
    private boolean isLight;
    private String color;

    public ImmersiveBarEvent() {
    }

    public ImmersiveBarEvent(boolean isFitWindow, boolean isLight, String color) {
        this.isFitWindow = isFitWindow;
        this.isLight = isLight;
        this.color = color;
    }

    public boolean isFitWindow() {
        return isFitWindow;
    }

    public void setFitWindow(boolean fitWindow) {
        isFitWindow = fitWindow;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean light) {
        isLight = light;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
