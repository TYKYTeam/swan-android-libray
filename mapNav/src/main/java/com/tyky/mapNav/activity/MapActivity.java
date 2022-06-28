package com.tyky.mapNav.activity;

import android.os.Bundle;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tyky.mapNav.BaiduMapUtils;
import com.tyky.mapNav.R;
import com.tyky.mapNav.bean.MapParamModel;
import com.tyky.mapNav.listener.MyRoutePlanResultListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    private MapView mMapView = null;
    /**
     * 类型：0：显示当前位置 1：步行规划 2：骑行规划 3：驾车规划
     */
    private int type;
    private MapParamModel data;
    private RoutePlanSearch routePlanSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        type = getIntent().getIntExtra("type", 0);
        data = (MapParamModel) getIntent().getSerializableExtra("data");

        //获取地图控件引用
        mMapView = findViewById(R.id.mapView);

        String[] permission = {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION};
        if (AndPermission.hasPermissions(this, permission)) {
            doAction();
        } else {
            AndPermission.with(this).runtime().permission(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION).onGranted(new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    doAction();
                }
            }).start();
        }

        findViewById(R.id.ivLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationClient.stop();
                mLocationClient = null;
                mLocationClient = BaiduMapUtils.restartBdLocation(mMapView);
            }
        });
    }

    private void doAction() {
        //根据type执行不同操作
        switch (type) {
            case 1:
            case 2:
            case 3:
                routeSearch();
                break;
            default:
                startLocationInit();
                break;
        }
    }

    /**
     * 规划
     */
    private void routeSearch() {
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(new MyRoutePlanResultListener(type,mMapView.getMap()));
        //根据地点名来构建起点和终点
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(data.getStartCityName(), data.getStartName());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(data.getEndCityName(), data.getEndName());
        //1:步行 2：骑行 3：驾车
        switch (type) {
            case 1:
                //构建起始点规划参数
                WalkingRoutePlanOption walkingRoutePlanOption = new WalkingRoutePlanOption().from(stNode).to(enNode);
                routePlanSearch.walkingSearch(walkingRoutePlanOption);
                break;
            case 2:
                //构建起始点规划参数
                BikingRoutePlanOption bikingRoutePlanOption = new BikingRoutePlanOption().from(stNode).to(enNode);
                routePlanSearch.bikingSearch(bikingRoutePlanOption);
                break;
            case 3:
                //构建起始点规划参数
                DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption().from(stNode).to(enNode);
                routePlanSearch.drivingSearch(drivingRoutePlanOption);
                break;
            default:break;
        }

    }


    LocationClient mLocationClient;

    private void startLocationInit() {
        ToastUtils.showShort("定位中，请稍候");
        ThreadUtils.getIoPool().submit(() -> {
                    try {
                        Thread.sleep(2000);
                        if (mLocationClient == null) {
                            mLocationClient = BaiduMapUtils.startBdLocation(mMapView);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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

        if (routePlanSearch != null) {
            routePlanSearch.destroy();
        }
        super.onDestroy();
    }
}