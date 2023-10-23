package com.tyky.acl;

import android.os.Bundle;

import com.tyky.webviewBase.activity.CustomWebViewActivity;

public class MainActivity extends CustomWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUrl("/index_old.html");

        // 武汉硚口项目测试环境地址，账号：admin   密码：Fgj123456
//        loadUrl("http://wuhantaiji.tpddns.cn:8009/pad/#/");

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