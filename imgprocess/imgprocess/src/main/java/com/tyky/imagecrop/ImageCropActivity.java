package com.tyky.imagecrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.PhoneUtils;

import com.tyky.imagecrop.camera.TestActivity;
import com.tyky.imagecrop.imgprocess.PhotoUtils;
import com.vachel.editor.MyAddTextListener;

import me.jessyan.autosize.internal.CustomAdapt;
import me.pqpo.smartcropperlib.view.CropImageView;

public class ImageCropActivity extends AppCompatActivity implements View.OnClickListener, CustomAdapt {

    static Bitmap imagebitmap=null;
    CropImageView cropImageView=null;
    private int changedip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        changedip=getResources().getDisplayMetrics().densityDpi;
        BarUtils.setStatusBarColor(getWindow(), ColorUtils.getColor(R.color.tab_bar_color));

        cropImageView=findViewById(R.id.image_cropimg);
        imagebitmap.setDensity(changedip);
        cropImageView.setImageToCrop(imagebitmap);
        cropImageView.crop();

        initView();


    }

    private void initView(){
        ImageView back=findViewById(R.id.edit_back);
        back.setOnClickListener(this);
        RelativeLayout editleftrotation=findViewById(R.id.edit_left_rotation);
        editleftrotation.setOnClickListener(this);

        RelativeLayout editrightrotation=findViewById(R.id.edit_right_rotation);
        editrightrotation.setOnClickListener(this);

        RelativeLayout editnext=findViewById(R.id.edti_next);
        editnext.setOnClickListener(this);

        RelativeLayout takecameraagain=findViewById(R.id.camera_take_again);
        takecameraagain.setOnClickListener(this);

        RelativeLayout camerafull=findViewById(R.id.camera_full);
        camerafull.setOnClickListener(this);


    }

    public static void setBitmap(Bitmap bitmap, Activity activity){

        imagebitmap=bitmap;
        Intent intent=new Intent(activity,ImageCropActivity.class);
        activity.startActivity(intent);

    }

    private void leftRotation(){
        imagebitmap= PhotoUtils.rotateImage(imagebitmap,-90);
        cropImageView.setImageToCrop(imagebitmap);

    }

    private void rightRotation(){
        imagebitmap=PhotoUtils.rotateImage(imagebitmap,90);
        cropImageView.setImageToCrop(imagebitmap);
    }


    @Override
    public void onClick(View view) {

            if (view.getId()==R.id.edit_back){
                finish();

            }else if (view.getId()==R.id.edit_left_rotation){
                Log.d("MyTest","执行一次");
                leftRotation();

            }else if (view.getId()==R.id.edit_right_rotation){
                rightRotation();


            }else if (view.getId()==R.id.edti_next){
                ImageFinalProcessActivity.setBitmap(cropImageView.crop(),this);

            }else if (view.getId()==R.id.camera_take_again){
                startActivity(new Intent(this, TestActivity.class));
                finish();
            }else if (view.getId()==R.id.camera_full){
                cropImageView.setFullImgCrop();

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