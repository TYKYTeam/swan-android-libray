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

    /*???????????????Marker????????????????????????????????????*/
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

    //?????? 0??????????????? 1??????????????? 2???AR????????????
    int type = 0;
    //????????????
    String endName = "";
    private LocationClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_main);
        requestPermission();
        mMapView = (MapView) findViewById(R.id.mapview);

        //??????????????????????????????????????????
        type = getIntent().getIntExtra("type", 0);
        endName = getIntent().getStringExtra("endName");

        /*??????????????????*/
        Button btnStartGuide = (Button) findViewById(R.id.btnStartGuide);
        switch (type) {
            case 0:
                btnStartGuide.setText("??????????????????");
                break;
            case 1:
                btnStartGuide.setText("??????????????????");
                break;
            case 2:
                btnStartGuide.setText("??????AR????????????");
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
                    case 1://????????????
                        startBikeNavi();
                        break;
                    case 2://todo AR????????????
                        walkParam.extraNaviMode(1);
                        startWalkNavi();
                        break;
                }
            }
        });

        //PlanNode stNode = PlanNode.withCityNameAndPlaceName("??????", "??????????????????");
        //PlanNode enNode = PlanNode.withCityNameAndPlaceName("??????", "??????????????????");
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

                //????????????????????????
                ThreadUtils.runOnUiThread(() -> initMapStatus());

                //????????????
                GeoCoder geoCoder = GeoCoder.newInstance();
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                            if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                                //?????????????????????
                                ToastUtils.showShort("????????????????????????");
                                finish();
                            } else {

                                double latitude = geoCodeResult.getLocation().latitude;
                                double longitude = geoCodeResult.getLocation().longitude;

                                KLog.d("????????????", latitude + "," + longitude);
                                //endPt = new LatLng(22.552085, 114.066566);
                                endPt = new LatLng(latitude, longitude);

                                /*?????????????????????????????????*/
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

                                /* ??????????????????Marker */
                                ThreadUtils.runOnUiThread(BNaviMainActivity.this::initOverlay);

                                isInitMapStatus = true;
                            }
                        }
                    }

                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                    }
                });

                KLog.d("???????????????" + endName);
                geoCoder.geocode(new GeoCodeOption()
                        .city(bdLocation.getCity())
                        .address(endName));

                geoCoder.destroy();
            }
        });


    }

    boolean isInitMapStatus = false;
    /**
     * ?????????????????????
     */
    private void initMapStatus() {

        mBaiduMap = mMapView.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(startPt).zoom(15);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


    }

    /**
     * ????????????????????????Marker
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
     * ??????????????????
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
     * ??????????????????
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
     * ????????????????????????
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
     * ????????????????????????
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
     * Android6.0??????????????????????????????
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
                    // ?????????????????????????????????.
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
