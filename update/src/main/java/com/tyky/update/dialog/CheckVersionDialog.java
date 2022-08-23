package com.tyky.update.dialog;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.socks.library.KLog;
import com.tyky.update.R;
import com.tyky.update.bean.UpdateParamModel;
import com.tyky.update.utils.FileDownloadUtil;
import com.tyky.update.view.CircleProgressBar;

import java.io.File;
import java.util.concurrent.Future;

public class CheckVersionDialog {

    public static void show(UpdateParamModel paramModel) {

        boolean forceUpdate = paramModel.isForceUpdate();
        String updateContent = paramModel.getUpdateContent();
        int type = 0;
        if (type == 1) {
            //热更新经过讨论不实现！！ 2022年8月15日14:17:47
            //热更新，直接静默下载，不给提示
            //downloadFile(paramModel);
        } else {
            //全量更新
            if (forceUpdate) {
                //是强制更新
                CustomDialog customDialog = CustomDialog.build()
                        .setCustomView(new OnBindView<CustomDialog>(R.layout.layout_dialog_update) {
                            @Override
                            public void onBind(final CustomDialog dialog, View v) {
                                TextView tvUpdateContent = v.findViewById(R.id.tvUpdateContent);
                                TextView tvVersion = v.findViewById(R.id.tvVersion);
                                Button btnConfirm = v.findViewById(R.id.btnConfirm);
                                Button btnCancel = v.findViewById(R.id.btnCancel);
                                //隐藏取消按钮
                                btnCancel.setVisibility(View.GONE);
                                tvUpdateContent.setText(updateContent);
                                tvVersion.setText(paramModel.getVersionName());
                                btnCancel.setOnClickListener(v1 -> dialog.dismiss());
                                btnConfirm.setOnClickListener(v12 -> {
                                    dialog.dismiss();
                                    downloadFile(paramModel);
                                });
                            }
                        })
                        .setAlign(CustomDialog.ALIGN.CENTER)
                        .setCancelable(false)
                        .setMaskColor(Color.parseColor("#80666666"));

                customDialog.show();
            } else {
                CustomDialog.build()
                        .setCustomView(new OnBindView<CustomDialog>(R.layout.layout_dialog_update) {
                            @Override
                            public void onBind(final CustomDialog dialog, View v) {
                                TextView tvUpdateContent = v.findViewById(R.id.tvUpdateContent);
                                TextView tvVersion = v.findViewById(R.id.tvVersion);
                                Button btnConfirm = v.findViewById(R.id.btnConfirm);
                                Button btnCancel = v.findViewById(R.id.btnCancel);

                                tvUpdateContent.setText(updateContent);
                                tvVersion.setText(paramModel.getVersionName());
                                btnCancel.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                    //不是强制更新，点了取消后在wifi环境自动下载apk文件
                                    downloadFileBackground(paramModel);
                                });
                                btnConfirm.setOnClickListener(v12 -> {
                                    dialog.dismiss();
                                    downloadFile(paramModel);
                                });
                            }
                        })
                        .setAlign(CustomDialog.ALIGN.CENTER)
                        .setCancelable(false)
                        .setMaskColor(Color.parseColor("#80666666"))
                        .show();
            }

        }


    }

    /**
     * wifi情况下，后台静默下载
     */
    public static void downloadFileBackground(UpdateParamModel updateParamModel) {
        //如果是处于wifi情况，后台下载apk文件
        if (NetworkUtils.isWifiConnected()) {
            String downloadUrl = updateParamModel.getDownloadUrl();
            int versionCode = updateParamModel.getVersionCode();

            File file = new File(PathUtils.getExternalAppFilesPath(), "temp_" + versionCode + ".apk");

            FileDownloadUtil.download(downloadUrl, file, new FileDownloadUtil.OnDownloadListener() {
                @Override
                public void onProgress(double progress) {
                    KLog.d("下载进度: " + progress);
                }

                @Override
                public void onError(Exception e) {
                    KLog.e("下载错误: " + e.getMessage());
                }

                @Override
                public void onSuccess(File outputFile) {
                    KLog.d("下载成功");
                }
            });


        }
    }

    public static void downloadFile(UpdateParamModel updateParamModel) {
        String downloadUrl = updateParamModel.getDownloadUrl();
        int versionCode = updateParamModel.getVersionCode();
        int type = 0;

        if (type == 1) {
            //下载h5资源包
            /*File file = new File(PathUtils.getExternalAppFilesPath(), "temp_" + h5VersionCode + ".zip");
            HttpRequest.DOWNLOAD(
                    ActivityUtils.getTopActivity(), downloadUrl,
                    file,
                    new OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            if (file.length() > 0) {
                                // 解压操作
                                File dir = new File(PathUtils.getExternalAppFilesPath(), "h5Assets/" + versionCode + "/" + h5VersionCode);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }

                                try {
                                    ZipUtils.unzipFile(file, dir);

                                    boolean needTip = updateParamModel.isNeedTip();
                                    if (needTip) {
                                        MessageDialog.show("提示", "请点击确定，重启APP完成更新", "确定").setOkButtonClickListener(new OnDialogButtonClickListener<MessageDialog>() {
                                            @Override
                                            public boolean onClick(MessageDialog baseDialog, View v) {
                                                AppUtils.relaunchApp(true);
                                                return false;
                                            }
                                        });
                                    }

                                } catch (IOException e) {
                                    KLog.e("解压失败" + e.getMessage());
                                }

                            } else {
                                ToastUtils.showShort("apk文件大小为0KB，更新失败！");
                            }
                        }

                        @Override
                        public void onDownloading(int progress) {
                            KLog.d("下载进度：" + progress);
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            ToastUtils.showShort("下载失败，原因：" + e.getMessage());
                        }
                    }
            );*/

        } else {
            File file = new File(PathUtils.getExternalAppFilesPath(), "temp_" + versionCode + ".apk");

            CustomDialog customDialog = CustomDialog.build()
                    .setCustomView(new OnBindView<CustomDialog>(R.layout.layout_dialog_download) {
                        @Override
                        public void onBind(final CustomDialog dialog, View v) {

                            CircleProgressBar circleProgressBar = v.findViewById(R.id.circleProgressBar);
                            Button btnCancel = v.findViewById(R.id.btnCancelDownload);
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }

                            });

                            Future<?> feature = FileDownloadUtil.download(downloadUrl, file, new FileDownloadUtil.OnDownloadListener() {
                                @Override
                                public void onProgress(double progress) {
                                    KLog.d("下载进度: " + progress);
                                    ThreadUtils.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int progressInt = Double.valueOf(progress * 100).intValue();
                                            circleProgressBar.setProgress(progressInt);
                                        }
                                    });
                                }

                                @Override
                                public void onError(Exception e) {
                                    KLog.e("下载错误: " + e.getMessage());
                                    ThreadUtils.runOnUiThread(() -> {
                                        dialog.dismiss();
                                        ToastUtils.showShort("下载失败,原因为" + e.getMessage());
                                    });
                                }

                                @Override
                                public void onSuccess(File outputFile) {
                                    ThreadUtils.runOnUiThread(() -> {
                                        dialog.dismiss();
                                        AppUtils.installApp(outputFile);
                                    });
                                }
                            });

                            dialog.setDialogLifecycleCallback(new DialogLifecycleCallback<CustomDialog>() {
                                @Override
                                public void onDismiss(CustomDialog dialog) {
                                    super.onDismiss(dialog);
                                    feature.cancel(true);
                                }
                            });
                        }
                    })
                    .setAlign(CustomDialog.ALIGN.CENTER)
                    .setCancelable(false)
                    .setMaskColor(Color.parseColor("#80666666"))
                    .setDialogLifecycleCallback(new DialogLifecycleCallback<CustomDialog>() {
                        @Override
                        public void onShow(CustomDialog dialog) {
                            super.onShow(dialog);

                        }

                        @Override
                        public void onDismiss(CustomDialog dialog) {
                            super.onDismiss(dialog);
                        }

                    });

            customDialog.show();
        }
    }
}
