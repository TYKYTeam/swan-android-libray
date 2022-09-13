package com.tyky.handware.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.IntentFilter;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;

public class BlueToothUtils {

    Gson gson = GsonUtils.getGson();

    /*
     * 蓝牙连接
     */
    public static boolean open() {
        //1.获取蓝牙管理服务BluetoothManager
        Activity topActivity = ActivityUtils.getTopActivity();
        BluetoothManager bluetoothManager = (BluetoothManager) topActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return false;
        }
        //2.获取蓝牙适配器Adapter
        BluetoothAdapter adapter = bluetoothManager.getAdapter();

        // 用BroadcastReceiver来取得搜索结果
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        topActivity.registerReceiver(new BlueToothBroadcastReceiver(), intent);



        return true;
    }

}
