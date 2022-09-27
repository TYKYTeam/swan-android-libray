package com.tyky.imagecrop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.tyky.imagecrop.camera.TestActivity;


import me.pqpo.smartcropperlib.SmartCropper;
import me.pqpo.smartcropperlib.view.CropImageView;

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SmartCropper.buildImageDetector(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        checkPermission();

        Button button=findViewById(R.id.addtext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));

//                startActivity(new Intent(MainActivity.this,ImageFinalProcessActivity.class));

            }
        });


        // 现在开始移植图片添加水印

    }


    private void checkPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},12);

    }

}