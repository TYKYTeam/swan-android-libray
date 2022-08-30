package com.tyky.page.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.blankj.utilcode.util.ActivityUtils;

public class WXLaunchMiniUtil {


    public String appId;//微信开发者平台APPP的id
    public String userName;// 小程序原始id
    public String path;//拉起小程序页面的可带参路径，不填默认拉起小程序首页
    public String miniprogramType;//0正式版 1开发版 2体验版

    //跳转
    public void sendReq() {
        ContentResolver resolver = ActivityUtils.getTopActivity().getContentResolver();
        Uri uri = Uri.parse("content://com.tencent.mm.sdk.comm.provider/launchWXMiniprogram");
        String[] path = new String[]{this.appId, this.userName, this.path, this.miniprogramType, ""};
        Cursor cursor;
        if ((cursor = resolver.query(uri, null, null, path, null)) != null) {
            cursor.close();
        }
    }

}
