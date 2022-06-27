package com.tyky.mapNav.bean;

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
