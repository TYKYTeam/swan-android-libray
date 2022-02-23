package com.tyky.debugger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tyky.media.activity.QrScanActivity;
import com.tyky.webviewBase.constants.MediaModuleConstants;
import com.tyky.webviewBase.event.UrlLoadEvent;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    EditText mEturl;
    Button mBtnscan;
    Button mBtnvisit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mEturl = findViewById(R.id.etUrl);
        mBtnscan = findViewById(R.id.btnScan);
        mBtnvisit = findViewById(R.id.btnVisit);

        mBtnscan.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), QrScanActivity.class);
            startActivityForResult(intent, MediaModuleConstants.REQUEST_QR_SCAN_CODE);
        });

        mBtnvisit.setOnClickListener(view -> {
            saveAndVisit();
        });

        String settingUrl = SPUtils.getInstance().getString("settingUrl", "");
        mEturl.setText(settingUrl);

    }

    private void saveAndVisit() {
        String url = mEturl.getText().toString();
        SPUtils.getInstance().put("settingUrl", url, true);
        EventBus.getDefault().post(new UrlLoadEvent(url));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MediaModuleConstants.REQUEST_QR_SCAN_CODE && resultCode == MediaModuleConstants.RESULT_QR_SCAN_CODE) {
            String stringExtra = data.getStringExtra(MediaModuleConstants.REQUEST_QR_SCAN_RESULT);
            if (!StringUtils.isEmpty(stringExtra)) {
                mEturl.setText(stringExtra);
                saveAndVisit();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
