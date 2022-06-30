package com.tyky.mapNav.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.bikenavi.params.BikeRouteNodeInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.socks.library.KLog;
import com.tyky.mapNav.BaiduMapUtils;
import com.tyky.mapNav.R;

import java.util.ArrayList;

public class BNaviMainActivity extends Activity {

    private final static String TAG = BNaviMainActivity.class.getSimpleName();

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    /*导航起终点Marker，可拖动改变起终点的坐标*/
    private Marker mStartMarker;
    private Marker mEndMarker;

    private LatLng startPt;
    private LatLng endPt;

    private BikeNaviLaunchParam bikeParam;
    private WalkNaviLaunchParam walkParam;

    private static boolean isPermissionRequested = false;

    private BitmapDescriptor bdStart = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_start);
    private BitmapDescriptor bdEnd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_end);

    //类型 0：步行导航 1：骑行导航 2：AR步行导航
    int type = 0;
    //终点名称
    String endName = "";
    private LocationClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);
        requestPermission();
        mMapView = (MapView) findViewById(R.id.mapview);

        //接收传递过来的类型和终点名称
        type = getIntent().getIntExtra("type", 0);
        endName = getIntent().getStringExtra("endName");

        /*骑行导航入口*/
        Button btnStartGuide = (Button) findViewById(R.id.btnStartGuide);
        switch (type) {
            case 0:
                btnStartGuide.setText("开始步行导航");
                break;
            case 1:
                btnStartGuide.setText("开始骑行导航");
                break;
            case 2:
                btnStartGuide.setText("开始AR步行导航");
                break;
        }
        btnStartGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 0:
                        walkParam.extraNaviMode(0);
                        startWalkNavi();
                        break;
                    case 1://骑行导航
                        startBikeNavi();
                        break;
                    case 2://todo AR步行导航
                        walkParam.extraNaviMode(1);
                        startWalkNavi();
                        break;
                }
            }
        });

        //PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", "新一代产业园");
        //PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳", "上梅林地铁站");
        //
        //startPt = stNode.getLocation();
        //endPt = enNode.getLocation();

        client = BaiduMapUtils.startBdLocation(mMapView, new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (isInitMapStatus) {
                    return;
                }
                startPt = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                //更新地图当前位置
                ThreadUtils.runOnUiThread(() -> initMapStatus());

                //调用地址
                GeoCoder geoCoder = GeoCoder.newInstance();
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                            if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                                //没有检索到结果
                                ToastUtils.showShort("终点地点检索失败");
                                finish();
                            } else {

                                double latitude = geoCodeResult.getLocation().latitude;
                                double longitude = geoCodeResult.getLocation().longitude;

                                KLog.d("经纬度：", latitude + "," + longitude);
                                //endPt = new LatLng(22.552085, 114.066566);
                                endPt = new LatLng(latitude, longitude);

                                /*构造导航起终点参数对象*/
                                BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
                                bikeStartNode.setLocation(startPt);
                                BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
                                bikeEndNode.setLocation(endPt);
                                bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

                                WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
                                walkStartNode.setLocation(startPt);
                                WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
                                walkEndNode.setLocation(endPt);
                                walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

                                /* 初始化起终点Marker */
                                ThreadUtils.runOnUiThread(BNaviMainActivity.this::initOverlay);

                                isInitMapStatus = true;
                            }
                        }
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                    }
                });

                KLog.d("终点名称：" + endName);
                geoCoder.geocode(new GeoCodeOption()
                        .city(bdLocation.getCity())
                        .address(endName));

                geoCoder.destroy();
            }
        });


    }

    boolean isInitMapStatus = false;
    /**
     * 初始化地图状态
     */
    private void initMapStatus() {

        mBaiduMap = mMapView.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(startPt).zoom(15);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


    }

    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {

        MarkerOptions ooA = new MarkerOptions().position(startPt).icon(bdStart)
                .zIndex(9).draggable(true);

        mStartMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        mStartMarker.setDraggable(true);
        MarkerOptions ooB = new MarkerOptions().position(endPt).icon(bdEnd)
                .zIndex(5);
        mEndMarker = (Marker) (mBaiduMap.addOverlay(ooB));
        mEndMarker.setDraggable(true);

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                if (marker == mStartMarker) {
                    startPt = marker.getPosition();
                } else if (marker == mEndMarker) {
                    endPt = marker.getPosition();
                }

                BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
                bikeStartNode.setLocation(startPt);
                BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
                bikeEndNode.setLocation(endPt);
                bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

                WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
                walkStartNode.setLocation(startPt);
                WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
                walkEndNode.setLocation(endPt);
                walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 开始骑行导航
     */
    private void startBikeNavi() {
        Log.d(TAG, "startBikeNavi");
        try {
            BikeNavigateHelper.getInstance().initNaviEngine(this, new IBEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "BikeNavi engineInitSuccess");
                    routePlanWithBikeParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "BikeNavi engineInitFail");
                    BikeNavigateHelper.getInstance().unInitNaviEngine();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 开始步行导航
     */
    private void startWalkNavi() {
        Log.d(TAG, "startWalkNavi");
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "WalkNavi engineInitSuccess");
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "WalkNavi engineInitFail");
                    WalkNavigateHelper.getInstance().unInitNaviEngine();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 发起骑行导航算路
     */
    private void routePlanWithBikeParam() {
        BikeNavigateHelper.getInstance().routePlanWithRouteNode(bikeParam, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "BikeNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d(TAG, "BikeNavi onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(BNaviMainActivity.this, BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d(TAG, "BikeNavi onRoutePlanFail");
            }

        });
    }

    /**
     * 发起步行导航算路
     */
    private void routePlanWithWalkParam() {

        WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "WalkNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {

                Log.d(TAG, "onRoutePlanSuccess");

                Intent intent = new Intent();
                intent.setClass(BNaviMainActivity.this, WNaviGuideActivity.class);
                startActivity(intent);

            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "WalkNavi onRoutePlanFail" + error.toString());
            }

        });
    }

    /**
     * Android6.0之后需要动态申请权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;

            ArrayList<String> permissionsList = new ArrayList<>();

            String[] permissions = {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_MULTICAST_STATE


            };

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
                    permissionsList.add(perm);
                    // 进入到这里代表没有权限.
                }
            }

            if (permissionsList.isEmpty()) {
                return;
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 0);
            }
        }
    }

    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        bdStart.recycle();
        bdEnd.recycle();
        client.stop();
    }
}
