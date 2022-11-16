package com.tyky.ocr.bean;

import com.baidu.ocr.sdk.model.IDCardResult;

/**
 * 身份证背面数据
 */
public class IdCardInfoBack {

    public static IdCardInfoBack fromIdCardResult(IDCardResult idCardResult) {
        IdCardInfoBack idCardInfoBack = new IdCardInfoBack();
        idCardInfoBack.setExpiryDate(idCardResult.getExpiryDate().getWords());
        idCardInfoBack.setSignDate(idCardResult.getSignDate().getWords());
        idCardInfoBack.setIssueAuthority(idCardResult.getIssueAuthority().getWords());
        return idCardInfoBack;
    }

    /**
     * 签发机关
     */
    private String issueAuthority;
    /**
     * 签证时间
     */
    private String signDate;
    /**
     * 到期时间
     */
    private String expiryDate;

    public String getIssueAuthority() {
        return issueAuthority;
    }

    public void setIssueAuthority(String issueAuthority) {
        this.issueAuthority = issueAuthority;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
