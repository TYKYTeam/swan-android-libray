package com.tyky.mapNav.listener;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.blankj.utilcode.util.ToastUtils;
import com.socks.library.KLog;
import com.tyky.mapNav.overlayutil.BikingRouteOverlay;
import com.tyky.mapNav.overlayutil.DrivingRouteOverlay;
import com.tyky.mapNav.overlayutil.WalkingRouteOverlay;


public class MyRoutePlanResultListener implements OnGetRoutePlanResultListener {


    private BaiduMap baiduMap;

    public MyRoutePlanResultListener(int type, BaiduMap baiduMap) {

        this.baiduMap = baiduMap;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            KLog.d("数量：" + walkingRouteResult.getRouteLines().size());

            //创建WalkingRouteOverlay实例
            WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
            if (walkingRouteResult.getRouteLines().size() > 0) {
                //获取路径规划数据,(以返回的第一条数据为例)
                //为WalkingRouteOverlay实例设置路径数据
                WalkingRouteLine walkingRouteLine = walkingRouteResult.getRouteLines().get(0);
                overlay.setData(walkingRouteLine);
                //在地图上绘制WalkingRouteOverlay
                overlay.addToMap();

                //设置地图中心点
                int middle = walkingRouteLine.getAllStep().size() / 2;
                LatLng location = walkingRouteLine.getAllStep().get(middle).getEntrance().getLocation();
                MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 12f);
                baiduMap.setMapStatus(mapStatus);
            } else {
                ToastUtils.showShort("未找到对应规划！");
            }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        KLog.d("数量：" + drivingRouteResult.getRouteLines().size());

        //创建WalkingRouteOverlay实例
        DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
        if (drivingRouteResult.getRouteLines().size() > 0) {
            //获取路径规划数据,(以返回的第一条数据为例)
            //为WalkingRouteOverlay实例设置路径数据
            DrivingRouteLine drivingRouteLine = drivingRouteResult.getRouteLines().get(0);
            overlay.setData(drivingRouteLine);
            //在地图上绘制WalkingRouteOverlay
            overlay.addToMap();

            //设置地图中心点
            int middle = drivingRouteLine.getAllStep().size() / 2;
            LatLng location = drivingRouteLine.getAllStep().get(middle).getEntrance().getLocation();
            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 12f);
            baiduMap.setMapStatus(mapStatus);
        } else {
            ToastUtils.showShort("未找到对应规划！");
        }

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        KLog.d("数量：" + bikingRouteResult.getRouteLines().size());

        //创建WalkingRouteOverlay实例
        BikingRouteOverlay overlay = new BikingRouteOverlay(baiduMap);
        if (bikingRouteResult.getRouteLines().size() > 0) {
            //获取路径规划数据,(以返回的第一条数据为例)
            //为WalkingRouteOverlay实例设置路径数据
            BikingRouteLine bikingRouteLine = bikingRouteResult.getRouteLines().get(0);
            overlay.setData(bikingRouteLine);
            //在地图上绘制WalkingRouteOverlay
            overlay.addToMap();

            //设置地图中心点
            int middle = bikingRouteLine.getAllStep().size() / 2;
            LatLng location = bikingRouteLine.getAllStep().get(middle).getEntrance().getLocation();
            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 12f);
            baiduMap.setMapStatus(mapStatus);
        } else {
            ToastUtils.showShort("未找到对应规划！");
        }
    }

    private void doAction(SearchResult result) {

    }
}
