package com.tyky.imagecrop.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.tyky.imagecrop.ImageCropActivity;
import com.tyky.imagecrop.R;

import java.io.File;

import me.jessyan.autosize.internal.CustomAdapt;
import me.pqpo.smartcropperlib.SmartCropper;

public class TestActivity extends AppCompatActivity implements CustomAdapt {

    JTCameraView jtCameraView=null;
    private String photoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){

            checkPermission();

        }else {
            setContentView(R.layout.activity_test);
            initView();
        }

        BarUtils.setStatusBarVisibility(getWindow(),false);

//        SmartCropper.buildImageDetector(this);


    }

    private void initView(){


        ImageView selectpicture=findViewById(R.id.camera_getpicture);
        selectpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPictureFromPhoto();
            }
        });

        ImageView imageViewback=findViewById(R.id.camera_test_back);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        jtCameraView=findViewById(R.id.jtcameraview);
        CoverView coverView=findViewById(R.id.cover);
        coverView.setListener(new CoverView.Listener() {
            @Override
            public void onFocus(Rect rect) {
                jtCameraView.focus(rect);
            }
        });

        ImageView imageView=findViewById(R.id.takepicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jtCameraView.takePicture();
            }
        });

        jtCameraView.setListener(new JTCameraListener() {
            @Override
            public void onCameraOpend() {

            }

            @Override
            public void onPreviewStart() {

            }

            @Override
            public void onPreviewStop() {

            }

            @Override
            public void onShutter() {

            }

            @Override
            public void onCupture(Bitmap bitmap) {
                Log.d("MyTest","获取到一张照片");
//                    ImageProcessActivity.setBitmap(bitmap,TestActivity.this);
//                ImageProcessActivity.setBitmap(bitmap,TestActivity.this);
                if (bitmap==null){
                    Log.d("MyTest","bitmap is null");
                    return;
                }else {
                    Log.d("MyTest","bitmap is not null");
                }
                ImageCropActivity.setBitmap(bitmap,TestActivity.this);
//                AddWatermarkActivity.setBitmap(bitmap,TestActivity.this,null);  //这个是直接跳转到添加水印界面
                finish();

            }

            @Override
            public void onCut(File file) {

            }

            @Override
            public void onCameraClosed() {

            }

            @Override
            public void onGetFaces(JTCameraView.Face[] faces) {

            }
        });



    }


    private void checkPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},12);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jtCameraView!=null){
            jtCameraView.stopPreview();
            jtCameraView.releaseCamera();
        }


    }


    /* 从相册中获取照片 */
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, 1021);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case 1021:

                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                photoPath = c.getString(columnIndex);
                c.close();
                Bitmap bitmap= BitmapFactory.decodeFile(photoPath);
                ImageCropActivity.setBitmap(bitmap,TestActivity.this);
//                AddWatermarkActivity.setBitmap(bitmap,TestActivity.this,null);  //这个是直接跳转到添加水印界面
                finish();
                break;
//                Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
//                break;

            default:
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==12){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setContentView(R.layout.activity_test);
                initView();
            }else {
                finish();
                ToastUtils.showShort("请打开摄像头权限");
            }
        }

    }


    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 360;
    }
}