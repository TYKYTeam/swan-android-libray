package com.tyky.update;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.tyky.update.bean.UpdateParamModel;
import com.tyky.update.dialog.CheckVersionDialog;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("update")
public class UpdateJsInterface {
    Gson gson = GsonUtils.getGson();

    /**
     * 弹出更新提示框
     */
    public String showUpdateDialog(String paramStr) {
        UpdateParamModel updateParamModel = gson.fromJson(paramStr, UpdateParamModel.class);
        CheckVersionDialog.show(updateParamModel);
        return gson.toJson(ResultModel.success(""));
    }


    /**
     * 下载文件并显示下载进度框
     */
    public String showDownloadDialog(String paramStr) {
        UpdateParamModel updateParamModel = gson.fromJson(paramStr, UpdateParamModel.class);
        CheckVersionDialog.downloadFile(updateParamModel,updateParamModel.isForceUpdate());
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 静默下载apk文件
     */
    public String downloadFileBackground(String paramStr) {
        UpdateParamModel updateParamModel = gson.fromJson(paramStr, UpdateParamModel.class);
        CheckVersionDialog.downloadFileBackground(updateParamModel);
        return gson.toJson(ResultModel.success(""));
    }


}
