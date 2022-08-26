package com.tyky.imagecrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vachel.editor.MyAddTextListener;
import com.vachel.editor.PictureEditActivity;

import me.pqpo.smartcropperlib.view.CropImageView;

public class ImageEditActivity extends AppCompatActivity {

    private static Bitmap imagebitmap=null;
    private static MyAddTextListener mlistener=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);

        CropImageView imageView=findViewById(R.id.cropimg);
        TextView canceltext=findViewById(R.id.cancel);
        TextView ensuretext=findViewById(R.id.ensure);
        canceltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ensuretext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.setBitmap(imageView.crop());
                imagebitmap=null;
                finish();
            }
        });

        imageView.setImageToCrop(imagebitmap);






    }

    public static void setBitmap(Bitmap bitmap,Activity activity,MyAddTextListener listener){
        imagebitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
        mlistener=listener;
        Intent intent=new Intent(activity,ImageEditActivity.class);
        activity.startActivity(intent);

    }








}