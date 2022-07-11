package com.tyky.baiduface;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.baidu.idl.face.example.HomeActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.model.ParamModel;
import com.tyky.webviewBase.model.ResultModel;

@WebViewInterface("baiduface")
public class BaiduFaceJsInterface {
    Gson gson = GsonUtils.getGson();


    /**
     * 保存数据
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String facialRecognite(String paramStr) {
        Log.d("test", "进入");
        ToastUtils.showShort("进入yy");
        ParamModel paramModel = gson.fromJson(paramStr, ParamModel.class);
        ActivityUtils.startActivity(HomeActivity.class);
        return gson.toJson(ResultModel.success(""));
    }


}
