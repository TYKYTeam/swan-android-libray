package com.tyky.acl;

import android.os.Bundle;
import android.view.View;

import com.tyky.tim.TIMLoginUtils;
import com.tyky.webviewBase.activity.CustomWebViewActivity;

public class MainActivity extends CustomWebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUrl("/index.html");
        //loadWebUrl("http://baidu.com");


        showTestBtn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TIMLoginUtils.login("5913635", "",true);
            }
        });
    }

}