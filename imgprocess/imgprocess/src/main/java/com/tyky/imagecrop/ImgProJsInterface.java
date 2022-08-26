package com.tyky.imagecrop;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.tyky.imagecrop.camera.TestActivity;
import com.tyky.webviewBase.annotation.WebViewInterface;

@WebViewInterface("imgprocess")
public class ImgProJsInterface {

    @JavascriptInterface
    public String setImgProcess(){
        Log.d("MyTest","setImgProcess");
        ActivityUtils.startActivity(TestActivity.class);

        return "执行一次";

    }


}
