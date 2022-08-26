package com.tyky.imagecrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



import com.tyky.imagecrop.addimgmark.AddWatermarkActivity;
import com.tyky.imagecrop.imgprocess.PhotoEnhance;
import com.tyky.imagecrop.imgprocess.PhotoUtils;
import com.vachel.editor.MyAddTextListener;
import com.vachel.editor.PictureEditActivity;
import com.vachel.editor.util.BitmapUtil;



import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static Bitmap srcbitmap=null;
    ImageView imageView=null;
    PhotoEnhance enhance=null;


    public static void setBitmap(Bitmap bitmap, Activity activity){
        srcbitmap=bitmap;
        activity.startActivity(new Intent(activity,ImageProcessActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        checkPermission();

        //需要考虑的问题是 scrbitmap从哪里来
        enhance=new PhotoEnhance(srcbitmap);

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        imageView=findViewById(R.id.srcimg);
        imageView.setImageBitmap(srcbitmap);

        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        List<String> datastring=new ArrayList<>();
        datastring.add("图片裁剪");
        datastring.add("图片旋转");
        datastring.add("添加文字");
        datastring.add("水平变换");
        datastring.add("上下翻转");
        datastring.add("添加水印");
        datastring.add("增强锐化");
        adapter.setDatalist(datastring);

        // 开始初始化一些组件
        SeekBar seekBar1=findViewById(R.id.seekbar1);
        SeekBar seekBar2=findViewById(R.id.seekbar2);
        SeekBar seekBar3=findViewById(R.id.seekbar3);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);

        TextView savetext=findViewById(R.id.saves);
        savetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将图片保存在本地
                Log.d("MyTest",Environment.getExternalStoragePublicDirectory("Download").getPath()+"/temp.jpg");
                BitmapUtil.saveBitmapFile(srcbitmap, Environment.getExternalStoragePublicDirectory("Download").getPath()+"/temp.jpg");
                Toast.makeText(view.getContext(), "保存成功", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!OpenCVLoader.initDebug())  // 初始化opencv
//            Log.e("OpenCv", "Unable to load OpenCV");
//        else
//            Log.e("OpenCv", "OpenCV loaded");
    }

    @Override
    public void onClick(View view) {

        TextView textView=(TextView) view;
        String msg=textView.getText().toString();
        if (msg.equals("图片裁剪")){
            ImageEditActivity.setBitmap(srcbitmap, this, new MyAddTextListener() {
                @Override
                public void setBitmap(Bitmap bitmap) {
                    Log.d("MyTest","执行一次");
                    srcbitmap=bitmap;
                    enhance=new PhotoEnhance(srcbitmap);
                    imageView.setImageBitmap(bitmap);

                }
            });


        }else if (msg.equals("图片旋转")){
            srcbitmap=PhotoUtils.rotateImage(srcbitmap,90);
            enhance=new PhotoEnhance(srcbitmap);
            imageView.setImageBitmap(srcbitmap);

        }else if (msg.equals("添加文字")){
            PictureEditActivity.setBitmap(srcbitmap, this, new MyAddTextListener() {
                @Override
                public void setBitmap(Bitmap bitmap) {
                    srcbitmap=bitmap;
                    enhance=new PhotoEnhance(srcbitmap);
                    imageView.setImageBitmap(srcbitmap);
                }
            });

        }else if (msg.equals("水平变换")){
            srcbitmap=PhotoUtils.reverseImage(srcbitmap,-1,1);
            imageView.setImageBitmap(srcbitmap);
            enhance=new PhotoEnhance(srcbitmap);


        }else if (msg.equals("上下翻转")){
            srcbitmap= PhotoUtils.reverseImage(srcbitmap,1,-1);
            imageView.setImageBitmap(srcbitmap);
            enhance=new PhotoEnhance(srcbitmap);

        }else if (msg.equals("添加水印")){
            AddWatermarkActivity.setBitmap(srcbitmap, this, new MyAddTextListener() {
                @Override
                public void setBitmap(Bitmap bitmap) {
                    srcbitmap=bitmap;
                    enhance=new PhotoEnhance(srcbitmap);
                    imageView.setImageBitmap(srcbitmap);
                }
            });


        }else if (msg.equals("增强锐化")){
//            srcbitmap=sharpenImage(srcbitmap);
            imageView.setImageBitmap(srcbitmap);
            enhance=new PhotoEnhance(srcbitmap);


        }

    }

//    private Bitmap sharpenImage(Bitmap inputImageBitmap) {
//        Mat img = new Mat();
////        Utils.bitmapToMat(inputImageBitmap, img);  // 将bitmap转换成了mat矩阵
//        Utils.bitmapToMat(inputImageBitmap,img,false);    // bitmap 的类型为 CV8uc4
//        Mat kener=new Mat(3,3, CvType.CV_32FC1);
//        kener.put(0,0,new float[]{0,-1,0,-1,5,-1,0,-1,0});
//
//        // Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
//        Mat dest = new Mat(img.rows(), img.cols(), img.type());  // 创建一个新的矩阵
////        Imgproc.GaussianBlur(img, dest, new Size(5, 5), 10);  // 高斯模糊，这个是初步的处理图像
////        Core.addWeighted(img, 1.5, dest, -0.5, 0, dest);  // 线性混合
//
//        List<Mat> list=new ArrayList<>();
////        Imgproc.cvtColor(img,);
//
////        Imgproc.filter2D(img,dest,-1,kener);   // 这个是用来 图像增强锐化的，但是并不能把图像增亮
//        Mat erodemat= Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
//        Imgproc.erode(img,dest,erodemat);
//        Core.split(dest,list);    // 成功将矩阵分成了一通道
//
//        for (int i=0;i<list.size();i++){
//            int z=list.get(i).rows()*list.get(i).cols();
//            byte[] mydata=new byte[z];
//            list.get(i).get(0,0,mydata);
//
//
//            for (int j=0;j<mydata.length;j++){
//                int s=mydata[j]&0xff;
//                int k= (int) (s*1.4+5);
//                if (k>255){
//                    k=255;
//                    ;                }
//                mydata[j]= (byte) k;
//            }
//            list.get(i).put(0,0,mydata);
//
//        }
//
//        Core.merge(list,dest);
//        Bitmap img_bitmap = Bitmap.createBitmap(dest.cols(), dest.rows(), Bitmap.Config.ARGB_8888);  //根据位图创建一个新的bitmap
//        Utils.matToBitmap(dest, img_bitmap);  // 将目的mat图像放到bitmap中
//        return img_bitmap;
//    }




    private void checkPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},12);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress=seekBar.getProgress();
        Log.d("MyTest","progress "+progress);
        if (seekBar.getId()==R.id.seekbar1){
            Log.d("MyTest","seekbar 1");
            enhance.setBrightness(progress);
            srcbitmap=enhance.handleImage(enhance.Enhance_Brightness);

        }else if (seekBar.getId()==R.id.seekbar2){
            Log.d("MyTest","seekbar 2");
            enhance.setSaturation(progress);
            srcbitmap=enhance.handleImage(enhance.Enhance_Saturation);

        }else if (seekBar.getId()==R.id.seekbar3){
            Log.d("MyTest","seekbar 3");
            enhance.setContrast(progress);
            srcbitmap=enhance.handleImage(enhance.Enhance_Contrast);

        }
        imageView.setImageBitmap(srcbitmap);

    }
}