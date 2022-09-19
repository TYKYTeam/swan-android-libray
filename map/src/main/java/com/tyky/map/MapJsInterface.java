package com.tyky.map;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

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
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
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

        //检测gps服务是否打开
        Pair<Boolean, ResultModel> booleanResultModelPair = LocationUtil.checkLocationService();
        Boolean flag = booleanResultModelPair.first;
        if (!flag) {
            return gson.toJson(booleanResultModelPair.second);
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
     * 计算两点距离
     * 参考文档 https://lbsyun.baidu.com/index.php?title=androidsdk/guide/tool/calculation
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
        //保留两位小数（四舍五入）
        BigDecimal bigDecimal = new BigDecimal(distance);
        distance = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return gson.toJson(ResultModel.success(distance));
    }

    /**
     * 坐标转地址
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
        //调用地址
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    ToastUtils.showShort("终点地址检索失败");
                } else {
                    //详细地址
                    String address = reverseGeoCodeResult.getAddress();

                    ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();
                    MyPoiResult myPoiResult = new MyPoiResult();
                    myPoiResult.setProvince(addressDetail.province);//省份
                    myPoiResult.setCity(addressDetail.city); //城市
                    myPoiResult.setArea(addressDetail.district);//区县
                    myPoiResult.setStreet(addressDetail.street);//街道信息
                    myPoiResult.setAddress(address);//详细地址信息
                    myPoiResult.setLatitude(reverseGeoCodeResult.getLocation().latitude);//纬度
                    myPoiResult.setLongitude(reverseGeoCodeResult.getLocation().longitude);//经度

                    EventBus.getDefault().post(new JsCallBackEvent(methodName, myPoiResult));
                }
            }
        });

        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(point)
                // 设置是否返回新数据 默认值0不返回，1返回
                .newVersion(1)
                // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(1000));
        geoCoder.destroy();
        return gson.toJson(ResultModel.success(""));

    }

    /**
     * 地址转坐标
     * 文档参考 https://lbsyun.baidu.com/index.php?title=androidsdk/guide/search/geo
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String geoCode(String paramStr) {
        MapParamModel paramModel = gson.fromJson(paramStr, MapParamModel.class);
        String city = paramModel.getCity();
        String endName = paramModel.getEndName();
        String methodName = paramModel.getCallBackMethod();

        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(endName) || TextUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //调用地址
        GeoCoder geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                    if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有检索到结果
                        ToastUtils.showShort("终点坐标检索失败");
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
     * 判断当前位置是否在某点范围内
     * 参考文档  https://lbsyun.baidu.com/index.php?title=androidsdk/guide/tool/location
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

        //检测gps服务是否打开
        Pair<Boolean, ResultModel> booleanResultModelPair = LocationUtil.checkLocationService();
        Boolean flag = booleanResultModelPair.first;
        if (!flag) {
            return gson.toJson(booleanResultModelPair.second);
        }

        LatLng center = new LatLng(latitude, longitude);

        //开启定位，获取当前位置
        BaiduMapUtils.startBdLocation(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();

                LatLng currentPoint = new LatLng(latitude, longitude);

                //触发js回调事件
                boolean flag = SpatialRelationUtil.isCircleContainsPoint(center, distance, currentPoint);
                EventBus.getDefault().post(new JsCallBackEvent(methodName, ResultModel.success(flag)));
                BaiduMapUtils.stopLocation();
            }
        });
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 唤起地图
     *
     * @param param
     * @return
     */
    @JavascriptInterface
    public String callSDKMap(String param) {
        MapParamModel mapParamModel = gson.fromJson(param, MapParamModel.class);
        Double longitude = mapParamModel.getLongitude();
        Double latitude = mapParamModel.getLatitude();
        String title = mapParamModel.getTitle();

        //当前已安装的地图APP
        List<Integer> currentInstallApp = new ArrayList<>();

        //地图APP的包名
        String[] pkgNameArr = {"com.baidu.BaiduMap", "com.autonavi.minimap", "com.tencent.map"};

        for (int i = 0; i < pkgNameArr.length; i++) {
            String s = pkgNameArr[i];
            if (AppUtils.isAppInstalled(s)) {
                currentInstallApp.add(i);
            }
        }

        if (currentInstallApp.isEmpty()) {
            String tip = "手机未安装任何的地图应用，唤起地图失败！";
            ToastUtils.showShort(tip);
            return gson.toJson(ResultModel.errorParam(tip));
        }

        //弹出底部选择对话框
        BottomDialog.show(new OnBindView<BottomDialog>(R.layout.layout_map_choice_list) {
            @Override
            public void onBind(BottomDialog dialog, View v) {
                TextView tvBaidu = v.findViewById(R.id.tvBaidu);
                TextView tvGaode = v.findViewById(R.id.tvGaode);
                TextView tvTengxu = v.findViewById(R.id.tvTengxu);

                TextView[] textViewArr = {tvBaidu, tvGaode, tvTengxu};
                for (Integer integer : currentInstallApp) {
                    TextView textView = textViewArr[integer];
                    textView.setVisibility(View.VISIBLE);
                    textView.setOnClickListener(v12 -> {
                        startMapActivity(integer + 1, title, longitude, latitude);
                        dialog.dismiss();
                    });
                }

                TextView tvCancel = v.findViewById(R.id.tvCancel);
                tvCancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
            }
        });


        return gson.toJson(ResultModel.success(""));
    }

    private void startMapActivity(int type, String title, Double longitude, Double latitude) {
        Intent intent = new Intent();
        StringBuffer stringBuffer = new StringBuffer();

        switch (type) {
            case 1:
                //唤起百度地图 协议详情请查看 https://lbsyun.baidu.com/index.php?title=uri/api/android#service-page-anchor9的2.3.2节
                stringBuffer.append("baidumap://map/direction?");
                stringBuffer.append("origin=我的位置");
                stringBuffer.append("&");
                stringBuffer.append("coord_type=gcj02");
                stringBuffer.append("&");

                String destinationParam = "destination=" + "name:" + title + "|" + "latlng:" + longitude + "," + latitude;
                stringBuffer.append(destinationParam);
                stringBuffer.append("&");
                stringBuffer.append("src=andr.").append(AppUtils.getAppPackageName());

                break;
            case 2:
                //唤起高德地图，协议详情请访问 https://lbs.amap.com/api/amap-mobile/guide/android/route
                stringBuffer.append("amapuri://route/plan/?");
                stringBuffer.append("dlat=").append(latitude);
                stringBuffer.append("&");
                stringBuffer.append("dlon=").append(longitude);
                stringBuffer.append("&");
                stringBuffer.append("dname=").append(title);
                stringBuffer.append("&");
                stringBuffer.append("dev=0");
                stringBuffer.append("&");
                stringBuffer.append("t=0");
                stringBuffer.append("&");
                stringBuffer.append("sourceApplication=").append(AppUtils.getAppPackageName());

                //唤起高德地图
                break;
            case 3:
                //唤起腾讯地图 https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobileRoute
                stringBuffer.append("qqmap://map/routeplan?");
                stringBuffer.append("from=我的位置");
                stringBuffer.append("&");
                stringBuffer.append("type=drive");
                stringBuffer.append("&");
                stringBuffer.append("tocoord=").append(latitude).append(",").append(longitude);
                stringBuffer.append("&");
                stringBuffer.append("to=").append(title);
                stringBuffer.append("&");
                stringBuffer.append("referer=").append(AppUtils.getAppPackageName());
                break;
        }
        String schemaUrl = stringBuffer.toString();
        KLog.d("唤起地图的协议地址：" + schemaUrl);
        intent.setData(Uri.parse(schemaUrl));
        ActivityUtils.startActivity(intent);
    }
}
