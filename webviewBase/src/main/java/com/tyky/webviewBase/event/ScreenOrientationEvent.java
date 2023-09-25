package com.tyky.webviewBase.event;

/**
 * 设置屏幕旋转事件 1：横屏 0：竖屏
 */
public class ScreenOrientationEvent {
    private Integer screenOrientation;

    public ScreenOrientationEvent() {
    }

    public ScreenOrientationEvent(Integer screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

    public Integer getScreenOrientation() {
        return screenOrientation;
    }

    public void setScreenOrientation(Integer screenOrientation) {
        this.screenOrientation = screenOrientation;
    }

}
