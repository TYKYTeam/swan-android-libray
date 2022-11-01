package com.tyky.share.utils;

import com.blankj.utilcode.util.MetaDataUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

/**
 * 参考文档 [WXMediaMessage （微信媒体消息内容）说明 | 微信开放文档](https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Share_and_Favorites/Android.html)
 * 微信分享工具类
 *
 * @author starsone
 */
public class WxUtils {

    //从meta里读取微信平台的appId
    public static String appId = MetaDataUtils.getMetaDataInApp("wechat_app_id");

    // IWXAPI 是第三方 app 和微信通信的 openApi 接口 初始化在ShareInitializer类中
    public static IWXAPI api;

    /**
     * 分享文本
     */
    public static void shareText() {

    }

    private static WXMediaMessage generateObj() {
        WXImageObject wxImageObject = new WXImageObject();

        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = "分享文本";

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = "分享文本";
        return msg;
    }

    /**
     * @param flag 0：好友 1：朋友圈
     */
    private static void sendMessage(int flag) {
        WXMediaMessage msg = generateObj();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());  //transaction字段用与唯一标示一个请求
        req.message = msg;

        //朋友圈:WXSceneTimeline
        //会话:WXSceneSession
        if (flag == 1) {
            req.scene = WXSceneTimeline;
        } else {
            req.scene = WXSceneSession;
        }

        //调用 api 接口，发送数据到微信
        api.sendReq(req);
    }
}
