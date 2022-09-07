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

        //全量更新
        if (forceUpdate) {
            //是强制更新
            CustomDialog customDialog = CustomDialog.build()
                    .setCustomView(new OnBindView<CustomDialog>(R.layout.layout_dialog_update) {
                        @Override
                        public void onBind(final CustomDialog dialog, View v) {
                            TextView tvUpdateContent = v.findViewById(R.id.tvUpdateContent);
                            TextView tvVersion = v.findViewById(R.id.tvVersion);
                            TextView tvFileSize = v.findViewById(R.id.tvFileSize);
                            Button btnConfirm = v.findViewById(R.id.btnConfirm);
                            Button btnCancel = v.findViewById(R.id.btnCancel);
                            //隐藏取消按钮
                            btnCancel.setVisibility(View.GONE);
                            tvUpdateContent.setText(updateContent);
                            tvVersion.setText(paramModel.getVersionName());
                            tvFileSize.setText(paramModel.getFileSize());
                            btnCancel.setOnClickListener(v1 -> dialog.dismiss());
                            btnConfirm.setOnClickListener(v12 -> {
                                dialog.dismiss();
                                downloadFile(paramModel, true);
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
                            TextView tvFileSize = v.findViewById(R.id.tvFileSize);
                            tvFileSize.setText(paramModel.getFileSize());
                            tvUpdateContent.setText(updateContent);
                            tvVersion.setText(paramModel.getVersionName());
                            btnCancel.setOnClickListener(v1 -> {
                                dialog.dismiss();
                                //不是强制更新，点了取消后在wifi环境自动下载apk文件
                                downloadFileBackground(paramModel);
                            });
                            btnConfirm.setOnClickListener(v12 -> {
                                dialog.dismiss();
                                downloadFile(paramModel, false);
                            });
                        }
                    })
                    .setAlign(CustomDialog.ALIGN.CENTER)
                    .setCancelable(false)
                    .setMaskColor(Color.parseColor("#80666666"))
                    .show();
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

    /**
     * @param updateParamModel
     * @param isForce          是否为强制更新
     */
    public static void downloadFile(UpdateParamModel updateParamModel, boolean isForce) {
        String downloadUrl = updateParamModel.getDownloadUrl();
        KLog.d("文件下载更新地址："+downloadUrl);
        int versionCode = updateParamModel.getVersionCode();

        File file = new File(PathUtils.getExternalAppFilesPath(), "temp_" + versionCode + ".apk");

        CustomDialog customDialog = CustomDialog.build()
                .setCustomView(new OnBindView<CustomDialog>(R.layout.layout_dialog_download) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {

                        CircleProgressBar circleProgressBar = v.findViewById(R.id.circleProgressBar);
                        Button btnCancel = v.findViewById(R.id.btnCancelDownload);
                        if (isForce) {
                            v.setVisibility(View.GONE);
                        } else {
                            v.setVisibility(View.VISIBLE);
                        }
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
                                try {
                                    feature.cancel(true);
                                } catch (IllegalStateException e) {
                                    KLog.e(e.getMessage());
                                }
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
