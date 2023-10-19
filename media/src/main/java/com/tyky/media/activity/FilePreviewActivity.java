package com.tyky.media.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.socks.library.KLog;
import com.tyky.media.R;
import com.tyky.media.bean.DownloadInfo;
import com.tyky.media.bean.DownloadResult;
import com.tyky.media.utils.FileDownloadUtil;
import com.tyky.media.utils.FileUtils;
import com.tyky.media.utils.PdfUtils;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

/**
 * 文件预览
 */
public class FilePreviewActivity extends AppCompatActivity {
    public static final String FILE_INFO = "fileInfo";
    private String fileUrl;
    private String fileName;
    private PDFView pdfView;
    private View imageBack;
    private View llOpenSuccess;
    private View llOpenFail;
    private View openByOther;
    private View retryBtn;
    private FileDownloadUtil.OnDownloadListener downloadListener;
    private PdfUtils.FileToPdfListener fileToPdfListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_preview);
        initData();
        initView();
        initListener();
        initPdfFile();
    }

    private void initData() {
        DownloadInfo downloadInfo = (DownloadInfo) getIntent().getSerializableExtra(FILE_INFO);
        fileUrl = downloadInfo.getUrl();
        fileName = downloadInfo.getName();
        KLog.d(fileUrl + "    " + fileName);
    }

    private void initView() {
        pdfView = findViewById(R.id.pdfView);
        imageBack = findViewById(R.id.imageBack);
        llOpenSuccess = findViewById(R.id.llOpenSuccess);
        llOpenFail = findViewById(R.id.llOpenFail);
        openByOther = findViewById(R.id.openByOther);
        // 暂时先不用
        openByOther.setVisibility(View.GONE);
        retryBtn = findViewById(R.id.retryBtn);
    }

    /**
     * 文件下载回调
     */
    private void initListener() {
        downloadListener = new FileDownloadUtil.OnDownloadListener() {
            @Override
            public void onProgress(double progress) {

            }

            @Override
            public void onError(String fileName, Exception e) {
                KLog.d("文件下载失败");
                WaitDialog.dismiss();
                ToastUtils.showLong("文件下载失败");
                updateDisplayView(false);
            }

            @Override
            public void onSuccess(File outputFile) {
                KLog.d("文件下载成功");
                if (FilePreviewActivity.this.isFinishing() || FilePreviewActivity.this.isDestroyed()) {
                    return;
                }
                // 展示pdf文件
                previewToPdf(outputFile);
            }
        };

        fileToPdfListener = new PdfUtils.FileToPdfListener() {
            @Override
            public void onTransformFail() {
                WaitDialog.dismiss();
                ToastUtils.showLong("源文件转pdf失败");
                updateDisplayView(false);
            }

            @Override
            public void onTransformSuccess(File targetFile) {
                KLog.d("文件转换pdf成功");
                // 展示pdf文件
                previewFile(targetFile);
            }
        };

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.dismiss();
                finish();
            }
        });

//        openByOther.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenFileUtil.openFile(FilePreviewActivity.this , UrlUtils.parseUrlFileName(fileUrl));
//            }
//        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPdfFile();
            }
        });
    }

    private void updateDisplayView(boolean isOpen) {
        if (isOpen) {
            llOpenSuccess.setVisibility(View.VISIBLE);
            llOpenFail.setVisibility(View.GONE);
            return;
        }
        llOpenFail.setVisibility(View.VISIBLE);
        llOpenSuccess.setVisibility(View.GONE);
    }


    // 加载pdf文件
    private void initPdfFile() {
        WaitDialog.show("加载中");

        // 获取本地 sdcard/Android/data/com.tyky.acl/files/ 下对应文件
        File file = FileUtils.getFile(fileName);

        // 判断本地是否存在源文件，本地没有文件，下载文件
        if (!file.exists()) {
            downloadFile();
            return;
        }

        previewToPdf(file);
    }

    // 统一转换为pdf
    private void previewToPdf(File file) {
        // 本地存在文件，源文件为pdf文件直接展示
        if (file.getName().endsWith("pdf")) {
            // 展示pdf文件
            previewFile(file);
            return;
        }

        // 将源文件转换为pdf文件，生成pdf文件
        PdfUtils.getInstance().toPdf(file, fileToPdfListener);
    }

    // 下载文件
    private void downloadFile() {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setUrl(fileUrl);
        downloadInfo.setName(fileName);
        downloadInfo.setListener(downloadListener);
        ExecutorService ioPool = ThreadUtils.getIoPool();
        ExecutorCompletionService<DownloadResult> completionService = new ExecutorCompletionService<>(ioPool);
        FileDownloadUtil.getInstance().download(completionService, downloadInfo);
    }


    // 展示pdf文件
    private void previewFile(File pdfFile) {
        if (!pdfFile.exists()) {
            ToastUtils.showLong("pdf预览文件不存在");
            return;
        }
        pdfView.fromFile(pdfFile)   //从链接加载pdf文件预览
                .defaultPage(0)
                .swipeHorizontal(false)
                .enableSwipe(false)
                .enableDoubletap(true)
                .enableAnnotationRendering(true)
                //.scrollHandle(new DefaultScrollHandle(PdfShowActivity.this))
                .spacing(10) // in dp
                // 横屏时也可以自适应宽度
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        pdfView.fitToWidth();
                    }
                })
                .onLoad(nbPages -> {
                    WaitDialog.dismiss();
                    updateDisplayView(true);
                })
                .onError(error -> {
                    ToastUtils.showLong("文件预览失败");
                })
                .load();
        pdfView.fitToWidth();
        pdfView.zoomWithAnimation(1f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}