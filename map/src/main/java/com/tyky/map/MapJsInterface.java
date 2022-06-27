package com.tyky.map;

import android.os.Build;
import android.webkit.JavascriptInterface;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
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
     * 地点检索
     * @param paramStr
     */
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

    void geo() {
        //todo 地理编码（地址转坐标）和逆地理编码（坐标转地址）
        //https://lbsyun.baidu.com/index.php?title=androidsdk/guide/search/geo
    }

    void isInCircle() {
        //todo 判断是否在该范围内
        //https://lbsyun.baidu.com/index.php?title=androidsdk/guide/tool/location
    }


}
