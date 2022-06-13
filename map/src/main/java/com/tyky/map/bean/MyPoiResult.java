package com.tyky.map.bean;

public class MyPoiResult {

    /**
     * address
     */
    private String address;
    /**
     * area
     */
    private String area;
    /**
     * city
     */
    private String city;
    /**
     * name
     */
    private String name;
    /**
     * latitude
     */
    private double latitude;
    /**
     * longitude
     */
    private double longitude;
    /**
     * phoneNum
     */
    private String phoneNum;
    /**
     * province
     */
    private String province;

    /**
     * 街道
     */
    private String street = "";

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
