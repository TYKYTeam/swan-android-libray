package com.tyky.debugger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.MetaDataUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.nex3z.flowlayout.FlowLayout;
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

        showCurrentBaseLibraryInfo();
    }

    /**
     * 显示基座版本和所含依赖模块信息
     */
    private void showCurrentBaseLibraryInfo() {
        //获取当前依赖模块（在app里的AndroidManifest中定义）
        String baseLibraryDependency = MetaDataUtils.getMetaDataInApp("base_library_dependency");
        //获取当前依赖版本（在app里的AndroidManifest中定义）
        String baseLibraryVersion = MetaDataUtils.getMetaDataInApp("base_library_version");
        TextView tvLibraryVersion = (TextView) findViewById(R.id.tvLibraryVersion);
        tvLibraryVersion.setText(baseLibraryVersion);

        TextView tvModuleTip = (TextView) findViewById(R.id.tvModuleTip);
        //动态假如itemView
        FlowLayout flowLayout = findViewById(R.id.flowLayout);

        if (!TextUtils.isEmpty(baseLibraryDependency)) {
            String[] split = baseLibraryDependency.split(",");
            tvModuleTip.setText("当前所含模块（" + split.length + "个)：");
            for (String module : split) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_layout, null);
                TextView tvModule = (TextView) view.findViewById(R.id.tvModule);
                tvModule.setText(module + "模块");
                flowLayout.addView(view);
            }
        } else {
            tvModuleTip.setText("当前所含模块（0个）");
        }
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
