package com.tyky.share;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.UriUtils;
import com.google.gson.Gson;
import com.tyky.share.bean.WxShareParamModel;
import com.tyky.share.utils.WxUtils;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import java.io.File;

@WebViewInterface("share")
public class ShareJsInterface {

    Gson gson = GsonUtils.getGson();

    /**
     * 分享文本
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String shareText(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        Intent shareTextIntent = IntentUtils.getShareTextIntent(content);
        shareTextIntent.setType(shareTextIntent.getType());
        ActivityUtils.getTopActivity().startActivity(shareTextIntent);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 分享图片（单张图片）
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String shareImage(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String data = paramModel.getContent();
        if (StringUtils.isEmpty(data)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //判断如果有base64开头，处理一下
        if (data.contains("base64,")) {
            data = org.apache.commons.lang3.StringUtils.substringAfter(data, "base64,");
        }
        byte[] bytes = EncodeUtils.base64Decode(data);
        Bitmap bitmap = ImageUtils.bytes2Bitmap(bytes);

        File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.PNG, true);
        Uri uri = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            UriUtils.file2Uri(file);
        } else {
            uri = Uri.fromFile(file);
        }

        if (file.exists()) {
            Intent shareImageIntent = IntentUtils.getShareImageIntent(uri);
            ActivityUtils.getTopActivity().startActivity(shareImageIntent);
        }
        return gson.toJson(ResultModel.success(""));
    }


    /**
     * 分享图文(单张图片）
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String shareTextImage(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String title = paramModel.getTitle();
        String data = paramModel.getContent();
        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(title)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //判断如果有base64开头，处理一下
        if (data.contains("base64,")) {
            data = org.apache.commons.lang3.StringUtils.substringAfter(data, "base64,");
        }
        byte[] bytes = EncodeUtils.base64Decode(data);
        Bitmap bitmap = ImageUtils.bytes2Bitmap(bytes);

        File file = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.PNG, true);

        Uri uri = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            UriUtils.file2Uri(file);
        } else {
            uri = Uri.fromFile(file);
        }

        if (file.exists()) {
            Intent shareImageIntent = IntentUtils.getShareTextImageIntent(title, uri);
            ActivityUtils.getTopActivity().startActivity(shareImageIntent);
        }
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 直接分享文本到微信好友
     */
    @JavascriptInterface
    public String shareToWechatFriend(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        String weixinPkg = "com.tencent.mm";
        if (!AppUtils.isAppInstalled(weixinPkg)) {
            return gson.toJson(ResultModel.errorParam("分享失败，微信未安装！！"));
        }
        Intent intent = new Intent();
        ComponentName cop = new ComponentName(weixinPkg, "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(cop);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("android.intent.extra.TEXT", content);
        intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityUtils.getTopActivity().startActivity(intent);
        return gson.toJson(ResultModel.success(""));
    }


    /**
     * 调用微信分享SDK分享文本
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String shareTextByWxOrigin(String paramStr) {
        //1.检测微信是否安装
        //2.检测appId是否有数值
        Pair<Boolean, String> booleanStringPair = checkWechatShareParam();
        if (!booleanStringPair.first) {
            return gson.toJson(ResultModel.errorParam(booleanStringPair.second));
        }

        WxShareParamModel paramModel = gson.fromJson(paramStr, WxShareParamModel.class);

        //不能为空
        String shareContent = paramModel.getShareContent();
        if (StringUtils.isEmpty(shareContent)) {
            return gson.toJson(ResultModel.errorParam("shareContent不能为空"));
        }

        String shareDescription = paramModel.getShareDescription();
        if (StringUtils.isEmpty(shareDescription)) {
            return gson.toJson(ResultModel.errorParam("shareDescription不能为空"));
        }

        int shareTargetType = paramModel.getShareTargetType();
        String shareTitle = paramModel.getShareTitle();

        WxUtils.shareText(shareContent, shareTargetType, shareTitle, shareDescription);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 调用微信分享SDK分享图片
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String shareImageByWxOrigin(String paramStr) {
        //1.检测微信是否安装
        //2.检测appId是否有数值
        Pair<Boolean, String> booleanStringPair = checkWechatShareParam();
        if (!booleanStringPair.first) {
            return gson.toJson(ResultModel.errorParam(booleanStringPair.second));
        }

        WxShareParamModel paramModel = gson.fromJson(paramStr, WxShareParamModel.class);
        Integer type = paramModel.getType();
        if (type == null) {
            return gson.toJson(ResultModel.errorParam("type不能为空"));
        }

        //不能为空
        String shareContent = paramModel.getShareContent();
        if (StringUtils.isEmpty(shareContent)) {
            return gson.toJson(ResultModel.errorParam("shareContent不能为空"));
        }

        String shareDescription = paramModel.getShareDescription();
        if (StringUtils.isEmpty(shareDescription)) {
            return gson.toJson(ResultModel.errorParam("shareDescription不能为空"));
        }

        int shareTargetType = paramModel.getShareTargetType();
        String shareTitle = paramModel.getShareTitle();

        //0：传base64字符串 1：传手机本机路径，如/storage/emulated/0/DCIM/com.tyky.guizhou.dangjian.party/1667294414888_100.JPG
        if (type == 0) {
            WxUtils.sharePicture(shareContent, shareTargetType, shareTitle, shareDescription);
        } else {
            WxUtils.sharePictureByImgFilePath(shareContent, shareTargetType, shareTitle, shareDescription);
        }
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 调用微信分享SDK分享视频(分享给好友会有卡片形式）
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String shareVideoByWxOrigin(String paramStr) {
        //1.检测微信是否安装
        //2.检测appId是否有数值
        Pair<Boolean, String> booleanStringPair = checkWechatShareParam();
        if (!booleanStringPair.first) {
            return gson.toJson(ResultModel.errorParam(booleanStringPair.second));
        }

        WxShareParamModel paramModel = gson.fromJson(paramStr, WxShareParamModel.class);

        //不能为空
        String shareContent = paramModel.getShareContent();
        if (StringUtils.isEmpty(shareContent)) {
            return gson.toJson(ResultModel.errorParam("shareContent不能为空"));
        }

        String shareDescription = paramModel.getShareDescription();
        if (StringUtils.isEmpty(shareDescription)) {
            return gson.toJson(ResultModel.errorParam("shareDescription不能为空"));
        }

        int shareTargetType = paramModel.getShareTargetType();
        String shareTitle = paramModel.getShareTitle();

        //缩略图base64
        String thumbData = paramModel.getShareThumbData();

        WxUtils.shareVideo(shareContent, shareTargetType, shareTitle, shareDescription,thumbData);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 调用微信分享SDK分享网页链接（分享给好友会有卡片形式）
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String shareWebUrlByWxOrigin(String paramStr) {
        //1.检测微信是否安装
        //2.检测appId是否有数值
        Pair<Boolean, String> booleanStringPair = checkWechatShareParam();
        if (!booleanStringPair.first) {
            return gson.toJson(ResultModel.errorParam(booleanStringPair.second));
        }

        WxShareParamModel paramModel = gson.fromJson(paramStr, WxShareParamModel.class);

        //不能为空
        String shareContent = paramModel.getShareContent();
        if (StringUtils.isEmpty(shareContent)) {
            return gson.toJson(ResultModel.errorParam("shareContent不能为空"));
        }

        String shareDescription = paramModel.getShareDescription();
        if (StringUtils.isEmpty(shareDescription)) {
            return gson.toJson(ResultModel.errorParam("shareDescription不能为空"));
        }

        int shareTargetType = paramModel.getShareTargetType();
        String shareTitle = paramModel.getShareTitle();

        //缩略图base64
        String thumbData = paramModel.getShareThumbData();

        WxUtils.shareWeb(shareContent, shareTargetType, shareTitle, shareDescription,thumbData);
        return gson.toJson(ResultModel.success(""));
    }


    /**
     * 检测微信是否安装及appId是否有数值
     *
     * @return 返回Pair, Boolean代表是否通过验证，String为错误提示信息
     */
    private Pair<Boolean, String> checkWechatShareParam() {
        String weixinPkg = "com.tencent.mm";
        if (!AppUtils.isAppInstalled(weixinPkg)) {
            return Pair.create(false, "分享失败，微信未安装！！");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(WxUtils.appId)) {
            return Pair.create(false, "分享失败，微信APP_ID数值为空，请在远程编译时候填写对应配置项！！");
        }

        return Pair.create(true, "");
    }

}
