package com.tyky.media.activity;

import android.os.Bundle;

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.tyky.media.R;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class PdfPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        PDFView pdfView = findViewById(R.id.pdfView);

        File pdfFile = new File(PathUtils.getExternalAppCachePath(), "test.pdf");
        ResourceUtils.copyFileFromAssets("test.pdf", pdfFile.getPath());

        WaitDialog dialog = WaitDialog.show("加载中");

        pdfView.fromFile(pdfFile)   //设置pdf文件地址
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
        pdfView.zoomWithAnimation(3f);


    }
}