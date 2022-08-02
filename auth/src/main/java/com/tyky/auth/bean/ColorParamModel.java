package com.tyky.auth.bean;

import com.tyky.webviewBase.model.ParamModel;

public class ColorParamModel extends ParamModel {
    /**
     * 错误的颜色和宽度
     */
    private String errorColor;
    private Integer errorWidth;

    /**
     * 选中后的实圆心和直线颜色，宽度
     */
    private String insideCircleColor;
    private Integer insideCircleWidth;

    /**
     * 未选中的圆形颜色和边框宽度
     */
    private String circleColor;
    private Integer circleWidth;

    public String getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(String errorColor) {
        this.errorColor = errorColor;
    }

    public Integer getErrorWidth() {
        return errorWidth;
    }

    public void setErrorWidth(Integer errorWidth) {
        this.errorWidth = errorWidth;
    }

    public String getInsideCircleColor() {
        return insideCircleColor;
    }

    public void setInsideCircleColor(String insideCircleColor) {
        this.insideCircleColor = insideCircleColor;
    }

    public Integer getInsideCircleWidth() {
        return insideCircleWidth;
    }

    public void setInsideCircleWidth(Integer insideCircleWidth) {
        this.insideCircleWidth = insideCircleWidth;
    }

    public String getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(String circleColor) {
        this.circleColor = circleColor;
    }

    public Integer getCircleWidth() {
        return circleWidth;
    }

    public void setCircleWidth(Integer circleWidth) {
        this.circleWidth = circleWidth;
    }
}
