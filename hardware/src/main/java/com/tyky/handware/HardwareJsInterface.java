package com.tyky.handware;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BrightnessUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.MetaDataUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.VolumeUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tyky.handware.bluetooth.BlueToothUtils;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import androidx.annotation.RequiresPermission;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

@WebViewInterface("hardware")
public class HardwareJsInterface {
    Gson gson = GsonUtils.getGson();

    /**
     * 获取电量
     *
     * @return
     */
    @JavascriptInterface
    public String getBattery() {
        Intent intent = ActivityUtils.getTopActivity().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //判断它是否是为电量变化的Broadcast Action
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            //获取当前电量
            int level = intent.getIntExtra("level", 0);
            //电量的总刻度
            int scale = intent.getIntExtra("scale", 100);
            //把它转成百分比
            String result = ((level * 100) / scale) + "%";
            return gson.toJson(ResultModel.success(result));
        }
        return gson.toJson(ResultModel.errorParam(""));

    }

    /**
     * 获取网络状态
     *
     * @return type of network
     * <ul>
     * <li>NETWORK_ETHERNET </li>
     * <li>NETWORK_WIFI     </li>
     * <li>NETWORK_4G       </li>
     * <li>NETWORK_3G       </li>
     * <li>NETWORK_2G       </li>
     * <li>NETWORK_UNKNOWN  </li>
     * <li>NETWORK_NO       </li>
     * </ul>
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String getNetworkState() {
        String name = NetworkUtils.getNetworkType().name();
        return gson.toJson(ResultModel.success(name));
    }

    /**
     * 获取最大媒体音量
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String getMaxVolume() {
        int volume = VolumeUtils.getMaxVolume(AudioManager.STREAM_MUSIC);
        return gson.toJson(ResultModel.success(volume));
    }

    /**
     * 获取媒体音量
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String getVolume() {
        int volume = VolumeUtils.getVolume(AudioManager.STREAM_MUSIC);
        return gson.toJson(ResultModel.success(volume));
    }

    /**
     * 设置媒体音量
     *
     * @return
     */
    @JavascriptInterface
    public String setVolume(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        int volume = Integer.parseInt(content);
        VolumeUtils.setVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);

        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 获取屏幕亮度
     *
     * @return
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    @JavascriptInterface
    public String getBrightness() {
        String bugly_appid = MetaDataUtils.getMetaDataInApp("BUGLY_APPID");
        KLog.d(bugly_appid);
        int brightness = BrightnessUtils.getBrightness();
        return gson.toJson(ResultModel.success(brightness));
    }

    /**
     * 设置屏幕亮度
     *
     * @return
     */
    @JavascriptInterface
    public String setBrightness(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        int brightness = Integer.parseInt(content);


        Activity topActivity = ActivityUtils.getTopActivity();
        //版本号大于
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //是否有Setting权限
            if (!Settings.System.canWrite(topActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + AppUtils.getAppPackageName()));
                topActivity.startActivity(intent);
            } else {
                BrightnessUtils.setBrightness(brightness);
                return gson.toJson(ResultModel.success(""));
            }

            return gson.toJson(ResultModel.errorPermission("未授予权限，设置亮度失败"));
        }
        return gson.toJson(ResultModel.success(""));

    }

    @JavascriptInterface
    public String bluetoothConnect() {

        boolean open = BlueToothUtils.open();
        if (open) {
            return gson.toJson(ResultModel.success(""));
        } else {
            return gson.toJson(ResultModel.errorParam("蓝牙服务打开失败!"));
        }

    }
}
