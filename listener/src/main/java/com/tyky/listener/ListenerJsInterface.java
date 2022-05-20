package com.tyky.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import androidx.annotation.RequiresPermission;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

@WebViewInterface("listener")
public class ListenerJsInterface {

    Gson gson = GsonUtils.getGson();
    private PhoneListener phoneListener;

    /**
     * 网络连接断开状态监听
     *
     * @param paramStr
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String registerNetworkDisconnectionListener(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);

        String method = paramModel.getCallBackMethod();
        if (StringUtils.isTrimEmpty(method)) {
            return gson.toJson(ResultModel.errorParam());
        }

        //将方法存本地，之后监听器取出方法名并回调JS
        SPUtils.getInstance().put("disconnectCallbackMethodName", method);

        NetWorkListener netWorkListener = GlobalListenerStorage.netWorkListener;

        if (netWorkListener == null) {
            //开启时，把之前留存的网络连接状态监听回调方法名置空
            SPUtils.getInstance().put("connectCallbackMethodName", "");

            GlobalListenerStorage.netWorkListener = new NetWorkListener();
            NetworkUtils.registerNetworkStatusChangedListener(GlobalListenerStorage.netWorkListener);
        }

        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 网络已连接状态监听
     *
     * @param paramStr
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String registerNetworkConnectionListener(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);

        String method = paramModel.getCallBackMethod();
        if (StringUtils.isTrimEmpty(method)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //将方法存本地，之后监听器取出方法名并回调JS
        SPUtils.getInstance().put("connectCallbackMethodName", method);

        NetWorkListener netWorkListener = GlobalListenerStorage.netWorkListener;
        if (netWorkListener == null) {
            //开启时，把之前留存的断开状态方法名置空
            SPUtils.getInstance().put("disconnectCallbackMethodName", "");

            GlobalListenerStorage.netWorkListener = new NetWorkListener();
            NetworkUtils.registerNetworkStatusChangedListener(GlobalListenerStorage.netWorkListener);
        }
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 来电监听
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String registerPhoneListener(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);

        String methodName = paramModel.getCallBackMethod();
        if (StringUtils.isEmpty(methodName)) {
            return gson.toJson(ResultModel.errorParam());
        }

        SPUtils.getInstance().put("phoneCallMethod",methodName);
        if (phoneListener == null) {
            phoneListener = new PhoneListener();

            //获得相应的系统服务
            TelephonyManager tm = (TelephonyManager) ActivityUtils.getTopActivity().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                try {
                    // 注册来电监听
                    tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                } catch (Exception e) {
                    // 异常捕捉
                    KLog.e(e);
                }
            }
        }

        return gson.toJson(ResultModel.success(""));
    }


}
