package com.tyky.acl;

import android.os.Bundle;

import com.tyky.webviewBase.activity.CustomWebViewActivity;

public class MainActivity extends CustomWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocalUrl("/index.html");
        //loadWebUrl("http://baidu.com");
    }


}