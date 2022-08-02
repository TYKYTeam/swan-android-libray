package com.tyky.auth;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.socks.library.KLog;
import com.tyky.auth.bean.ColorParamModel;
import com.tyky.auth.view.GestureUnlockView;

import androidx.appcompat.app.AppCompatActivity;

public class UnLockActivity extends AppCompatActivity {

    private TextView tvTip;
    private TextView tvTitle;
    private GestureUnlockView gestureUnlockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        tvTip = findViewById(R.id.tvTip);
        tvTitle = findViewById(R.id.tvTitle);

        gestureUnlockView = findViewById(R.id.gestureUnlockView);
        GestureUnlockView.OnVertifyListener listener = new GestureUnlockView.OnVertifyListener() {

            @Override
            public void onUnlockSuccess() {
                ToastUtils.showShort("解锁成功");
                finish();
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
            public void onSetSuccess(String pwd) {
                //ToastUtils.showShort("手势设置成功,密码为" + pwd);
                ToastUtils.showShort("手势设置成功");
                SPUtils.getInstance().put("lockPwd", pwd);
                finish();
            }

            @Override
            public void onError(String error) {
                KLog.d(error);
                showError(error);
            }
        };
        int type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            //解锁模式
            String lockPwd = SPUtils.getInstance().getString("lockPwd");
            gestureUnlockView.setUnlockMode(lockPwd);
            tvTitle.setText("手势解锁");
            showTip("绘制图案进行手势解锁");
        } else {
            //设置手势密码模式
            gestureUnlockView.setConfigLockMode();
            tvTitle.setText("设置手势密码");
        }

        initColor(getIntent().getStringExtra("colorParam"));
        gestureUnlockView.setOnVertifyListener(listener);


    }

    private void initColor(String colorParam) {
        ColorParamModel colorParamModel = GsonUtils.getGson().fromJson(colorParam, ColorParamModel.class);
        String circleColor = colorParamModel.getCircleColor();
        if (TextUtils.isEmpty(circleColor)) {
            circleColor = "grey";
        }
        Integer circleWidth = colorParamModel.getCircleWidth();
        if (circleWidth == null || circleWidth <=0) {
            circleWidth = 5;
        }

        String insideCircleColor = colorParamModel.getInsideCircleColor();
        if (TextUtils.isEmpty(insideCircleColor)) {
            insideCircleColor = "blue";
        }
        Integer insideCircleWidth = colorParamModel.getInsideCircleWidth();
        if (insideCircleWidth == null || insideCircleWidth <=0) {
            insideCircleWidth = 5;
        }

        String errorColor = colorParamModel.getErrorColor();
        if (TextUtils.isEmpty(errorColor)) {
            errorColor = "red";
        }
        Integer errorWidth = colorParamModel.getErrorWidth();
        if (errorWidth == null || errorWidth <=0) {
            errorWidth = 5;
        }

        gestureUnlockView.setErrorPaint(errorColor,errorWidth);
        gestureUnlockView.setCircle(circleColor,circleWidth);
        gestureUnlockView.setInsideCircle(insideCircleColor,insideCircleWidth);

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