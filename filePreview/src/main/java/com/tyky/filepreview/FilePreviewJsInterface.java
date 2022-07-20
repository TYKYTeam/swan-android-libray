package com.tyky.filepreview;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.tyky.filepreview.activity.PdfPreviewActivity;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("filePreview")
public class FilePreviewJsInterface {

    Gson gson = GsonUtils.getGson();

    /**
     * 预览文件（不包含图片）
     */
    @JavascriptInterface
    public String previewPdf(String paramStr) {
        ActivityUtils.startActivity(PdfPreviewActivity.class);
        return gson.toJson(ResultModel.success(""));
    }
}
