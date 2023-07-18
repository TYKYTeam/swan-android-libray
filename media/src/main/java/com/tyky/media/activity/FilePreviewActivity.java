package com.tyky.media.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.socks.library.KLog;
import com.tyky.media.R;
import com.tyky.media.bean.DownloadInfo;
import com.tyky.media.bean.DownloadResult;
import com.tyky.media.utils.FileDownloadUtil;
import com.tyky.media.utils.FileUtils;
import com.tyky.media.utils.PdfUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 文件预览
 */
public class FilePreviewActivity extends AppCompatActivity {
    public static final String FILE_URL = "filePath";
    private String fileUrl;
    private PDFView pdfView;
    private View imageBack;
    private View llOpenSuccess;
    private View llOpenFail;
    private View openByOther;
    private View retryBtn;
    private FileDownloadUtil.OnDownloadListener downloadListener;

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
        fileUrl = getIntent().getStringExtra(FILE_URL);
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

    private void initListener() {
        downloadListener = new FileDownloadUtil.OnDownloadListener() {
            @Override
            public void onProgress(double progress) {

            }

            @Override
            public void onError(String fileName, Exception e) {
                KLog.d("文件下载失败");
            }

            @Override
            public void onSuccess(File outputFile) {
                KLog.d("文件下载成功");
            }
        };

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        // 从url中获取文件名称
        String fileName = FileUtils.parseUrlFileName(fileUrl);
        // 获取本地 sdcard/Android/data/com.tyky.acl/files/ 下对应文件
        File file = FileUtils.getFile(fileName);

        // 源文件为pdf文件直接展示
        if (fileName.endsWith("pdf") && file.exists()) {
            // 展示pdf文件
            previewFile(file);
            return;
        }

        // 判断本地是否存在源文件，本地没有文件，下载文件
        if (!file.exists() && !downloadFile()) {
            return;
        }

        // 源文件为pdf文件直接展示
        if (fileName.endsWith("pdf")) {
            // 展示pdf文件
            previewFile(file);
            return;
        }

        // 源文件非pdf文件 需要转换为pdf文件
        String pdfName = FileUtils.getTransformFileName(file, "pdf");
        File pdfFile = FileUtils.getFile(pdfName);

        // 将源文件转换为pdf文件，生成pdf文件
        File toPdfFile = PdfUtils.getInstance().toPdf(file);

        // 获取本地对应的pdf文件名称
        if (!toPdfFile.exists()) {
            WaitDialog.dismiss();
            ToastUtils.showLong("源文件转pdf失败");
            updateDisplayView(false);
            return;
        }
        // 展示pdf文件
        previewFile(pdfFile);
    }

    // 下载文件
    private boolean downloadFile() {
        String fileName = FileUtils.parseUrlFileName(fileUrl);
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setUrl(fileUrl);
        downloadInfo.setFileName(fileName);
        downloadInfo.setListener(downloadListener);

        ExecutorService ioPool = ThreadUtils.getIoPool();
        ExecutorCompletionService<DownloadResult> completionService = new ExecutorCompletionService<>(ioPool);
        Future<DownloadResult> resultFuture = FileDownloadUtil.getInstance().download(completionService, downloadInfo);
        boolean isSuccess = false;
        try {
            // 阻塞当前线程
            DownloadResult result = resultFuture.get();
            isSuccess = result.isSuccess();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        ioPool.shutdown();
        String tips = isSuccess ? fileName + "下载成功" : fileName + "源文件下载失败";
        KLog.d(tips);
        if (!isSuccess) {
            WaitDialog.dismiss();
            ToastUtils.showLong(tips);
        }
        return isSuccess;
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