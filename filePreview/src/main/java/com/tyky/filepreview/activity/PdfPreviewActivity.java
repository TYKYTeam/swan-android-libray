package com.tyky.filepreview.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.socks.library.KLog;
import com.tyky.filepreview.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Future;

public class PdfPreviewActivity extends AppCompatActivity {
    private Future<?> displayPdf;
    private String fileContent;
    private View imageBack;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        initData();
        initView();
        bindListener();
        loadPdf();
    }

    private void loadPdf() {
        WaitDialog dialog = WaitDialog.show("加载中");
        if (fileContent.startsWith("http")) {
            displayPdf = ThreadUtils.getIoPool().submit(() -> {
                try {
                    URLConnection urlConnection = new URL(fileContent).openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    runOnUiThread(() -> {
                        pdfView.fitToWidth();
                        pdfView.zoomWithAnimation(1f);
                        pdfView.fromStream(inputStream)   //从链接加载pdf文件预览
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
                                .onLoad(nbPages -> dialog.doDismiss())
                                .load();
                    });
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        KLog.e("下载失败：" + e.getMessage());
                        dialog.doDismiss();
                        ToastUtils.showShort("下载pdf文件失败，原因" + e.getMessage());
                    });
                }
            });
            return;
        }

        //base64字符串 未测试！
        ThreadUtils.getIoPool().submit(() -> {
            byte[] bytes = EncodeUtils.base64Decode(fileContent);
            runOnUiThread(() -> {
                pdfView.fromBytes(bytes)   //设置pdf文件地址
                        .defaultPage(0)
                        .swipeHorizontal(false)
                        .enableSwipe(false)
                        .enableDoubletap(true)
                        .enableAnnotationRendering(true)
                        //.scrollHandle(new DefaultScrollHandle(PdfShowActivity.this))
                        .spacing(10) // in dp
                        .onLoad(nbPages -> dialog.doDismiss())
                        .load();

                pdfView.fitToWidth();
                pdfView.zoomWithAnimation(1f);
            });
        });
    }

    private void bindListener() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayPdf != null && !displayPdf.isCancelled()) {
                    displayPdf.cancel(true);
                }
                finish();
            }
        });
    }

    private void initView() {
        imageBack = findViewById(R.id.imageBack);
        pdfView = findViewById(R.id.pdfView);
    }

    private void initData() {
        String filePath = getIntent().getStringExtra("filePath");
        fileContent = FileIOUtils.readFile2String(filePath);
    }
}