package com.tyky.map.bean;

import com.tyky.webviewBase.model.ParamModel;

import java.io.Serializable;

public class MapParamModel extends ParamModel implements Serializable {

    //---------百度地图poi检索参数---------
    private String keyword;
    private String city;
    private String tags;

    //------路径规划参数-----

    /**
     * 起点名称
     */
    private String startName;
    /**
     * 起点城市
     */
    private String startCityName;
    /**
     * 终点名称
     */
    private String endName;
    /**
     * 终点城市
     */
    private String endCityName;


    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 距离
     */
    private Integer distance;

    /**
     * -------计算两点距离的起点和终点经纬度---------
     */
    /**
     * 起点纬度
     */
    private Double startLatitude;
    /**
     * 起点经度
     */
    private Double startLongitude;

    /**
     * 终点纬度
     */
    private Double endLatitude;
    /**
     * 终点经度
     */
    private Double endLongitude;

    public Double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public Double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public Double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public Double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getStartCityName() {
        return startCityName;
    }

    public void setStartCityName(String startCityName) {
        this.startCityName = startCityName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getEndCityName() {
        return endCityName;
    }

    public void setEndCityName(String endCityName) {
        this.endCityName = endCityName;
    }

    public String getKeyword() {
        return keyword;
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public String getTags() {
        return tags;
    }


    public void setTags(String tags) {
        this.tags = tags;
    }

}
