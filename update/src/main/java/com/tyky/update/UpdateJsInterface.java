package com.tyky.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
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
     * 检测更新
     *
     * @return
     */
    @JavascriptInterface
    public String checkVersion(String paramStr) {
        UpdateParamModel updateParamModel = gson.fromJson(paramStr, UpdateParamModel.class);
        CheckVersionDialog.show(updateParamModel);
        return gson.toJson(ResultModel.success(""));
    }

    private void showDownloadDialog() {
        Activity topActivity = ActivityUtils.getTopActivity();

        ProgressDialog.show(topActivity, "版本下载", "下载中");
        /*new AlertDialog.Builder(topActivity).setTitle("新版本更新")
                .setMessage("更新内容：\n1.内容111\n2.内容222")
                .setPositiveButton("确定升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
    }
}
