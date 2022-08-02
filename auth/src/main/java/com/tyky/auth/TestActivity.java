package com.tyky.auth;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.socks.library.KLog;
import com.tyky.auth.view.GestureUnlockView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private TextView tvTip;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tvTip = findViewById(R.id.tvTip);
        tvTitle = findViewById(R.id.tvTitle);

        GestureUnlockView gestureUnlockView = findViewById(R.id.gestureUnlockView);

        int type = getIntent().getIntExtra("type", 2);
        if (type == 1) {
            //解锁模式
        } else {
            //设置手势密码模式
            gestureUnlockView.setOnVertifyListener(new GestureUnlockView.OnVertifyListener() {

                @Override
                public void onUnlockSuccess() {
                    //finish();
                }

                @Override
                public void onUnlockError() {
                    showError("手势错误，请重试");
                }

                @Override
                public void onConfirmSuccess(String msg) {
                    showTip(msg);
                }

                @Override
                public void onSetSuccess() {
                    ToastUtils.showShort("手势设置成功");
                    //finish();
                }

                @Override
                public void onError(String error) {
                    KLog.d(error);
                    showError(error);
                }
            });
        }
    }

    private void showTip(String msg) {
        runOnUiThread(() -> {
            tvTip.setText(msg);
            tvTip.setTextColor(Color.GRAY);
        });
    }

    private void showError(String msg) {
        runOnUiThread(() -> {
            tvTip.setText(msg);
            tvTip.setTextColor(Color.RED);
        });

    }
}