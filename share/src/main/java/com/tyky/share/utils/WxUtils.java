package com.tyky.share.utils;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.MetaDataUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.File;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

/**
 * 参考文档 [WXMediaMessage （微信媒体消息内容）说明 | 微信开放文档](https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Share_and_Favorites/Android.html)
 * 微信分享工具类(没有对接小程序和音乐）
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
     *
     * @param text        文本内容（长度需大于 0 且不超过 10KB）
     * @param flag        0：好友 1：朋友圈
     * @param title       分享标题
     * @param description 分享描述
     */
    public static void shareText(String text, int flag, String title, String description) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.title = title;
        msg.description = description;
        sendMessage(msg, flag);
    }

    /**
     * 分享图片
     *
     * @param imgBase64   图片base64数据
     * @param flag        0：好友 1：朋友圈
     * @param title       分享标题
     * @param description 分享描述
     */
    public static void sharePicture(String imgBase64, int flag, String title, String description) {
        //base64数据处理
        String data = imgBase64;
        if (data.contains("base64,")) {
            data = org.apache.commons.lang3.StringUtils.substringAfter(data, "base64,");
        }
        byte[] bytes = EncodeUtils.base64Decode(data);
        Bitmap bitmap = ImageUtils.bytes2Bitmap(bytes);

        //使用file存放，突破微信分享的限制
        File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.PNG);
        sharePictureByImgFilePath(file.getPath(), flag, title, description);
    }

    /**
     * 分享图片
     *
     * @param imgFilePath 图片本地路径
     * @param flag        0：好友 1：朋友圈
     * @param title       分享标题
     * @param description 分享描述
     */
    public static void sharePictureByImgFilePath(String imgFilePath, int flag, String title, String description) {
        //初始化一个 WXImageObject 对象，填写分享图片
        WXImageObject wxImageObject = new WXImageObject();
        wxImageObject.imagePath = imgFilePath;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        msg.title = title;
        msg.description = description;
        sendMessage(msg, flag);
    }

    /**
     * 分享视频
     *
     * @param videoUrl    视频链接
     * @param flag        0：好友 1：朋友圈
     * @param title       分享标题
     * @param description 分享描述
     */
    public static void shareVideo(String videoUrl, int flag, String title, String description) {
        //初始化一个 WXImageObject 对象，填写分享图片
        WXVideoObject wxImageObject = new WXVideoObject();
        wxImageObject.videoUrl = videoUrl;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        msg.title = title;
        msg.description = description;
        sendMessage(msg, flag);
    }

    /**
     * 分享网页
     *
     * @param webUrl      网页链接
     * @param flag        0：好友 1：朋友圈
     * @param title       分享标题
     * @param description 分享描述
     */
    public static void shareWeb(String webUrl, int flag, String title, String description) {
        //初始化一个 WXImageObject 对象，填写分享图片
        WXWebpageObject wxImageObject = new WXWebpageObject();
        wxImageObject.webpageUrl = webUrl;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        msg.title = title;
        msg.description = description;
        sendMessage(msg, flag);
    }


    /**
     * @param msg  分享内容实体数据
     * @param flag 0：好友 1：朋友圈
     */
    private static void sendMessage(WXMediaMessage msg, int flag) {
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
