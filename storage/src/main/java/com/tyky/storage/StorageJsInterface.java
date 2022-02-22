package com.tyky.storage;

import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("storage")
public class StorageJsInterface {
    Gson gson = GsonUtils.getGson();


    /**
     * 保存数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String save(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String key = paramModel.getKey();
        String value = paramModel.getValue();

        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return gson.toJson(ResultModel.errorParam());
        }
        SPUtils instance = SPUtils.getInstance();
        instance.put(key,value,true);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 获取数据
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String get(String paramStr) {
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        String key = paramModel.getKey();
        if (StringUtils.isEmpty(key)) {
            return gson.toJson(ResultModel.errorParam());
        }
        String result = SPUtils.getInstance().getString(key, "");
        return gson.toJson(ResultModel.success(result));
    }
}
