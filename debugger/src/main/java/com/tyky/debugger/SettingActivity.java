package com.tyky.debugger;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.MetaDataUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tyky.debugger.adapter.RvHistoryInfoAdapter;
import com.tyky.debugger.adapter.RvModuleAdapter;
import com.tyky.debugger.bean.HistoryUrlInfo;
import com.tyky.media.activity.QrScanActivity;
import com.tyky.webviewBase.constants.MediaModuleConstants;
import com.tyky.webviewBase.event.UrlLoadEvent;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SettingActivity extends AppCompatActivity {

    EditText mEturl;
    ImageView mBtnscan;
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


        int color = Color.parseColor("white");
        BarUtils.setStatusBarLightMode(this, ColorUtils.isLightColor(color));
        BarUtils.setStatusBarColor(this, color, true);

        LitePal.initialize(this);
        showHistory();
    }

    private void showHistory() {
        List<HistoryUrlInfo> all = LitePal.findAll(HistoryUrlInfo.class);
        View tvNodata = findViewById(R.id.tvNodata);
        RvHistoryInfoAdapter rvHistoryInfoAdapter = new RvHistoryInfoAdapter(all,tvNodata,this);
        RecyclerView rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvHistory.setLayoutManager(linearLayoutManager);
        rvHistory.setAdapter(rvHistoryInfoAdapter);
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
        RecyclerView rvModule = findViewById(R.id.rvModule);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        rvModule.setLayoutManager(gridLayoutManager);

        if (!TextUtils.isEmpty(baseLibraryDependency)) {
            String[] split = baseLibraryDependency.split(",");
            rvModule.setAdapter(new RvModuleAdapter(split));
            tvModuleTip.setText("当前所含模块（" + split.length + ")");
        } else {
            tvModuleTip.setText("当前所含模块（0）");
        }
    }

    private void saveAndVisit() {
        String url = mEturl.getText().toString();
        SPUtils.getInstance().put("settingUrl", url, true);
        //初始化数据库
        SQLiteDatabase db = LitePal.getDatabase();

        List<HistoryUrlInfo> historyUrlInfos = LitePal.where("url =?", url).find(HistoryUrlInfo.class);
        if (historyUrlInfos.isEmpty()) {
            HistoryUrlInfo historyUrlInfo = new HistoryUrlInfo(url);
            historyUrlInfo.save();
        }
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
