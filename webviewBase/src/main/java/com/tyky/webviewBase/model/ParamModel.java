package com.tyky.webviewBase.model;

import java.util.List;

public class ParamModel {
    private String phone;
    private String content;
    private String callBackMethod;
    private List<String> dataList;

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCallBackMethod() {
        return callBackMethod;
    }

    public void setCallBackMethod(String callBackMethod) {
        this.callBackMethod = callBackMethod;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
