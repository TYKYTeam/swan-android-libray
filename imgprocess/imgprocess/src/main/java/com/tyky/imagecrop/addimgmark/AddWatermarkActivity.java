package com.tyky.imagecrop.addimgmark;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;

import com.huantansheng.easyphotos.EasyPhotos;
import com.tyky.imagecrop.R;
import com.vachel.editor.MyAddTextListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 添加水印
 * @author jarlen
 */
public class AddWatermarkActivity extends Activity  {

    static Bitmap srcbitmap=null;
    OperateView operateView=null;
    LinearLayout linearLayout=null;
    private String photoPath = null;
    TextView selectpicture=null;
    OperateUtils operateUtils;
    private ImageButton btn_ok, btn_cancel;
    private static MyAddTextListener myAddTextListener=null;

    private static final int PHOTO_PICKED_WITH_DATA = 3021;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addwatermark);
        BarUtils.setStatusBarColor(getWindow(),getResources().getColor(R.color.top_bar_bg));

        linearLayout=findViewById(R.id.linearlayout);

        selectpicture=findViewById(R.id.selectpictue);

        btn_ok=findViewById(R.id.btn_ok);

        btn_cancel=findViewById(R.id.btn_cancel);

        fillContent();

        operateUtils=new OperateUtils(this);


        selectpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPictureFromPhoto();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave();
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void btnSave()
    {
        operateView.save();
        Bitmap bmp = getBitmapByView(operateView);  //从view里面获取图片
        if (bmp != null)
        {
            Log.d("MyTest","这个被执行了一次");
            linearLayout.removeAllViews();
//            ImageView imageView=new ImageView(this);
//            imageView.setImageBitmap(bmp);
//            linearLayout.addView(imageView);
            myAddTextListener.setBitmap(bmp);
        }
    }

    // 将模板View的图片转化为Bitmap
    public Bitmap getBitmapByView(View v)
    {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }


    private void addpic(String photoPath)
    {
        Bitmap bmp = BitmapFactory.decodeFile(photoPath);
        // ImageObject imgObject = operateUtils.getImageObject(bmp);
        ImageObject imgObject = operateUtils.getImageObject(bmp, operateView,
                5, 150, 100);  // 这个是一个水印图片
        operateView.addItem(imgObject);  // 这个imgobject 就是表示的是 一个水印，其中有水印图片、旋转图片和x号图片
    }

    private void fillContent()
    {
        operateView = new OperateView(AddWatermarkActivity.this, srcbitmap);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                srcbitmap.getWidth(), srcbitmap.getHeight());   // 这个view的宽高是 背景图片的宽高
        operateView.setLayoutParams(layoutParams);
        linearLayout.addView(operateView);
        operateView.setMultiAdd(true); // 设置此参数，可以添加多个图片
    }

    public static void setBitmap(Bitmap bitmap, Activity activity, MyAddTextListener listener){

        srcbitmap=bitmap;
        activity.startActivity(new Intent(activity,AddWatermarkActivity.class));
        myAddTextListener=listener;



    }

    /* 从相册中获取照片 */
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, PHOTO_PICKED_WITH_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case PHOTO_PICKED_WITH_DATA:

                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                photoPath = c.getString(columnIndex);
                c.close();
                addpic(photoPath);
                break;
//                Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
//                break;

            default:
                break;
        }

    }

}
