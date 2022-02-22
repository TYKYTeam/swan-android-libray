package com.tyky.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
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
    private NetWorkListener netWorkListener;
    private PhoneListener phoneListener;

    /**
     * 网络状态监听
     *
     * @param paramStr
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String registerNetworkListener(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);

        String method = paramModel.getCallBackMethod();

        if (!StringUtils.isEmpty(method) && method.contains(",")) {
            String[] split = method.split(",");
            String callback1 = split[0];//网络断开的回调方法
            String callback2 = split[1];//网络连接的回调方法
            if (netWorkListener == null) {
                netWorkListener = new NetWorkListener(callback1, callback2);
                NetworkUtils.registerNetworkStatusChangedListener(netWorkListener);
            }
            //不为空，说明之前已经注册过了，没必要重新注册监听

            return gson.toJson(ResultModel.success(""));
        } else {
            return gson.toJson(ResultModel.errorParam());
        }
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

        if (phoneListener == null) {
            phoneListener = new PhoneListener(methodName);

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
