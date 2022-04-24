package com.tyky.map;

import android.os.Bundle;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MapView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.socks.library.KLog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        KLog.d(AppUtils.getAppPackageName());
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mapView);

        String[] permission = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION};
        if (AndPermission.hasPermissions(this, permission)) {
            startLocationInit();
        } else {
            AndPermission.with(this).runtime().permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION).onGranted(new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    startLocationInit();
                }
            }).start();
        }

        findViewById(R.id.ivLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLocationClient == null) {
                    mLocationClient = BaiduMapUtils.startBdLocation(mMapView);
                }
            }
        });
    }

    LocationClient mLocationClient;

    private void startLocationInit() {
        ThreadUtils.getIoPool().submit(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mLocationClient == null) {
                        mLocationClient = BaiduMapUtils.startBdLocation(mMapView);
                    }
                }
        );
    }

    /*LocationClient mLocationClient;

    private void startLocation() {
        if (mLocationClient == null) {
            //定位初始化
            mLocationClient = new LocationClient(this);
        }
        //mMapView.setZ(18);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                //mapView 销毁后不在处理新接收的位置
                if (location == null || mMapView == null) {
                    return;
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMapView.getMap().setMyLocationData(locData);
                        //设置中心点位置，不然没法显示当前位置的地图
                        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18);
                        mMapView.getMap().setMapStatus(mapStatus);
                    }
                });
            }
        });


        //开启地图定位图层
        mLocationClient.start();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        mMapView.getMap().setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}