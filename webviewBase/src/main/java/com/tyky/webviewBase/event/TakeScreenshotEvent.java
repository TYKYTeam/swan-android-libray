package com.tyky.webviewBase.event;

public class TakeScreenshotEvent {
    String callBackMethod;

    public String getCallBackMethod() {
        return callBackMethod;
    }

    public void setCallBackMethod(String callBackMethod) {
        this.callBackMethod = callBackMethod;
    }

    public TakeScreenshotEvent( String callBackMethod) {
        this.callBackMethod = callBackMethod;
    }
}
