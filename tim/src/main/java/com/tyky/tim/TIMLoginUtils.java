package com.tyky.tim;

import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.socks.library.KLog;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMManagerImpl;
import com.tencent.qcloud.tim.demo.DemoApplication;
import com.tencent.qcloud.tim.demo.bean.UserInfo;
import com.tencent.qcloud.tim.demo.main.MainActivity;
import com.tencent.qcloud.tim.demo.signature.GenerateTestUserSig;
import com.tencent.qcloud.tim.demo.utils.TUIUtils;

public class TIMLoginUtils {

    static String TAG = TIMLoginUtils.class.getName();

    public static void logout() {
        V2TIMManagerImpl.getInstance().logout(null);
    }

    /**
     * Im登录
     *
     * @param userId        登录的userId
     * @param userSignature 登录的签名
     * @param isGotoPage    登录成功后是否跳转到首页
     */
    public static void login(String userId, String userSignature, boolean isGotoPage) {

        UserInfo mUserInfo = UserInfo.getInstance();
        int loginStatus = V2TIMManagerImpl.getInstance().getLoginStatus();
        String userSig = userSignature;
        if (TextUtils.isEmpty(userSig)) {
            // 获取userSig函数
            userSig = GenerateTestUserSig.genTestUserSig(userId);
        }

        //判断是否登录
        if (loginStatus == V2TIMManager.V2TIM_STATUS_LOGINED) {//已登录
            doCheckGotoPage(isGotoPage);
        } else {
            DemoApplication.instance().init();
            mUserInfo.setUserId(userId);
            mUserInfo.setUserSig(userSig);

            TUIUtils.login(mUserInfo.getUserId(), mUserInfo.getUserSig(), new V2TIMCallback() {
                @Override
                public void onError(final int code, final String desc) {
                    KLog.e(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
                }

                @Override
                public void onSuccess() {
                    doCheckGotoPage(isGotoPage);
                }
            });
        }
    }

    private static void doCheckGotoPage(boolean isGotoPage) {
        if (isGotoPage) {
            //进入聊天页面
            gotoChatPage();
        }
    }

    private static void gotoChatPage() {
        UserInfo.getInstance().setAutoLogin(true);
        ActivityUtils.startActivity(MainActivity.class);
    }
}
