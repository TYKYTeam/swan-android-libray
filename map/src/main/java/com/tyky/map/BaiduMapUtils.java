package com.tyky.map;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;
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
     * 发起定位
     * @param listener
     * @return
     */
    public static LocationClient startBdLocation(BDAbstractLocationListener listener) {
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
     * 停止定位
     */
    public static void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
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
