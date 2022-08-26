package com.vachel.editor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vachel.editor.ui.PictureEditView;
import com.vachel.editor.ui.widget.ColorGroup;
import com.vachel.editor.ui.widget.ProgressDialog;
import com.vachel.editor.ui.widget.StickerImageDialog;
import com.vachel.editor.ui.widget.TextEditDialog;
import com.vachel.editor.util.BitmapUtil;
import com.vachel.editor.util.Utils;
import com.vachel.editor.bean.StickerText;

import java.lang.ref.WeakReference;

public class PictureEditActivity extends AppCompatActivity implements View.OnClickListener,
        TextEditDialog.ITextChangedListener,
        RadioGroup.OnCheckedChangeListener,
        DialogInterface.OnShowListener,
        DialogInterface.OnDismissListener,
        IEditSave,
        PictureEditView.IOnPathListener {
    public static final String EXTRA_IMAGE_URI = "image_uri";
    public static final String RESULT_IMAGE_SAVE_PATH = "result_image_save_path";

    protected PictureEditView mPictureEditView;
    private RadioGroup mModeGroup;
    private ColorGroup mColorGroup;
    private TextEditDialog mTextDialog;
    public StickerImageDialog mStickerImageDialog;
    private View mLayoutOpSub;
    private ViewSwitcher mOpSwitcher, mOpSubSwitcher;

    public static final int OP_HIDE = -1;
    public static final int OP_NORMAL = 0;
    public static final int OP_CLIP = 1;

    public static final int OP_SUB_DOODLE = 0;
    public static final int OP_SUB_MOSAIC = 1;

    private View mUnDoView;
    private ProgressDialog mWaitDialog;
    public boolean mSupportEmoji = false;

    public static Bitmap imagebitmap=null;
    public static MyAddTextListener staticlistener=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_edit_activity);
        mWaitDialog = new ProgressDialog(this).bindLifeCycle(this);  // 这个是等待对话框
        mWaitDialog.show();
        onBitmapLoad(imagebitmap);

    }

     public static void setBitmap(Bitmap bitmap, Activity activity,MyAddTextListener listener){
        if (imagebitmap!=null){
            imagebitmap=null;
        }
        imagebitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
        staticlistener=listener;
        Intent intent=new Intent(activity,PictureEditActivity.class);
        activity.startActivity(intent);
     }


    private void onBitmapLoad(@NonNull Bitmap bitmap) {
        Utils.dismissDialog(mWaitDialog);   // 对话框工具，表示此时取消了加载动画
        initData();  // 这个为空
        initViews();
        mPictureEditView.setImageBitmap(bitmap);
    }

    public void initData() {

    }

    private void initViews() {
        mPictureEditView = findViewById(R.id.image_canvas);  // 这个view是 PictureEditView
        mModeGroup = findViewById(R.id.rg_modes);  // rg_modes 这个表示的是 底部的功能选择，如画笔，裁剪等。
        mOpSwitcher = findViewById(R.id.vs_op);  // 这个表示的是 主页面的switcher组件， 这也是一个 充满全屏的
        mOpSubSwitcher = findViewById(R.id.vs_op_sub);  // 这个表示的是 上部分的 颜色选择switcher
        mColorGroup = findViewById(R.id.cg_colors);  // 这个表示的是 上部分的 颜色选择switcher内的颜色group
        mColorGroup.setCheckColor(PictureEditor.getInstance().getDefaultCheckedColor(this));
        mColorGroup.setOnCheckedChangeListener(this);
        mLayoutOpSub = findViewById(R.id.layout_op_sub);  // 这个表示的是颜色内容
        mUnDoView = findViewById(R.id.btn_undo);  // 这个表示的是撤回button

        TextView tvDone = findViewById(R.id.tv_done);  // 这个表示的是 完成按钮
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(PictureEditor.getInstance().getBtnColor(this));
        gd.setCornerRadius(Utils.dip2px(this, 4));
        tvDone.setBackground(gd);
        TextView clipDone = findViewById(R.id.ib_clip_done);  // 这个表示的是 裁剪完成 按钮
        clipDone.setBackground(gd);
        if (mSupportEmoji) {
            findViewById(R.id.sticker_img_space).setVisibility(View.VISIBLE);
            View stickerImgBtn = findViewById(R.id.sticker_img);
            stickerImgBtn.setVisibility(View.VISIBLE);
            stickerImgBtn.setOnClickListener(this);
        }

        mPictureEditView.setOnPathListener(this);  // 这个表示的是 设置绘制路径的监听器。当开始绘制的时候，调用一次，结束绘制的时候调用一次
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.rb_doodle) {
            onModeClick(EditMode.DOODLE);
        } else if (vid == R.id.btn_text) {
            Log.d("MyTest","执行一次 but_text");
            onTextModeClick();
        } else if (vid == R.id.btn_clip) {
            onModeClick(EditMode.CLIP);
        } else if (vid == R.id.btn_undo) {
            onUndoClick();
        } else if (vid == R.id.tv_done) {
            mPictureEditView.saveEdit(this);
        } else if (vid == R.id.tv_cancel) {
            onCancelClick();
        } else if (vid == R.id.ib_clip_cancel) {
            onCancelClipClick();
        } else if (vid == R.id.ib_clip_done) {
            onDoneClipClick();
        } else if (vid == R.id.tv_clip_reset) {
            onResetClipClick();
        } else if (vid == R.id.ib_clip_rotate) {
            onRotateClipClick();
        } else if (vid == R.id.sticker_img) {
            onStickImgClick();
        }
    }

    public void onStickImgClick() {
        StickerImageDialog stickerImageDialog = getStickerImageDialog();
        if (stickerImageDialog == null) {
            return;
        }
        if (stickerImageDialog.isShowing()) {
            stickerImageDialog.dismiss();
        } else {
            stickerImageDialog.show();
        }
    }

    private StickerImageDialog getStickerImageDialog() {
        if (mStickerImageDialog == null) {
            View stickerLayout = getStickerLayout();
            if (stickerLayout == null) {
                return null;
            }
            mStickerImageDialog = new StickerImageDialog(this, stickerLayout);
        }
        return mStickerImageDialog;
    }

    public void updateModeUI() {
        EditMode mode = mPictureEditView.getMode();
        switch (mode) {
            case DOODLE:
                mModeGroup.check(R.id.rb_doodle);
                setOpSubDisplay(OP_SUB_DOODLE);
                break;
            case MOSAIC:
                setOpSubDisplay(OP_SUB_MOSAIC);
                break;
            case NONE:
                mModeGroup.clearCheck();
                mColorGroup.setCheckColor(PictureEditor.getInstance().getDefaultCheckedColor(this));
                setOpSubDisplay(OP_HIDE);
                break;
        }
    }

    public void onTextModeClick() {
        if (mTextDialog == null) {
            mTextDialog = new TextEditDialog(this, this);
            mTextDialog.setOnShowListener(this);
            mTextDialog.setOnDismissListener(this);
        }
        mTextDialog.show();
    }

    public void enableUndo(boolean enable) {
        mUnDoView.setEnabled(enable);
    }

    @Override
    public final void onCheckedChanged(RadioGroup group, int checkedId) {
        onColorChanged(mColorGroup.getCheckColor());
    }

    public void setOpDisplay(int op) {
        if (op >= 0) {
            mOpSwitcher.setDisplayedChild(op);
        }
    }

    public void setOpSubDisplay(int opSub) {
        if (opSub < 0) {
            mLayoutOpSub.setVisibility(View.GONE);
        } else {
            mOpSubSwitcher.setDisplayedChild(opSub);
            mLayoutOpSub.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPathStart() {  // 当开始绘制的时候，就会隐藏功能界面
        mOpSwitcher.setVisibility(View.GONE);
        Log.d("MyTest","onPathStart");
    }

    @Override
    public void onPathEnd() {
        mOpSwitcher.setVisibility(View.VISIBLE);
        Log.d("MyTest","onPathEnd");
    }

    @Override
    public void onShow(DialogInterface dialog) {
        mOpSwitcher.setVisibility(View.GONE);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mOpSwitcher.setVisibility(View.VISIBLE);
    }

    public Bitmap getBitmap() {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        Uri uri = intent.getParcelableExtra(EXTRA_IMAGE_URI);
        if (uri == null) {
            return null;
        }
        return BitmapUtil.getBitmapFromUri(getApplicationContext(), uri,
                Utils.getScreenWidth(this) / 2,
                Utils.getScreenHeight(this) / 2);
    }

    @Override
    public void onText(StickerText text, boolean enableEdit) {
        mPictureEditView.addStickerText(text, enableEdit);
    }

    public void onModeClick(EditMode mode) {
        EditMode cm = mPictureEditView.getMode();
        if (cm == mode) {
            mode = EditMode.NONE;
        } else if (mode == EditMode.DOODLE && (cm == EditMode.DOODLE || cm == EditMode.MOSAIC)) {
            mode = EditMode.NONE;
        }
        mPictureEditView.setMode(mode);
        updateModeUI();

        if (mode == EditMode.CLIP) {
            setOpDisplay(OP_CLIP);
        }
    }

    public void onUndoClick() {
        mPictureEditView.undo();
    }

    public void onCancelClick() {
        finish();
    }

    @Override
    public void onSaveSuccess(Bitmap bitmap) {
        staticlistener.setBitmap(bitmap);
        finish();
    }

    @Override
    public void onSaveFailed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onCancelClipClick() {
        mPictureEditView.cancelClip();
        setOpDisplay(mPictureEditView.getMode() == EditMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    public void onDoneClipClick() {
        mPictureEditView.doClip();
        setOpDisplay(mPictureEditView.getMode() == EditMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    public void onResetClipClick() {
        mPictureEditView.resetClip();
    }

    public void onRotateClipClick() {
        mPictureEditView.doRotate();
    }

    public void onColorChanged(int checkedColor) {
        mPictureEditView.setPenColor(checkedColor);
    }

    public View getStickerLayout() {
        return null;
    }
}
