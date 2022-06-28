package com.tyky.mapNav;

import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.mapNav.activity.BNaviMainActivity;
import com.tyky.mapNav.activity.MapActivity;
import com.tyky.mapNav.bean.MapParamModel;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("mapNav")
public class MapNavJsInterface {

    String tag = "MapJsInterface";

    Gson gson = GsonUtils.getGson();


    /**
     * 在地图显示当前位置
     *
     * @return
     */
    @JavascriptInterface
    public String showLocationInMap() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        ActivityUtils.startActivity(bundle, MapActivity.class);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 步行规划
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String walkingRouteSearch(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        return routeSearch(1, paramModel);
    }

    /**
     * 骑行规划
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String ridingRouteSearch(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        return routeSearch(2, paramModel);
    }

    /**
     * 驾车规划
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String drivingRouteSearch(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        return routeSearch(3, paramModel);
    }


    /**
     * 步行导航
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String walkGuide(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        ActivityUtils.startActivity(BNaviMainActivity.class);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 验证规划的参数
     *
     * @return 是否通过验证
     */
    private boolean checkRouteSearchParam(MapParamModel paramModel) {
        String startName = paramModel.getStartName();
        String startCityName = paramModel.getStartCityName();
        String endCityName = paramModel.getEndCityName();
        String endName = paramModel.getEndName();
        return !StringUtils.isEmpty(startName) && !StringUtils.isEmpty(endCityName) && !StringUtils.isEmpty(startCityName) && !StringUtils.isEmpty(endName);
    }

    private String routeSearch(int type,MapParamModel paramModel) {
        //验证下参数是否通过
        if (checkRouteSearchParam(paramModel)) {
            //传数据到MapActivity，MapActivity画出路径
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            bundle.putSerializable("data", paramModel);
            ActivityUtils.startActivity(bundle, MapActivity.class);
            return gson.toJson(ResultModel.success(""));
        } else {
            return gson.toJson(ResultModel.errorParam());
        }
    }


}
