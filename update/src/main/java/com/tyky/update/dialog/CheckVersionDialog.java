package com.tyky.update.dialog;

import android.app.ProgressDialog;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kongzue.baseokhttp.HttpRequest;
import com.kongzue.baseokhttp.listener.OnDownloadListener;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.socks.library.KLog;
import com.tyky.update.bean.UpdateParamModel;

import java.io.File;

public class CheckVersionDialog {

    public static void show(UpdateParamModel paramModel) {

        //获取当前APP版本号和h5版本号
        int currentVersionCode = AppUtils.getAppVersionCode();

        int versionCode = paramModel.getVersionCode();
        int h5VersionCode = paramModel.getH5VersionCode();
        int type = paramModel.getType();
        boolean forceUpdate = paramModel.isForceUpdate();
        boolean needTip = paramModel.isNeedTip();
        String updateContent = paramModel.getUpdateContent();
        String downloadUrl = paramModel.getDownloadUrl();

        /*if (currentVersionCode < versionCode) {
            //需要更新

        }*/

        if (type == 1) {
            //热更新
        } else {


            if (forceUpdate) {
                //是强制更新
                //全量更新
                MessageDialog.show("新版本更新", updateContent, "确定升级")
                        .setOkButtonClickListener(new OnDialogButtonClickListener<MessageDialog>() {
                            @Override
                            public boolean onClick(MessageDialog baseDialog, View v) {
                                downloadFile(paramModel);
                                return false;
                            }
                        });
            } else {
                MessageDialog.show("新版本更新", updateContent, "确定升级", "暂不升级")
                        .setOkButtonClickListener(new OnDialogButtonClickListener<MessageDialog>() {
                            @Override
                            public boolean onClick(MessageDialog baseDialog, View v) {
                                downloadFile(paramModel);
                                return false;
                            }
                        })
                        .setCancelButtonClickListener(new OnDialogButtonClickListener<MessageDialog>() {
                            @Override
                            public boolean onClick(MessageDialog baseDialog, View v) {
                                return false;
                            }
                        });
            }

        }


    }

    public static void downloadFile(UpdateParamModel updateParamModel) {
        String downloadUrl = updateParamModel.getDownloadUrl();
        int type = updateParamModel.getType();
        int versionCode = updateParamModel.getVersionCode();
        int h5VersionCode = updateParamModel.getH5VersionCode();

        if (type == 1) {
            //todo 下载h5资源包
        } else {
            File file = new File(PathUtils.getExternalAppFilesPath(), "temp_" + versionCode + ".apk");

            ProgressDialog progressDialog = new ProgressDialog(ActivityUtils.getTopActivity());
            progressDialog.setTitle("更新版本");
            progressDialog.setMessage("正在下载中");
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();

            HttpRequest.DOWNLOAD(
                    ActivityUtils.getTopActivity(), downloadUrl,
                    file,
                    new OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            progressDialog.dismiss();
                            if (file.length() > 0) {
                                //下载完毕后安装apk文件
                                AppUtils.installApp(file);
                            } else {
                                ToastUtils.showShort("apk文件大小为0KB，更新失败！");
                            }
                        }

                        @Override
                        public void onDownloading(int progress) {
                            KLog.d("下载进度：" + progress);
                            //更新进度
                            progressDialog.setProgress(progress);
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            ToastUtils.showShort("下载失败，原因：" + e.getMessage());
                        }
                    }
            );
        }
    }
}
