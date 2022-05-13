package com.tyky.acl;

import android.os.Bundle;

import com.tyky.webviewBase.activity.CustomWebViewActivity;

public class MainActivity extends CustomWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUrl("/index_old.html");
        //loadUrl("http://101.43.17.174:10003/#/");
        //loadUrl("https://www.bingtuanbdc.com.cn/mobile-jsbt/#/home");


        /*showTestBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = BadgeUtils.setCount(MainActivity.this, 0);
                Toast.makeText(MainActivity.this, "" + b, Toast.LENGTH_SHORT).show();

            }
        });*/
    }

}