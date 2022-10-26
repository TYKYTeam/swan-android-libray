package com.tyky.imagecrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import com.tyky.imagecrop.addimgmark.AddWatermarkActivity;
import com.tyky.imagecrop.imgprocess.PhotoUtils;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.vachel.editor.MyAddTextListener;
import com.vachel.editor.PictureEditActivity;

import org.greenrobot.eventbus.EventBus;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jessyan.autosize.internal.CustomAdapt;

public class ImageFinalProcessActivity extends AppCompatActivity implements View.OnClickListener, CustomAdapt {

    static Bitmap imagebitmap=null;
    ImageView imageView=null;
    Bitmap originagImg=null;

    ImageView originalSrc=null;
    ImageView ruihuaSrc=null;
    ImageView originalMask=null;
    ImageView ruihuaMask=null;

    int currentSelectId=-1;
    static WeakReference<Activity> preActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_final_process);

        if (!OpenCVLoader.initDebug()) { // 初始化opencv
            Log.e("OpenCv", "Unable to load OpenCV");
        }else{
            Log.e("OpenCv", "OpenCV loaded");
    }

        BarUtils.setStatusBarColor(getWindow(), ColorUtils.getColor(R.color.tab_bar_color));

        initView();

    }


    public static Bitmap fitBitmap(Bitmap target, int newWidth)
    {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        // float scaleHeight = ((float)newHeight) / height;
        int newHeight = (int) (scaleWidth * height);
        matrix.postScale(scaleWidth, scaleWidth);
        // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
        // matrix,true);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
//        if (target != null && !target.equals(bmp) && !target.isRecycled())
//        {
//            target.recycle();
//            target = null;
//        }
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
        // true);
    }


    public static void setBitmap(Bitmap bitmap, Activity activity){
        preActivity=new WeakReference<>(activity);
        imagebitmap=bitmap;
        Intent intent=new Intent(activity,ImageFinalProcessActivity.class);
        activity.startActivity(intent);

    }

    private void initView(){
        imageView=findViewById(R.id.camera_process_showimg);
        imageView.setImageBitmap(imagebitmap);

        RelativeLayout cameraFinalOne=findViewById(R.id.camera_final_1);
        RelativeLayout cameraFinalTwo=findViewById(R.id.camera_final_2);
        RelativeLayout cameraFinalThree=findViewById(R.id.camera_final_3);
        RelativeLayout cameraFinalFour=findViewById(R.id.camera_final_4);
        RelativeLayout cameraFinalFive=findViewById(R.id.camera_final_5);

        RelativeLayout cameraSrcLayout=findViewById(R.id.camera_relative_one);
        RelativeLayout cameraRuiHuaLayout=findViewById(R.id.camera_relative_two);

        ImageView saveimg=findViewById(R.id.camera_save);
        saveimg.setOnClickListener(this);

        cameraFinalOne.setOnClickListener(this);
        cameraFinalTwo.setOnClickListener(this);
        cameraFinalThree.setOnClickListener(this);
        cameraFinalFour.setOnClickListener(this);
        cameraFinalFive.setOnClickListener(this);

        cameraSrcLayout.setOnClickListener(this);
        cameraRuiHuaLayout.setOnClickListener(this);

        originalSrc=findViewById(R.id.camera_original_1);
        ruihuaSrc=findViewById(R.id.camera_ruihua_1);
        originalMask=findViewById(R.id.camera_original_2);
        ruihuaMask=findViewById(R.id.camera_ruihua_2);
        originagImg=imagebitmap.copy(Bitmap.Config.ARGB_8888,false);

        Bitmap tempBitmap=fitBitmap(imagebitmap,300);
        originalSrc.setImageBitmap(tempBitmap);
        ruihuaSrc.setImageBitmap(sharpenImage(tempBitmap));
        
        changeStyle(R.id.camera_relative_one);

        ImageView cameraprocessback=findViewById(R.id.camera_process_back);
        cameraprocessback.setOnClickListener(this);



    }

    private Bitmap sharpenImage(Bitmap inputImageBitmap) {
        Mat img = new Mat();
//        Utils.bitmapToMat(inputImageBitmap, img);  // 将bitmap转换成了mat矩阵
        Utils.bitmapToMat(inputImageBitmap,img,false);    // bitmap 的类型为 CV8uc4
        Mat kener=new Mat(3,3, CvType.CV_32FC1);
        kener.put(0,0,new float[]{0,-1,0,-1,5,-1,0,-1,0});

        // Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);
        Mat dest = new Mat(img.rows(), img.cols(), img.type());  // 创建一个新的矩阵
//        Imgproc.GaussianBlur(img, dest, new Size(5, 5), 10);  // 高斯模糊，这个是初步的处理图像
//        Core.addWeighted(img, 1.5, dest, -0.5, 0, dest);  // 线性混合

        List<Mat> list=new ArrayList<>();
//        Imgproc.cvtColor(img,);

//        Imgproc.filter2D(img,dest,-1,kener);   // 这个是用来 图像增强锐化的，但是并不能把图像增亮
        Mat erodemat= Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
        Imgproc.erode(img,dest,erodemat);
        Core.split(dest,list);    // 成功将矩阵分成了一通道

        for (int i=0;i<list.size();i++){
            int z=list.get(i).rows()*list.get(i).cols();
            byte[] mydata=new byte[z];
            list.get(i).get(0,0,mydata);


            for (int j=0;j<mydata.length;j++){
                int s=mydata[j]&0xff;
                int k= (int) (s*1.4+5);
                if (k>255){
                    k=255;
                    ;                }
                mydata[j]= (byte) k;
            }
            list.get(i).put(0,0,mydata);

        }

        Core.merge(list,dest);
        Bitmap img_bitmap = Bitmap.createBitmap(dest.cols(), dest.rows(), Bitmap.Config.ARGB_8888);  //根据位图创建一个新的bitmap
        Utils.matToBitmap(dest, img_bitmap);  // 将目的mat图像放到bitmap中
        return img_bitmap;
    }




    private void changeStyle(int id){

        if (id==R.id.camera_relative_one){
            originalMask.setVisibility(View.VISIBLE);
            ruihuaMask.setVisibility(View.GONE);



        }else if (id==R.id.camera_relative_two){
            originalMask.setVisibility(View.GONE);
            ruihuaMask.setVisibility(View.VISIBLE);


        }

        currentSelectId=id;

    }


    @Override
    public void onClick(View view) {

        int id=view.getId();
        if (id==R.id.camera_final_1){

          imagebitmap= PhotoUtils.rotateImage(imagebitmap,-90);
          imageView.setImageBitmap(imagebitmap);

        }else if (id==R.id.camera_final_2){
            PictureEditActivity.setBitmap(imagebitmap, this, new MyAddTextListener() {
                @Override
                public void setBitmap(Bitmap bitmap) {
                    imagebitmap=bitmap;
                    imageView.setImageBitmap(imagebitmap);
                }
            });

        }else if(id==R.id.camera_final_3){
            AddWatermarkActivity.setBitmap(imagebitmap, this, new MyAddTextListener() {
                @Override
                public void setBitmap(Bitmap bitmap) {
                    imagebitmap=bitmap;
                    imageView.setImageBitmap(imagebitmap);
                }
            });

        }else if (id==R.id.camera_final_4){
            imagebitmap=PhotoUtils.reverseImage(imagebitmap,-1,1);
            imageView.setImageBitmap(imagebitmap);

        }else if (id==R.id.camera_final_5){
            imagebitmap=PhotoUtils.reverseImage(imagebitmap,1,-1);
            imageView.setImageBitmap(imagebitmap);

        }else if (id==R.id.camera_relative_one){
            changeStyle(id);
            Bitmap newbitmap=originagImg.copy(Bitmap.Config.ARGB_8888,true);
            imagebitmap.recycle();
            imagebitmap=newbitmap;
            imageView.setImageBitmap(imagebitmap);

        }else if (id==R.id.camera_relative_two){
            if (currentSelectId==id){
                return;
            }
            changeStyle(id);
            imagebitmap=sharpenImage(imagebitmap);
            imageView.setImageBitmap(imagebitmap);

        }else if (id==R.id.camera_save){

            File receivefile=ImageUtils.save2Album(imagebitmap, Bitmap.CompressFormat.JPEG);
//            EventBus.getDefault().post(new JsCallBackEvent());
            String methodimg= SPUtils.getInstance().getString("method_img");
            int type=SPUtils.getInstance().getInt("method_imgtype",1);
            Map<String,String> map=new HashMap<>();
            map.put("path",receivefile.getPath());
            if (type==2){
                String base64 = EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(imagebitmap));
                map.put("base64",base64);
            }
            EventBus.getDefault().post(new JsCallBackEvent(methodimg,map));
            ToastUtils.showShort("成功保存到相册");
            imagebitmap.recycle();
            preActivity.get().finish();
            finish();

        }else if (id==R.id.camera_process_back){
            finish();
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