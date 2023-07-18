package com.tyky.webviewBase.event;

/**
 * 状态栏沉浸式
 */
public class ImmersiveBarEvent {
    private boolean isFitWindow;
    private boolean isStatusBarVisible;
    private boolean isLight;
    private boolean isNavBarVisible;
    private String color;

    public ImmersiveBarEvent() {
    }

    public ImmersiveBarEvent(boolean isStatusBarVisible, boolean isFitWindow, boolean isLight, boolean isNavBarVisible, String color) {
        this.isStatusBarVisible = isStatusBarVisible;
        this.isFitWindow = isFitWindow;
        this.isLight = isLight;
        this.isNavBarVisible = isNavBarVisible;
        this.color = color;
    }

    public boolean isFitWindow() {
        return isFitWindow;
    }

    public void setFitWindow(boolean fitWindow) {
        isFitWindow = fitWindow;
    }

    public boolean isStatusBarVisible() {
        return isStatusBarVisible;
    }

    public void setStatusBarVisible(boolean statusBarVisible) {
        isStatusBarVisible = statusBarVisible;
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

    public boolean isNavBarVisible() {
        return isNavBarVisible;
    }

    public void setNavBarVisible(boolean navBarVisible) {
        isNavBarVisible = navBarVisible;
    }
}
