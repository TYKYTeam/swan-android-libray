package com.tyky.handware.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BlueToothBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle b = intent.getExtras();
        Object[] lstName = b.keySet().toArray();

        // 显示所有收到的消息及其细节
        for (int i = 0; i < lstName.length; i++) {
            String keyName = lstName[i].toString();
            Log.e("bluetooth", keyName + ">>>" + String.valueOf(b.get(keyName)));
        }
        BluetoothDevice device;
        // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
       /* if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            onRegisterBltReceiver.onBluetoothDevice(device);
        }
        //状态改变时
        else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING://正在配对
                    Log.d("BlueToothTestActivity", "正在配对......");
                    onRegisterBltReceiver.onBltIng(device);
                    break;
                case BluetoothDevice.BOND_BONDED://配对结束
                    Log.d("BlueToothTestActivity", "完成配对");
                    onRegisterBltReceiver.onBltEnd(device);
                    break;
                case BluetoothDevice.BOND_NONE://取消配对/未配对
                    Log.d("BlueToothTestActivity", "取消配对");
                    onRegisterBltReceiver.onBltNone(device);
                default:
                    break;
            }
        }*/
    }
}
