package com.tyky.webviewBase.event;

public class JsCallBackEvent {
    private Object object;
    private String method;

    public JsCallBackEvent(String method, Object object) {
        this.object = object;
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
