package com.tyky.listener;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ResultModel;

import org.greenrobot.eventbus.EventBus;

public class PhoneListener extends PhoneStateListener {

    private String callbackMethodName;

    public PhoneListener(String callbackMethodName) {
        this.callbackMethodName = callbackMethodName;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:// 电话挂断
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: //电话通话的状态
                break;
            case TelephonyManager.CALL_STATE_RINGING: //电话响铃的状态
                EventBus.getDefault().post(new JsCallBackEvent(callbackMethodName, ResultModel.success("")));
                break;
        }

    }

}
