package com.tyky.webviewBase.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.socks.library.KLog;
import com.tyky.webviewBase.R;

import org.apache.commons.lang3.StringUtils;

public class WebviewDownloader implements DownloadListener {
    private Context mContext;

    public WebviewDownloader(Context mContext) {
        this.mContext = mContext;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

        final String fileName = StringUtils.substringBetween(contentDisposition, "\"");
        //字节转为具体大小
        final String fileSize = contentLength / 1024 + "KB";
        //扩展名
        String extendName = "";
        if (fileName != null && fileName.contains(".")) {
            extendName = StringUtils.substringAfter(fileName, ".");
        }
        String finalExtendName = extendName;

        String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extendName);

        CustomDialog.show(new OnBindView<CustomDialog>(R.layout.dialog_downloader) {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                TextView tvFileSize = v.findViewById(R.id.tvFileSize);
                tvFileSize.setText(fileSize);

                TextView tvExtendName = v.findViewById(R.id.tvExtendName);
                tvExtendName.setText(finalExtendName);

                TextView tvFileName = v.findViewById(R.id.tvFileName);
                if (StringUtils.isNotBlank(fileName)) {
                    tvFileName.setText(fileName);
                } else {
                    tvFileName.setText("未知");
                }

                v.findViewById(R.id.btnDownload).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadBySystem(url, fileName, mimeTypeFromExtension);
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        }).setAlign(CustomDialog.ALIGN.BOTTOM).setMaskColor(Color.parseColor("#4D000000"));
    }


    /**
     * 使用系统的下载服务
     *
     * @param url
     * @param fileName 文件名
     * @param mimeType
     */
    private void downloadBySystem(String url, String fileName, String mimeType) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
//        request.setTitle("This is title");
        // 设置通知栏的描述
//        request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(false);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        request.setMimeType(mimeType);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Activity.DOWNLOAD_SERVICE);
        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
        KLog.d("下载任务" + downloadId);
    }
}
