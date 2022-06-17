package com.tyky.map;

import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tyky.map.bean.MapParamModel;
import com.tyky.map.bean.MyPoiResult;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@WebViewInterface("map")
public class MapJsInterface {

    String tag = "MapJsInterface";

    Gson gson = GsonUtils.getGson();


    /**
     * 开始定位
     *
     * @return
     */
    @JavascriptInterface
    public String getLocation(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String methodName = paramModel.getCallBackMethod();
        if (StringUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        if (PermissionUtils.isGranted(Permission.ACCESS_FINE_LOCATION)) {
            KLog.e(tag, "有定位权限");
            BaiduMapUtils.startBdLocation(methodName);
        } else {
            PermissionUtils.permission(Permission.ACCESS_FINE_LOCATION)
                    .callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(@NonNull List<String> granted) {
                            BaiduMapUtils.startBdLocation(methodName);
                        }

                        @Override
                        public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                            ToastUtils.showShort("拒绝权限");
                        }
                    })
                    .request();
        }
        return gson.toJson(ResultModel.success(""));
    }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @JavascriptInterface
    public void poiSearch(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        String methodName = paramModel.getCallBackMethod();
        String keyword = paramModel.getKeyword();
        String city = paramModel.getCity();
        String tags = paramModel.getTags();

        PoiSearch poiSearch = PoiSearch.newInstance();

        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<MyPoiResult> myPoiResults = new ArrayList<>();
                poiResult.getAllPoi().forEach(item -> {
                    MyPoiResult myPoiResult = new MyPoiResult();
                    myPoiResult.setAddress(item.address);
                    myPoiResult.setArea(item.area);
                    myPoiResult.setCity(item.city);
                    myPoiResult.setProvince(item.province);
                    myPoiResult.setLatitude(item.getLocation().latitude);
                    myPoiResult.setLongitude(item.getLocation().longitude);
                    myPoiResult.setName(item.name);
                    myPoiResult.setPhoneNum(item.phoneNum == null ? "" : item.phoneNum);
                    myPoiResults.add(myPoiResult);
                });
                EventBus.getDefault().post(new JsCallBackEvent(methodName, myPoiResults));
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption()
                .city(city)
                .keyword(keyword)
                .tag(tags)
                .pageNum(0);
        poiSearch.searchInCity(poiCitySearchOption);
        poiSearch.destroy();
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
