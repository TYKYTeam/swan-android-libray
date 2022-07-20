package com.tyky.filepreview;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PathUtils;
import com.google.gson.Gson;
import com.tyky.filepreview.activity.PdfPreviewActivity;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

import java.io.File;

@WebViewInterface("filePreview")
public class FilePreviewJsInterface {

    Gson gson = GsonUtils.getGson();

    /**
     * 预览pdf文件
     */
    @JavascriptInterface
    public String previewPdf(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String content = paramModel.getContent();
        if (TextUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        //将文件内容写入文件，之后再读取（防止数据过大导致Intent传递数据失败）
        File file = new File(PathUtils.getExternalAppCachePath(), System.currentTimeMillis() + ".txt");
        FileIOUtils.writeFileFromString(file, content);
        //文件路径存在bundle中
        Bundle bundle = new Bundle();
        bundle.putString("filePath", file.getPath());
        ActivityUtils.startActivity(bundle, PdfPreviewActivity.class);
        return gson.toJson(ResultModel.success(""));
    }
}
