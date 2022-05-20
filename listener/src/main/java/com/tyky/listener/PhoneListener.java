package com.tyky.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.blankj.utilcode.util.SPUtils;
import com.tyky.webviewBase.event.JsCallBackEvent;

import org.greenrobot.eventbus.EventBus;

public class PhoneListener extends PhoneStateListener {



    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:// 电话挂断
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: //电话通话的状态
                break;
            case TelephonyManager.CALL_STATE_RINGING: //电话响铃的状态
                String phoneCallMethod = SPUtils.getInstance().getString("phoneCallMethod");
                EventBus.getDefault().post(new JsCallBackEvent(phoneCallMethod, ""));
                break;
        }

    }

}
