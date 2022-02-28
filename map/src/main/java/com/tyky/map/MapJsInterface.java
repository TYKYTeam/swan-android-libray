package com.tyky.map;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

@WebViewInterface("map")
public class MapJsInterface {

    Gson gson = GsonUtils.getGson();


    /**
     * 开始定位
     *
     * @return
     */
    @JavascriptInterface
    public String startLocation(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String methodName = paramModel.getCallBackMethod();
        if (StringUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        Context applicationContext = ActivityUtils.getTopActivity().getApplicationContext();
        if (AndPermission.hasPermissions(applicationContext, Permission.ACCESS_FINE_LOCATION)) {
            startBdLocation(methodName);
        } else {
            AndPermission.with(applicationContext).runtime()
                    .permission(Permission.ACCESS_FINE_LOCATION)
                    .onGranted(data -> startBdLocation(methodName))
                    .start();
            return gson.toJson(ResultModel.errorPermission());
        }

        return gson.toJson(ResultModel.success(""));
    }


    LocationClient mLocationClient = null;

    BaiduLocation.Location location1;

    private void startBdLocation(String methodName) {
        Context applicationContext = ActivityUtils.getTopActivity().getApplicationContext();
        if (mLocationClient == null) {
            //声明LocationClient类
            mLocationClient = new LocationClient(applicationContext);

            //获取到LocationOption参数
            LocationClientOption mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            // 定位时间间隔
            mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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

            mLocationClient.setLocOption(mOption);
            //注册监听函数
            mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation location) {

                    //把第一次获取的位置信息保存
                    //由于没有申请AppKey，只要在第一次进入APP中并且首次发起定位可以获取到位置信息，所以采用此种方式，对数据进行缓存

                    if (location1 == null) {
                        location1 = new BaiduLocation().new Location();
                        location1.setProvince(location.getProvince()); //获取省份
                        location1.setCity(location.getCity());  //获取城市
                        location1.setDistrict(location.getDistrict());//获取区县
                        location1.setStreet(location.getStreet());//获取街道信息
                        location1.setAddrStr(location.getAddrStr());//获取详细地址信息
                    }


                    //触发js回调事件
                    EventBus.getDefault().post(new JsCallBackEvent(methodName, location1));

                    mLocationClient.stop();
                }
            });
        }
        mLocationClient.start();
    }
}
