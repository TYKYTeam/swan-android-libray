package com.tyky.logupload.activity;

import android.os.Bundle;

import com.blankj.utilcode.util.AppUtils;
import com.tyky.logupload.CrashUploader;
import com.tyky.logupload.R;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorTipActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_tip);

        String path = getIntent().getStringExtra(CrashActivity.CRASH_FILE_PATH);

        //调用上传的日志接口
        CrashUploader.uploadLog(path, new CrashUploader.UploadListener() {
            @Override
            public void onUploadSuccess(File file) {
                restartApp();
            }

            @Override
            public void onUploadError(File file) {
                restartApp();
            }
        });

    }

    private void restartApp() {
        findViewById(R.id.ivTip).postDelayed(() -> AppUtils.relaunchApp(false), 2000);
    }
}