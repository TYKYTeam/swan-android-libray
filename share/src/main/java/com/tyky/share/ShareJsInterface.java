package com.tyky.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import java.io.File;

@WebViewInterface("share")
public class ShareJsInterface {

    Gson gson = new Gson();

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
        shareTextIntent.setType(shareTextIntent.getType() );
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

        String filePath = PathUtils.getExternalAppFilesPath() + System.currentTimeMillis() + ".png";

        File file = ImageUtils.save2Album(bitmap, filePath, Bitmap.CompressFormat.PNG, true);
        if (file.exists()) {
            Intent shareImageIntent = IntentUtils.getShareImageIntent(file);
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

        String filePath = PathUtils.getExternalAppFilesPath() + System.currentTimeMillis() + ".png";

        File file = ImageUtils.save2Album(bitmap, filePath, Bitmap.CompressFormat.PNG, true);
        if (file.exists()) {
            Intent shareImageIntent = IntentUtils.getShareTextImageIntent(title, file);
            ActivityUtils.getTopActivity().startActivity(shareImageIntent);
        }
        return gson.toJson(ResultModel.success(""));
    }


}
