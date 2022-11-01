package com.tyky.share.bean;

import com.tyky.webviewBase.model.ParamModel;

public class WxShareParamModel extends ParamModel {
    /**
     * 分享标题
     */
    private String shareTitle;
    /**
     * 分享描述
     */
    private String shareDescription;
    /**
     * 缩略图base64
     */
    private String shareThumbData;

    /**
     * 分享内容
     */
    private String shareContent;

    /**
     * 分享类型 0：文本 1：图片 2：视频 3：网页 4：小程序
     */
    private Integer type;

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

    @Override
    public Integer getType() {
        return type;
    }

    @Override
    public void setType(Integer type) {
        this.type = type;
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
