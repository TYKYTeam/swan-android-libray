package com.tyky.map;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.socks.library.KLog;
import com.tyky.map.bean.MyPoiResult;
import com.tyky.webviewBase.event.JsCallBackEvent;

import org.greenrobot.eventbus.EventBus;

public class BaiduMapUtils {

    static LocationClient mLocationClient = null;


    /**
     * 定位使用的
     * @param methodName js回调方法名
     * @return
     */
    public static LocationClient startBdLocation(String methodName) {
        return startBdLocation(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyPoiResult myPoiResult = new MyPoiResult();
                myPoiResult.setProvince(location.getProvince());//获取省份
                myPoiResult.setCity(location.getCity()); //获取城市
                myPoiResult.setArea(location.getDistrict());//获取区县
                myPoiResult.setStreet(location.getStreet());//获取街道信息
                myPoiResult.setAddress(location.getAddrStr());//获取详细地址信息
                myPoiResult.setLatitude(location.getLatitude());//获取维度
                myPoiResult.setLongitude(location.getLongitude());//获取经度

                //触发js回调事件
                EventBus.getDefault().post(new JsCallBackEvent(methodName, myPoiResult));
                mLocationClient.stop();
            }
        });
    }

    /**
     * 地图显示当前位置
     * @param mMapView 地图组件View
     * @return
     */
    public static LocationClient startBdLocation(MapView mMapView) {
        //设置定位图层
        BaiduMap map = mMapView.getMap();
        map.setMyLocationEnabled(true);

        return startBdLocation(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                //mapView 销毁后不在处理新接收的位置
                if (location == null || mMapView == null) {
                    return;
                }
                KLog.e(location.toString());
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BaiduMap map = mMapView.getMap();
                        map.setMyLocationData(locData);
                        //设置中心点位置，不然没法显示当前位置的地图
                        //默认18缩放等级
                        float zoomLevel = 18f;
                        if (map.getMapStatus().zoom > 12f) {
                            zoomLevel = map.getMapStatus().zoom;
                        }
                        KLog.e("缩放等级："+zoomLevel);
                        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel);
                        map.setMapStatus(mapStatus);
                    }
                });
            }
        });
    }


    private static LocationClient startBdLocation(BDAbstractLocationListener listener) {
        Context applicationContext = ActivityUtils.getTopActivity().getApplicationContext();
        //声明LocationClient类
        mLocationClient = new LocationClient(applicationContext);

        LocationClientOption mOption = initOptions();
        mLocationClient.setLocOption(mOption);
        //注册监听函数
        mLocationClient.registerLocationListener(listener);

        ThreadUtils.runOnUiThread(() -> mLocationClient.start());

        return mLocationClient;
    }

    /**
     * 定位参数初始化
     *
     * @return
     */
    private static LocationClientOption initOptions() {
        //获取到LocationOption参数
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        // 定位时间间隔
        mOption.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        // 设置需要海拔信息
        mOption.setIsNeedAltitude(true);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        mOption.setOpenGps(true);

        return mOption;
    }

}
