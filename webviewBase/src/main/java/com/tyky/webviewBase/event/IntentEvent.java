package com.tyky.webviewBase.event;

/**
 * 页面跳转事件
 */
public class IntentEvent {
    Class<?> activityClass;
    /**
     * 当methodName传空白字符串，标明事件是由原生触发的页面跳转，而不是JS触发的
     */
    String methodName;

    public IntentEvent(Class<?> activityClass, String methodName) {
        this.activityClass = activityClass;
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public IntentEvent(Class<?> activityClass) {
        this.activityClass = activityClass;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<?> activityClass) {
        this.activityClass = activityClass;
    }
}
