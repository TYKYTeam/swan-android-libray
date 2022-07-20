package com.tyky.filepreview.activity;

import android.os.Bundle;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.socks.library.KLog;
import com.tyky.filepreview.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import androidx.appcompat.app.AppCompatActivity;

public class PdfPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        String filePath = getIntent().getStringExtra("filePath");
        String content = FileIOUtils.readFile2String(filePath);

        PDFView pdfView = findViewById(R.id.pdfView);

        WaitDialog dialog = WaitDialog.show("加载中");
        if (content.startsWith("http")) {
            ThreadUtils.getIoPool().submit(() -> {

                try {
                    URLConnection urlConnection = new URL(content).openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    runOnUiThread(() -> {
                        pdfView.fromStream(inputStream)   //从链接加载pdf文件预览
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
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        KLog.e("下载失败："+e.getMessage());
                        dialog.doDismiss();
                        ToastUtils.showShort("下载pdf文件失败，原因" + e.getMessage());
                    });
                }

            });
        } else {
            //base64字符串 未测试！
            ThreadUtils.getIoPool().submit(() -> {
                byte[] bytes = EncodeUtils.base64Decode(content);

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

    }
}