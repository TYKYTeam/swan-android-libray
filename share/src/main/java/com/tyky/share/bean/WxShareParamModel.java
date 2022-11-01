package com.tyky.share.bean;

import com.tyky.webviewBase.model.ParamModel;

public class WxShareParamModel extends ParamModel {
    /**
     * 分享标题
     */
    private String shareTitle = "";
    /**
     * 分享描述
     */
    private String shareDescription = "";
    /**
     * 缩略图base64
     */
    private String shareThumbData;

    /**
     * 分享内容
     */
    private String shareContent;

    /**
     * 分享目标类型 0：好友 1：朋友圈
     */
    private int shareTargetType = 0;

    public String getShareThumbData() {
        return shareThumbData;
    }

    public void setShareThumbData(String shareThumbData) {
        this.shareThumbData = shareThumbData;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public int getShareTargetType() {
        return shareTargetType;
    }

    public void setShareTargetType(int shareTargetType) {
        this.shareTargetType = shareTargetType;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }
}
