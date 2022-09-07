package com.tyky.imagecrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;


import com.huantansheng.easyphotos.utils.bitmap.BitmapUtils;
import com.tyky.imagecrop.addimgmark.ImageObject;
import com.tyky.imagecrop.addimgmark.OperateUtils;
import com.tyky.imagecrop.addimgmark.OperateView;
import com.tyky.imagecrop.camera.TestActivity;
import com.tyky.imagecrop.imgprocess.PhotoUtils;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.vachel.editor.EditMode;
import com.vachel.editor.IEditSave;
import com.vachel.editor.MyAddTextListener;
import com.vachel.editor.bean.StickerText;
import com.vachel.editor.ui.CustomColorGroup;
import com.vachel.editor.ui.PictureEditView;
import com.vachel.editor.ui.sticker.ImageStickerView;
import com.vachel.editor.util.BitmapUtil;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.jessyan.autosize.internal.CustomAdapt;
import me.pqpo.smartcropperlib.SmartCropper;
import me.pqpo.smartcropperlib.view.CropImageView;

public class ImageCropActivity extends AppCompatActivity implements View.OnClickListener, CustomAdapt, PictureEditView.IOnPathListener, RadioGroup.OnCheckedChangeListener {

    static Bitmap imagebitmap=null;
    static Bitmap originalbitmap=null;
    CropImageView cropImageView=null;


    RelativeLayout item1rel6=null;
    RelativeLayout item1rel7=null;
    RelativeLayout item1rel8=null;
    RelativeLayout item1rel9=null;

    int changedip=0;

    // 这是Main 菜单的

    ImageView item1rel6img=null;
    ImageView item1rel7img=null;
    ImageView item1rel8img=null;
    ImageView item1rel9img=null;

    TextView item1rel6text=null;
    TextView item1rel7text=null;
    TextView item1rel8text=null;
    TextView item1rel9text=null;

    //这个是主菜单第一个点击按钮的子内容

    ImageView item1rel1img=null;
    ImageView item1rel2img=null;
    ImageView item1rel3img=null;
    ImageView item1rel4img=null;
    ImageView item1rel5img=null;

    TextView item1rel1text=null;
    TextView item1rel2text=null;
    TextView item1rel3text=null;
    TextView item1rel4text=null;
    TextView item1rel5text=null;

    FrameLayout frameLayout=null;

    LinearLayout imgShowContainer=null;

    LinearLayout editcontainerokor=null;

    PictureEditView pictureEditView=null;

    CustomColorGroup customColorGroup=null;
    private OperateUtils operateUtils=null;
    private OperateView operateView=null;

    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    private String photoPath = null;

    private boolean isfirstpage=true;

    private ImageView editSvar=null;


    // 需要一种机制，能够在页面切换的时候还能够保证能够对选择的按钮有所记忆，那就设置一种吧

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        SmartCropper.buildImageDetector(this);
        BarUtils.setStatusBarVisibility(getWindow(),false);
        imgShowContainer=findViewById(R.id.container_img_view);
        frameLayout=findViewById(R.id.image_process_bottom);
        changedip=getResources().getDisplayMetrics().densityDpi;
        initview();
        initFirstView();
        changeMainSytle(6);
        showOrginalImg();
        changeXiuJianStyle(1);

    }

    private void initview() {

         ImageView backcamera=findViewById(R.id.edit_back);
         backcamera.setOnClickListener(this);

         editcontainerokor=findViewById(R.id.edit_okorrefresh);
         ImageView editeditok=findViewById(R.id.edit_editok);
         ImageView editeditrefresh=findViewById(R.id.edit_editrefresh);
         editeditok.setOnClickListener(this);
         editeditrefresh.setOnClickListener(this);

         editSvar=findViewById(R.id.edit_save);
         editSvar.setOnClickListener(this);

    }

    private void setSaveIsVisibly(boolean isVisibly){
        if (isVisibly){
            editSvar.setVisibility(View.VISIBLE);
        }else {
            editSvar.setVisibility(View.GONE);
        }
    }

    private Bitmap fangdaBitmap(Bitmap bitmap){
        Log.d("MyTest","  执行一次 ："+bitmap.getWidth()+"    "+bitmap.getHeight());

        return null;
    }




    private void showOrginalImg(){

        imgShowContainer.removeAllViews();
        ImageView imageView=new ImageView(this);
        imageView.setImageBitmap(originalbitmap);
        imgShowContainer.addView(imageView);

        imagebitmap.recycle();
        imagebitmap=originalbitmap.copy(Bitmap.Config.ARGB_8888,true);

    }

    private void addCropImageView(){

        imgShowContainer.removeAllViews();
        cropImageView=new CropImageView(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cropImageView.setLayoutParams(layoutParams);
        cropImageView.setPadding(50,50,50,50);
        imgShowContainer.addView(cropImageView);
        //bitmap 的大小与设备的密度有关，必须要设置好bitmap的密度值cropimageview 才能够正确的绘制
        imagebitmap.setDensity(changedip);
        cropImageView.setImageToCrop(imagebitmap);

        editcontainerokor.setVisibility(View.VISIBLE);


    }

    // 添加imageview
    private void addImageView(){
        imgShowContainer.removeAllViews();
        ImageView imageView=new ImageView(this);
        Log.d("MyTest","imagebitmap :"+imagebitmap.getWidth()+"   "+imagebitmap.getHeight());
        imagebitmap.setDensity(originalbitmap.getDensity());
        imageView.setImageBitmap(imagebitmap);

        imgShowContainer.addView(imageView);
    }

    private void addBiaoQianView(){

        imgShowContainer.removeAllViews();
        pictureEditView=new PictureEditView(this);
//        pictureEditView.setBackgroundColor(Color.parseColor("#D1CFCF"));
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pictureEditView.setLayoutParams(layoutParams);
        pictureEditView.setBackgroundColor(Color.parseColor("#000000"));
        imagebitmap.setDensity(originalbitmap.getDensity());
        pictureEditView.setImageBitmap(imagebitmap.copy(Bitmap.Config.ARGB_8888,true));
        imgShowContainer.addView(pictureEditView);


        // 添加一个标签试一下
//        pictureEditView.addStickerText(new StickerText("点击编辑文字",ColorUtils.getColor(R.color.purple_700)),true);
//        pictureEditView.addStickerText(new StickerText("",ColorUtils.getColor(R.color.purple_700)),true);


    }

    private void addTuYaView(){
        imgShowContainer.removeAllViews();
        pictureEditView=new PictureEditView(this);
//        pictureEditView.setBackgroundColor(Color.parseColor("#D1CFCF"));
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pictureEditView.setLayoutParams(layoutParams);
        pictureEditView.setBackgroundColor(Color.parseColor("#000000"));
//        imagebitmap.setDensity(originalbitmap.getDensity());

        pictureEditView.setImageBitmap(imagebitmap.copy(Bitmap.Config.ARGB_8888,true));
        imgShowContainer.addView(pictureEditView);

//        pictureEditView.setMode(EditMode.DOODLE);

        pictureEditView.setOnPathListener(this);


    }




    private void  initFirstView(){

        frameLayout.removeAllViews();

        View view=getLayoutInflater().inflate(R.layout.item_page1,null,false);  // 问题在于，我需要不断的切换这个界面  ，，我需要切换切换这个界面，每个界面都有独特的元素
        frameLayout.addView(view);
        RelativeLayout item1rel1=view.findViewById(R.id.item1_rel_1);
        RelativeLayout item1rel2=view.findViewById(R.id.item1_rel_2);
        RelativeLayout item1rel3=view.findViewById(R.id.item1_rel_3);
        RelativeLayout item1rel4=view.findViewById(R.id.item1_rel_4);
        RelativeLayout item1rel5=view.findViewById(R.id.item1_rel_5);
        item1rel6=view.findViewById(R.id.item1_rel_6);
        item1rel7=view.findViewById(R.id.item1_rel_7);
        item1rel8=view.findViewById(R.id.item1_rel_8);
        item1rel9=view.findViewById(R.id.item1_rel_9);


        item1rel1.setOnClickListener(this);
        item1rel2.setOnClickListener(this);
        item1rel3.setOnClickListener(this);
        item1rel4.setOnClickListener(this);
        item1rel5.setOnClickListener(this);
        item1rel6.setOnClickListener(this);
        item1rel7.setOnClickListener(this);
        item1rel8.setOnClickListener(this);
        item1rel9.setOnClickListener(this);


        item1rel1img=view.findViewById(R.id.item1_rel_1_img);
        item1rel2img=view.findViewById(R.id.item1_rel_2_img);
        item1rel3img=view.findViewById(R.id.item1_rel_3_img);
        item1rel4img=view.findViewById(R.id.item1_rel_4_img);
        item1rel5img=view.findViewById(R.id.item1_rel_5_img);
        item1rel6img=view.findViewById(R.id.item1_rel_6_img);
        item1rel7img=view.findViewById(R.id.item1_rel_7_img);
        item1rel8img=view.findViewById(R.id.item1_rel_8_img);
        item1rel9img=view.findViewById(R.id.item1_rel_9_img);


        item1rel1text=view.findViewById(R.id.item1_rel_1_text);
        item1rel2text=view.findViewById(R.id.item1_rel_2_text);
        item1rel3text=view.findViewById(R.id.item1_rel_3_text);
        item1rel4text=view.findViewById(R.id.item1_rel_4_text);
        item1rel5text=view.findViewById(R.id.item1_rel_5_text);
        item1rel6text=view.findViewById(R.id.item1_rel_6_text);
        item1rel7text=view.findViewById(R.id.item1_rel_7_text);
        item1rel8text=view.findViewById(R.id.item1_rel_8_text);
        item1rel9text=view.findViewById(R.id.item1_rel_9_text);


    }

    private void  initSecondView(){

        frameLayout.removeAllViews();
        View view=getLayoutInflater().inflate(R.layout.item_page2,null,false);  // 问题在于，我需要不断的切换这个界面  ，，我需要切换切换这个界面，每个界面都有独特的元素
        frameLayout.addView(view);
        RelativeLayout item2rel1=view.findViewById(R.id.item2_rel_1);
        RelativeLayout item2rel2=view.findViewById(R.id.item2_rel_2);
        RelativeLayout item2rel3=view.findViewById(R.id.item2_rel_3);
        item1rel6=view.findViewById(R.id.item1_rel_6);
        item1rel7=view.findViewById(R.id.item1_rel_7);
        item1rel8=view.findViewById(R.id.item1_rel_8);
        item1rel9=view.findViewById(R.id.item1_rel_9);


        item2rel1.setOnClickListener(this);
        item2rel2.setOnClickListener(this);
        item2rel3.setOnClickListener(this);
        item1rel6.setOnClickListener(this);
        item1rel7.setOnClickListener(this);
        item1rel8.setOnClickListener(this);
        item1rel9.setOnClickListener(this);


        item1rel1img=view.findViewById(R.id.item1_rel_1_img);
        item1rel2img=view.findViewById(R.id.item1_rel_2_img);
        item1rel3img=view.findViewById(R.id.item1_rel_3_img);

        item1rel6img=view.findViewById(R.id.item1_rel_6_img);
        item1rel7img=view.findViewById(R.id.item1_rel_7_img);
        item1rel8img=view.findViewById(R.id.item1_rel_8_img);
        item1rel9img=view.findViewById(R.id.item1_rel_9_img);


        item1rel1text=view.findViewById(R.id.item1_rel_1_text);
        item1rel2text=view.findViewById(R.id.item1_rel_2_text);
        item1rel3text=view.findViewById(R.id.item1_rel_3_text);

        item1rel6text=view.findViewById(R.id.item1_rel_6_text);
        item1rel7text=view.findViewById(R.id.item1_rel_7_text);
        item1rel8text=view.findViewById(R.id.item1_rel_8_text);
        item1rel9text=view.findViewById(R.id.item1_rel_9_text);


    }



    //  样式的改变有这种特点，就是子样式不改变父样式
    private void changeMainSytle(int position){

        setEditContainerGone();
        addImageView();

        if (position==6){

            setSaveIsVisibly(true);

            item1rel6img.setImageResource(R.drawable.design_edit_edit_select);
            item1rel6text.setTextColor(Color.parseColor("#628ef7"));

            item1rel7img.setImageResource(R.drawable.design_edit_bianji);
            item1rel7text.setTextColor(Color.parseColor("#ffffff"));

            item1rel8img.setImageResource(R.drawable.design_edit_lujing);
            item1rel8text.setTextColor(Color.parseColor("#ffffff"));

            item1rel9img.setImageResource(R.drawable.design_edit_shuiying);
            item1rel9text.setTextColor(Color.parseColor("#ffffff"));



        }else if (position==7){

            setSaveIsVisibly(true);

            item1rel6img.setImageResource(R.drawable.design_edit_edit_noselect);
            item1rel6text.setTextColor(Color.parseColor("#ffffff"));

            item1rel7img.setImageResource(R.drawable.design_edit_bianji_select);
            item1rel7text.setTextColor(Color.parseColor("#628ef7"));

            item1rel8img.setImageResource(R.drawable.design_edit_lujing);
            item1rel8text.setTextColor(Color.parseColor("#ffffff"));

            item1rel9img.setImageResource(R.drawable.design_edit_shuiying);
            item1rel9text.setTextColor(Color.parseColor("#ffffff"));



        }else if (position==8){

            setSaveIsVisibly(true);

            item1rel6img.setImageResource(R.drawable.design_edit_edit_noselect);
            item1rel6text.setTextColor(Color.parseColor("#ffffff"));

            item1rel7img.setImageResource(R.drawable.design_edit_bianji);
            item1rel7text.setTextColor(Color.parseColor("#ffffff"));

            item1rel8img.setImageResource(R.drawable.design_edit_lujing_select);
            item1rel8text.setTextColor(Color.parseColor("#628ef7"));

            item1rel9img.setImageResource(R.drawable.design_edit_shuiying);
            item1rel9text.setTextColor(Color.parseColor("#ffffff"));



        }else if (position==9){

            setSaveIsVisibly(false);

            item1rel6img.setImageResource(R.drawable.design_edit_edit_noselect);
            item1rel6text.setTextColor(Color.parseColor("#ffffff"));

            item1rel7img.setImageResource(R.drawable.design_edit_bianji);
            item1rel7text.setTextColor(Color.parseColor("#ffffff"));

            item1rel8img.setImageResource(R.drawable.design_edit_lujing);
            item1rel8text.setTextColor(Color.parseColor("#ffffff"));

            item1rel9img.setImageResource(R.drawable.design_edit_shuiying_select);
            item1rel9text.setTextColor(Color.parseColor("#628ef7"));


        }


    }

    private void changeXiuJianStyle(int position){

        if (position==1){

            setSaveIsVisibly(true);

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg_select);
            item1rel1text.setTextColor(Color.parseColor("#628ef7"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation);
            item1rel2text.setTextColor(Color.parseColor("#ffffff"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation);
            item1rel3text.setTextColor(Color.parseColor("#ffffff"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang);
            item1rel4text.setTextColor(Color.parseColor("#ffffff"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen);
            item1rel5text.setTextColor(Color.parseColor("#ffffff"));

        }else if (position==2){

            setSaveIsVisibly(true);

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg);
            item1rel1text.setTextColor(Color.parseColor("#ffffff"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation_select);
            item1rel2text.setTextColor(Color.parseColor("#628ef7"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation);
            item1rel3text.setTextColor(Color.parseColor("#ffffff"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang);
            item1rel4text.setTextColor(Color.parseColor("#ffffff"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen);
            item1rel5text.setTextColor(Color.parseColor("#ffffff"));


        }else if (position==3){

            setSaveIsVisibly(true);

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg);
            item1rel1text.setTextColor(Color.parseColor("#ffffff"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation);
            item1rel2text.setTextColor(Color.parseColor("#ffffff"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation_select);
            item1rel3text.setTextColor(Color.parseColor("#628ef7"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang);
            item1rel4text.setTextColor(Color.parseColor("#ffffff"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen);
            item1rel5text.setTextColor(Color.parseColor("#ffffff"));

        }else if (position==4){

            setSaveIsVisibly(true);

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg);
            item1rel1text.setTextColor(Color.parseColor("#ffffff"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation);
            item1rel2text.setTextColor(Color.parseColor("#ffffff"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation);
            item1rel3text.setTextColor(Color.parseColor("#ffffff"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang_select);
            item1rel4text.setTextColor(Color.parseColor("#628ef7"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen);
            item1rel5text.setTextColor(Color.parseColor("#ffffff"));

        }else if (position==5){

            setSaveIsVisibly(false);

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg);
            item1rel1text.setTextColor(Color.parseColor("#ffffff"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation);
            item1rel2text.setTextColor(Color.parseColor("#ffffff"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation);
            item1rel3text.setTextColor(Color.parseColor("#ffffff"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang);
            item1rel4text.setTextColor(Color.parseColor("#ffffff"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen_select);
            item1rel5text.setTextColor(Color.parseColor("#628ef7"));

        }


        if (position!=5){

            setEditContainerGone();

        }

    }

    private void changeBianJiStyle(int position){

        if (position==1){

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg_select);
            item1rel1text.setTextColor(Color.parseColor("#628ef7"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation);
            item1rel2text.setTextColor(Color.parseColor("#ffffff"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation);
            item1rel3text.setTextColor(Color.parseColor("#ffffff"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang);
            item1rel4text.setTextColor(Color.parseColor("#ffffff"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen);
            item1rel5text.setTextColor(Color.parseColor("#ffffff"));

        }else if (position==2){

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg);
            item1rel1text.setTextColor(Color.parseColor("#ffffff"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation_select);
            item1rel2text.setTextColor(Color.parseColor("#628ef7"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation);
            item1rel3text.setTextColor(Color.parseColor("#ffffff"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang);
            item1rel4text.setTextColor(Color.parseColor("#ffffff"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen);
            item1rel5text.setTextColor(Color.parseColor("#ffffff"));


        }else if (position==3){

            item1rel1img.setImageResource(R.drawable.design_edit_srcimg);
            item1rel1text.setTextColor(Color.parseColor("#ffffff"));

            item1rel2img.setImageResource(R.drawable.design_edit_leftrotation);
            item1rel2text.setTextColor(Color.parseColor("#ffffff"));

            item1rel3img.setImageResource(R.drawable.design_edit_rightrotation_select);
            item1rel3text.setTextColor(Color.parseColor("#628ef7"));

            item1rel4img.setImageResource(R.drawable.design_edit_jingxiang);
            item1rel4text.setTextColor(Color.parseColor("#ffffff"));

            item1rel5img.setImageResource(R.drawable.design_edit_xiuzhen);
            item1rel5text.setTextColor(Color.parseColor("#ffffff"));

        }




    }



    public static void setBitmap(Bitmap bitmap, Activity activity){

        imagebitmap=bitmap;
        originalbitmap=imagebitmap.copy(Bitmap.Config.ARGB_8888,true);
        Intent intent=new Intent(activity,ImageCropActivity.class);
        activity.startActivity(intent);

    }

    private void leftRotation(){
        imagebitmap=PhotoUtils.rotateImage(imagebitmap,-90);

    }

    private void rightRotation(){
        imagebitmap=PhotoUtils.rotateImage(imagebitmap,90);
    }

    @Override
    public void onClick(View view) {

//            if (view.getId()==R.id.edit_back){
//                finish();
//
//            }else if (view.getId()==R.id.edit_left_rotation){
//                Log.d("MyTest","执行一次");
//                leftRotation();
//
//            }else if (view.getId()==R.id.edit_right_rotation){
//                rightRotation();
//
//
//            }else if (view.getId()==R.id.edti_next){
//                ImageFinalProcessActivity.setBitmap(cropImageView.crop(),this);
//
//            }else if (view.getId()==R.id.camera_take_again){
//                startActivity(new Intent(this, TestActivity.class));
//                finish();
//            }

        int viewid=view.getId();
        if (viewid == R.id.item1_rel_1) {  // 原图
            showOrginalImg();
            changeXiuJianStyle(1);
        } else if (viewid == R.id.item1_rel_2) {  // 左转
            changeXiuJianStyle(2);
            leftRotation();
            addImageView();
        } else if (viewid == R.id.item1_rel_3) {  //右转
            changeXiuJianStyle(3);
            rightRotation();
            addImageView();
        } else if (viewid == R.id.item1_rel_4) {  //镜像
            changeXiuJianStyle(4);
            imagebitmap = PhotoUtils.reverseImage(imagebitmap, -1, 1);
            addImageView();
        } else if (viewid == R.id.item1_rel_5) {
            changeXiuJianStyle(5);
            addCropImageView();
        } else if (viewid == R.id.item1_rel_6) {
            initFirstView();
            changeMainSytle(6);
        } else if (viewid == R.id.item1_rel_7) {
            initSecondView();
            changeMainSytle(7);
        } else if (viewid == R.id.item1_rel_8) {
            changeMainSytle(8);
        } else if (viewid == R.id.item1_rel_9) {
            changeMainSytle(9);
            initShuiYinView();
            addShuiYinView();
        } else if (viewid == R.id.item2_rel_1) {
            Log.d("MyTest", "标签点击一次");
            setSaveIsVisibly(false);
            initBiaoQianView();
            addBiaoQianView();
        } else if (viewid == R.id.item2_rel_2) {
            setSaveIsVisibly(false);
            initTuYaView();
            addTuYaView();
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //必须要延时一段时间才能画出来
                    pictureEditView.setMode(EditMode.DOODLE);
                }
            }, 500);

//                pictureEditView.setOnPathListener(this);
        } else if (viewid == R.id.item2_rel_3) {
            Log.d("MyTest", "马赛克点击一次");
        } else if (viewid == R.id.item3_page_ok) {
            pictureEditView.saveEdit(new IEditSave() {
                @Override
                public void onSaveSuccess(Bitmap bitmap) {
                    imagebitmap = bitmap;
                    imagebitmap.setDensity(originalbitmap.getDensity());
                    addImageView();
                    initSecondView();
                    changeMainSytle(7);
                }

                @Override
                public void onSaveFailed() {
                }
            });
        } else if (viewid == R.id.item3_page_cancel) {
            initSecondView();
            changeMainSytle(7);
            addImageView();
        } else if (viewid == R.id.item4_page_ok) {
            initFirstView();
            changeMainSytle(6);
        } else if (viewid == R.id.item4_page_cancel) {
            initFirstView();
            changeMainSytle(6);
        } else if (viewid == R.id.edit_back) {
            startActivity(new Intent(ImageCropActivity.this, TestActivity.class));
            finish();
        } else if (viewid == R.id.edit_editok) {
            setEditContainerGone();
            if (cropImageView != null) {
//                imagebitmap = cropImageView.crop();
//                addImageView();
                Bitmap newbitmap=cropImageView.crop();
                if (newbitmap.getWidth()<400){
                    Log.d("MyTest","图片缩放执行一次");
                    //在这里进行图片缩放：
                    imagebitmap=ImageUtils.scale(newbitmap,2.5f,2.5f);
                    addImageView();
                }else {
                    Log.d("MyTest","图片没有执行缩放");
                    imagebitmap = newbitmap;
                    addImageView();
                }

            }
        } else if (viewid == R.id.edit_editrefresh) {
            if (cropImageView != null) {
                cropImageView.setFullImgCrop();
            }
        } else if (viewid == R.id.add_one_text) {
            if (pictureEditView != null) {
                pictureEditView.addStickerText(new StickerText("点击编辑文字", Color.parseColor("#ffffff")), true);
            }
        } else if (viewid == R.id.shuiyin_relative1) {
            addpic(ImageUtils.getBitmap(R.drawable.design_edit_shuiyin));
        } else if (viewid == R.id.shuiyin_relative2) {
            addpic(ImageUtils.getBitmap(R.drawable.design_edit_shuiyin2));
        } else if (viewid == R.id.shuiyin_select) {
            getPictureFromPhoto();
            // 这个是从相册里面选择图片
        } else if (viewid == R.id.edit_save) {

            File receivefile=ImageUtils.save2Album(imagebitmap, Bitmap.CompressFormat.JPEG);
//            EventBus.getDefault().post(new JsCallBackEvent());
            String methodimg=SPUtils.getInstance().getString("method_img");
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
            finish();


        }



    }

    private void addShuiYinView() {
//
        imgShowContainer.removeAllViews();
        operateUtils=new OperateUtils(this);
        operateView = new OperateView(ImageCropActivity.this, imagebitmap);

        int width=0,height=0;
        if (imagebitmap.getWidth()>imgShowContainer.getWidth()){
            width=imgShowContainer.getWidth()-50;
            height=imagebitmap.getHeight()*width/imagebitmap.getWidth();
        }else {
            width=imagebitmap.getWidth();
            height=imagebitmap.getHeight();
        }

        Log.d("MyTest","width :"+width+"    height "+height);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                width, height);   // 这个view的宽高是 背景图片的宽高
        operateView.setLayoutParams(layoutParams);


        imgShowContainer.addView(operateView);
        operateView.setMultiAdd(true); // 设置此参数，可以添加多个图片



    }

    private void addpic(Bitmap bmp)
    {
        // ImageObject imgObject = operateUtils.getImageObject(bmp);
        ImageObject imgObject = operateUtils.getImageObject(bmp, operateView,
                5, 150, 100);  // 这个是一个水印图片
        operateView.addItem(imgObject);  // 这个imgobject 就是表示的是 一个水印，其中有水印图片、旋转图片和x号图片
    }



    private void setEditContainerGone(){
        editcontainerokor.setVisibility(View.GONE);
    }

    private void setEditContainerVisibly(){
        editcontainerokor.setVisibility(View.VISIBLE);
    }


    private void initShuiYinView() {
        frameLayout.removeAllViews();
        View view=getLayoutInflater().inflate(R.layout.item_page4,null,false);  // 问题在于，我需要不断的切换这个界面  ，，我需要切换切换这个界面，每个界面都有独特的元素
        frameLayout.addView(view);
        ImageView item4ok=view.findViewById(R.id.item4_page_ok);
        ImageView item4cancel=view.findViewById(R.id.item4_page_cancel);
        item4ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave();
                initFirstView();
                changeMainSytle(6);
                addImageView();



            }
        });
        item4cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFirstView();
                changeMainSytle(6);
                addImageView();
            }
        });

        //水印图片点击

        RelativeLayout shuiyinrel1=view.findViewById(R.id.shuiyin_relative1);
        RelativeLayout shuiyinrel2=view.findViewById(R.id.shuiyin_relative2);
        RelativeLayout shuiyinselect=view.findViewById(R.id.shuiyin_select);

        shuiyinrel1.setOnClickListener(this);
        shuiyinrel2.setOnClickListener(this);
        shuiyinselect.setOnClickListener(this);



    }



    private void btnSave()
    {
        operateView.save();
        Bitmap bmp = getBitmapByView(operateView);  //从view里面获取图片
        if (bmp != null)
        {
            if (!imagebitmap.isRecycled()){
                imagebitmap.recycle();
            }
          imagebitmap=bmp;
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

    private void initBiaoQianView() {

        frameLayout.removeAllViews();
        View view=getLayoutInflater().inflate(R.layout.item_page3,null,false);  // 问题在于，我需要不断的切换这个界面  ，，我需要切换切换这个界面，每个界面都有独特的元素
        frameLayout.addView(view);

        ImageView item3cancel=view.findViewById(R.id.item3_page_cancel);
        ImageView item3ok=view.findViewById(R.id.item3_page_ok);
        item3ok.setOnClickListener(this);
        item3cancel.setOnClickListener(this);

        TextView textView=view.findViewById(R.id.add_one_text);
        textView.setOnClickListener(this);





    }

    private void initTuYaView(){

        frameLayout.removeAllViews();
        View view=getLayoutInflater().inflate(R.layout.item_page3,null,false);  // 问题在于，我需要不断的切换这个界面  ，，我需要切换切换这个界面，每个界面都有独特的元素
        frameLayout.addView(view);

        ImageView item3cancel=view.findViewById(R.id.item3_page_cancel);
        ImageView item3ok=view.findViewById(R.id.item3_page_ok);
        item3ok.setOnClickListener(this);
        item3cancel.setOnClickListener(this);

        TextView textView=view.findViewById(R.id.add_one_text);
        textView.setText("涂鸦");

        TextView tuyalable=view.findViewById(R.id.tuyalable);
        tuyalable.setVisibility(View.INVISIBLE);


        customColorGroup=findViewById(R.id.cg_colors);
        customColorGroup.setOnCheckedChangeListener(this);


        customColorGroup.setVisibility(View.VISIBLE);
        customColorGroup.setCheckColor(ColorUtils.getColor(R.color.image_color_red));


    }


    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 780;
    }

    @Override
    public void onPathStart() {

    }

    @Override
    public void onPathEnd() {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (pictureEditView!=null){
            pictureEditView.setPenColor(customColorGroup.getCheckColor());
        }


    }

    /* 从相册中获取照片 */
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, PHOTO_PICKED_WITH_DATA);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

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
                addpic(BitmapFactory.decodeFile(photoPath));

                break;
//                Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
//                break;

            default:
                break;
        }

    }








}