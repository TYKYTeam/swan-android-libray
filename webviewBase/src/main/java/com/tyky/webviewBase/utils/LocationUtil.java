package com.tyky.webviewBase.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Pair;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tyky.webviewBase.model.ResultModel;

public class LocationUtil {
    /**
     * 检测是否开启gps定位服务
     *
     * @return
     */
    public static Pair<Boolean, ResultModel> checkLocationService() {
        //从系统服务中获取定位管理器
        LocationManager lm = (LocationManager) ActivityUtils.getTopActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!providerEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ActivityUtils.startActivity(intent);
            String tip = "定位服务未开启，请先开启定位服务！";
            ToastUtils.showShort(tip);
            return Pair.create(providerEnabled, ResultModel.errorParam(tip));
        }
        return Pair.create(providerEnabled, null);
    }
}
