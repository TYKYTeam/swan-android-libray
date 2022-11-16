package com.tyky.ocr.bean;

import com.baidu.ocr.sdk.model.IDCardResult;

public class IdCardInfoFront {
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 公民身份号码
     */
    private String idNumber;
    /**
     * 住址
     */
    private String address;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 民族
     */
    private String ethnic;


    public static IdCardInfoFront fromIdCardResult(IDCardResult idCardResult) {
        IdCardInfoFront idCardInfoFront = new IdCardInfoFront();
        idCardInfoFront.setAddress(idCardResult.getAddress().getWords());
        idCardInfoFront.setBirthday(idCardResult.getBirthday().getWords());
        idCardInfoFront.setEthnic(idCardResult.getEthnic().getWords());
        idCardInfoFront.setGender(idCardResult.getGender().getWords());
        idCardInfoFront.setIdNumber(idCardResult.getIdNumber().getWords());
        idCardInfoFront.setName(idCardResult.getName().getWords());
        return idCardInfoFront;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }
}
