package com.tyky.webviewBase.model;

public class ResultModel {
    private int code;
    private Object result;
    private String desc = "";

    public static ResultModel success(Object result) {
        ResultModel resultModel = new ResultModel();
        resultModel.code = 200;
        resultModel.result = result;
        return resultModel;
    }

    public static ResultModel success(String result) {
        ResultModel resultModel = new ResultModel();
        resultModel.code = 200;
        resultModel.result = result;
        return resultModel;
    }

    /**
     * 参数验证错误
     * @param error
     * @return
     */
    public static ResultModel errorParam(String error) {
        ResultModel resultModel = new ResultModel();
        resultModel.code = 201;
        resultModel.result = "";
        resultModel.desc = error;
        return resultModel;
    }

    /**
     * 缺少权限
     * @param error
     * @return
     */
    public static ResultModel errorPermission(String error) {
        ResultModel resultModel = new ResultModel();
        resultModel.code = 202;
        resultModel.result = "";
        resultModel.desc = error;
        return resultModel;
    }

    /**
     * 缺少权限
     * @return
     */
    public static ResultModel errorPermission() {
        ResultModel resultModel = new ResultModel();
        resultModel.code = 202;
        resultModel.result = "";
        resultModel.desc = "";
        return resultModel;
    }

    public static ResultModel errorParam() {
        ResultModel resultModel = new ResultModel();
        resultModel.code = 201;
        resultModel.result = "";
        resultModel.desc = "必要参数未传";
        return resultModel;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
