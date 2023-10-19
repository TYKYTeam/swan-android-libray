package com.tyky.acl;

import android.os.Bundle;

import com.tyky.webviewBase.activity.CustomWebViewActivity;

public class MainActivity extends CustomWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUrl("/index_old.html");
//        loadUrl("http://10.72.0.59:8088/pad/index.html");

        //loadUrl("http://wuhantaiji.tpddns.cn:8056/static/app/test06.html?r=212");
//        loadUrl("http://ark.tyky.com.cn:35603/mobile/native/#/");
        //loadUrl("http://183.62.130.45:35603/mobile/swan-ui/#/");
        //loadUrl("https://www.bingtuanbdc.com.cn/mobile-jsbt/#/home");

        /*showTestBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScreenUtils.setPortrait(MainActivity.this);
            }
        });*/
    }

}