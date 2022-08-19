package com.tyky.acl;

import android.os.Bundle;

import com.tyky.webviewBase.activity.CustomWebViewActivity;

public class MainActivity extends CustomWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUrl("/index_old.html");

        //loadUrl("http://183.62.130.45:35603/mobile/cli/");
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