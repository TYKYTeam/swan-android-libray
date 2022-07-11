package com.baidu.idl.face.example;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baidu.idl.face.platform.ui.BaseActivity;
import com.tyky.baiduface.R;

/**
 * 人脸采集协议页面
 */
public class FaceHomeAgreementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_home_agreement);
        initView();
    }

    private void initView() {
        ImageView agreementReturn = (ImageView) findViewById(R.id.agreement_return);
        agreementReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
