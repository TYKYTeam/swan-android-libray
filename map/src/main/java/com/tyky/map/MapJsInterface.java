package com.tyky.map;

import android.webkit.JavascriptInterface;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.IntentEvent;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;

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
     * 在地图显示当前位置
     *
     * @return
     */
    @JavascriptInterface
    public String showLocationInMap() {
        EventBus.getDefault().post(new IntentEvent(MapActivity.class));
        return gson.toJson(ResultModel.success(""));
    }

    @JavascriptInterface
    public void poiSearch(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String methodName = paramModel.getCallBackMethod();
        String keyword = paramModel.getKeyword();
        String city = paramModel.getCity();
        String tags = paramModel.getTags();

        PoiSearch poiSearch = PoiSearch.newInstance();

        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                EventBus.getDefault().post(new JsCallBackEvent(methodName,poiResult));
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
    }


}
