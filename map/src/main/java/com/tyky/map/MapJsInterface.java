package com.tyky.map;

import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.webkit.JavascriptInterface;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
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
import com.tyky.webviewBase.utils.LocationUtil;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@WebViewInterface("map")
public class MapJsInterface {

    String tag = "MapJsInterface";

    Gson gson = GsonUtils.getGson();


    /**
     * ????????????
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

        //??????gps??????????????????
        Pair<Boolean, ResultModel> booleanResultModelPair = LocationUtil.checkLocationService();
        Boolean flag = booleanResultModelPair.first;
        if (!flag) {
            return gson.toJson(booleanResultModelPair.second);
        }

        if (PermissionUtils.isGranted(Permission.ACCESS_FINE_LOCATION)) {
            KLog.e(tag, "???????????????");
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
                            ToastUtils.showShort("????????????");
                        }
                    })
                    .request();
        }
        return gson.toJson(ResultModel.success(""));
    }


    /**
     * ????????????
     *
     * @param paramStr
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @JavascriptInterface
    public String poiSearch(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        String methodName = paramModel.getCallBackMethod();
        String keyword = paramModel.getKeyword();
        String city = paramModel.getCity();
        String tags = paramModel.getTags();

        if (TextUtils.isEmpty(methodName) || TextUtils.isEmpty(keyword) || TextUtils.isEmpty(city) || TextUtils.isEmpty(tags)) {
            return gson.toJson(ResultModel.errorParam());
        }

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
        return gson.toJson(ResultModel.success(""));
    }


    /**
     * ??????????????????
     * ???????????? https://lbsyun.baidu.com/index.php?title=androidsdk/guide/tool/calculation
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String getDistance(String paramStr) {
        MapParamModel mapParamModel = gson.fromJson(paramStr, MapParamModel.class);
        Double startLatitude = mapParamModel.getStartLatitude();
        Double startLongitude = mapParamModel.getStartLongitude();
        Double endLatitude = mapParamModel.getEndLatitude();
        Double endLongitude = mapParamModel.getEndLongitude();

        if (startLatitude == 0 || startLongitude == 0 || endLatitude == 0 || endLongitude == 0) {
            return gson.toJson(ResultModel.errorParam());
        }

        LatLng p1 = new LatLng(startLatitude, startLongitude);
        LatLng p2 = new LatLng(endLatitude, endLongitude);
        double distance = DistanceUtil.getDistance(p1, p2);
        //????????????????????????????????????
        BigDecimal bigDecimal = new BigDecimal(distance);
        distance = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return gson.toJson(ResultModel.success(distance));
    }

    /**
     * ???????????????
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String reverseGeoCode(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        Double latitude = paramModel.getLatitude();
        Double longitude = paramModel.getLongitude();
        String methodName = paramModel.getCallBackMethod();

        if (latitude == null || latitude == 0) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (longitude == null || longitude == 0) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (TextUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        LatLng point = new LatLng(latitude, longitude);
        //????????????
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //????????????????????????
                    ToastUtils.showShort("????????????????????????");
                } else {
                    //????????????
                    String address = reverseGeoCodeResult.getAddress();

                    ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();
                    MyPoiResult myPoiResult = new MyPoiResult();
                    myPoiResult.setProvince(addressDetail.province);//??????
                    myPoiResult.setCity(addressDetail.city); //??????
                    myPoiResult.setArea(addressDetail.district);//??????
                    myPoiResult.setStreet(addressDetail.street);//????????????
                    myPoiResult.setAddress(address);//??????????????????
                    myPoiResult.setLatitude(reverseGeoCodeResult.getLocation().latitude);//??????
                    myPoiResult.setLongitude(reverseGeoCodeResult.getLocation().longitude);//??????

                    EventBus.getDefault().post(new JsCallBackEvent(methodName, myPoiResult));
                }
            }
        });

        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(point)
                // ??????????????????????????? ?????????0????????????1??????
                .newVersion(1)
                // POI????????????????????????????????????0-1000????????????1000??????1000????????????????????????1000
                .radius(1000));
        geoCoder.destroy();
        return gson.toJson(ResultModel.success(""));

    }

    /**
     * ???????????????
     * ???????????? https://lbsyun.baidu.com/index.php?title=androidsdk/guide/search/geo
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String geocode(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        String city = paramModel.getCity();
        String endName = paramModel.getEndName();
        String methodName = paramModel.getCallBackMethod();

        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(endName) || TextUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //????????????
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                    if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                        //?????????????????????
                        ToastUtils.showShort("????????????????????????");
                    } else {
                        double latitude = geoCodeResult.getLocation().latitude;
                        double longitude = geoCodeResult.getLocation().longitude;

                        MyPoiResult myPoiResult = new MyPoiResult();
                        myPoiResult.setLatitude(latitude);
                        myPoiResult.setLongitude(longitude);
                        EventBus.getDefault().post(new JsCallBackEvent(methodName, myPoiResult));
                    }
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        });
        geoCoder.geocode(new GeoCodeOption()
                .city(city)
                .address(endName));
        geoCoder.destroy();
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * ??????????????????????????????????????????
     * ????????????  https://lbsyun.baidu.com/index.php?title=androidsdk/guide/tool/location
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String ptInCircle(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        Double latitude = paramModel.getLatitude();
        Double longitude = paramModel.getLongitude();
        Integer distance = paramModel.getDistance();
        String methodName = paramModel.getCallBackMethod();

        if (latitude == null || latitude == 0) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (longitude == null || longitude == 0) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (distance == null || distance == 0) {
            return gson.toJson(ResultModel.errorParam());
        }
        if (TextUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        //??????gps??????????????????
        Pair<Boolean, ResultModel> booleanResultModelPair = LocationUtil.checkLocationService();
        Boolean flag = booleanResultModelPair.first;
        if (!flag) {
            return gson.toJson(booleanResultModelPair.second);
        }

        LatLng center = new LatLng(latitude, longitude);

        //?????????????????????????????????
        BaiduMapUtils.startBdLocation(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();

                LatLng currentPoint = new LatLng(latitude, longitude);

                //??????js????????????
                boolean flag = SpatialRelationUtil.isCircleContainsPoint(center, distance, currentPoint);
                EventBus.getDefault().post(new JsCallBackEvent(methodName, ResultModel.success(flag)));
                BaiduMapUtils.stopLocation();
            }
        });
        return gson.toJson(ResultModel.success(""));
    }

}
