package com.tyky.webviewBase.model;

import java.util.List;

public class ParamModel {
    /**
     * 电话号码
     */
    private String phone;

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 回调方法
     */
    private String callBackMethod;

    /**
     * shareperfence存储需要用到的key和value
     */
    private String key;
    private String value;

    /**
     * 应用包名
     */
    private String packageName;
    /**
     * activity全类名
     */
    private String activityName;

    //-------------IM的Module传参字段开始----------
    /**
     * 登录userId
     */
    private String userId;
    /**
     * 登录签名
     */
    private String userSig;
    /**
     * 登录成功胡是否跳转IM首页
     */
    private boolean isGotoPage;
    //-------------IM的Module传参字段结束----------


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public boolean isGotoPage() {
        return isGotoPage;
    }

    public void setGotoPage(boolean gotoPage) {
        isGotoPage = gotoPage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private List<String> dataList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

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
