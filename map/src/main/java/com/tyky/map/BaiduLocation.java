package com.tyky.map;

import java.io.Serializable;

/**
 * 百度定位信息实体
 */
public class BaiduLocation implements Serializable {

    private int code;
    private Location data;
    private String msg;

    public BaiduLocation() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Location getData() {
        return data;
    }

    public void setData(Location data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class Location {
        private String province;
        private String city;
        private String district;
        private String street;
        private String addrStr;

        public Location() {
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getAddrStr() {
            return addrStr;
        }

        public void setAddrStr(String addrStr) {
            this.addrStr = addrStr;
        }
    }
}

