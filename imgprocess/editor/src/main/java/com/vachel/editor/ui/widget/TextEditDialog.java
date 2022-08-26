package com.vachel.editor.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vachel.editor.PictureEditor;
import com.vachel.editor.util.Utils;
import com.vachel.editor.R;
import com.vachel.editor.bean.StickerText;

public class TextEditDialog extends Dialog implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

    private EditText mEditText;

    private final ITextChangedListener mTextListener;

    private StickerText mDefaultText;

    private ColorGroup mColorGroup;
    private View mEnableDrawBg;
    private int mCurrentColor;  //表示当前选择的颜色

    public TextEditDialog(Context context, ITextChangedListener ITextChangedListener) {  // 这个表示的是文本监听器
        super(context, R.style.TextEditDialog);
        setContentView(R.layout.edit_text_dialog);
        mTextListener = ITextChangedListener;
        Window window = getWindow();
        if (window != null) {
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mColorGroup = findViewById(R.id.cg_colors);  // 这个表示的是 多个颜色
        mColorGroup.setOnCheckedChangeListener(this);
        mEditText = findViewById(R.id.et_text);  // 这个表示的是 文字编辑框
        mEnableDrawBg = findViewById(R.id.enable_bg_btn);  // 这个表示的是文字加粗按钮
        mEditText.requestFocus();  // 请求焦点

        findViewById(R.id.tv_cancel).setOnClickListener(this);  // 这个表示的是 取消按钮
        TextView tvDone = findViewById(R.id.tv_done);  // 这个表示的是确认按钮
        tvDone.setOnClickListener(this);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(PictureEditor.getInstance().getBtnColor(getContext()));
        gd.setCornerRadius(Utils.dip2px(getContext(), 4));
        tvDone.setBackground(gd);
        mEnableDrawBg.setOnClickListener(this);

        GradientDrawable editGd = new GradientDrawable();
        editGd.setColor(Color.TRANSPARENT);
        editGd.setCornerRadius(Utils.dip2px(getContext(), 10));
        mEditText.setBackground(editGd);

        findViewById(R.id.root_dialog).setOnTouchListener(this);  // 这个是整个界面的根布局，设置一个监听器
    }

    private void enableDrawBg(boolean enable) {
        GradientDrawable myGrad = (GradientDrawable) mEditText.getBackground();  // 这个是背景
        if (enable) {
            boolean isWhite = mCurrentColor == Color.WHITE;
            mEditText.setTextColor(isWhite ? Color.BLACK : Color.WHITE);
            myGrad.setColor(mCurrentColor);
        } else {
            mEditText.setTextColor(mCurrentColor);  // 设置当前的文字颜色
            myGrad.setColor(Color.TRANSPARENT);  // 同时将背景设置为透明
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mDefaultText != null) {
            mEditText.setText(mDefaultText.getText());
            mCurrentColor = mDefaultText.getColor();
            if (!mDefaultText.isEmpty()) {
                mEditText.setSelection(mEditText.length());
            }
            mEnableDrawBg.setSelected(mDefaultText.isDrawBackground());
            enableDrawBg(mDefaultText.isDrawBackground());
            mDefaultText = null;
        } else {
            mEditText.setText("");
            mCurrentColor = mColorGroup.getCheckColor();
            mEnableDrawBg.setSelected(false);
            enableDrawBg(false);
            showKeyboard();
        }
        mColorGroup.setCheckColor(mEditText.getCurrentTextColor());
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            mEditText.requestFocus();
            imm.showSoftInput(mEditText, 0);
        }
    }

    public void setText(StickerText text) {
        mDefaultText = text;
    }

    public void reset() {
        setText(new StickerText(null, Color.WHITE));
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.tv_done) {
            onDone();
        } else if (vid == R.id.tv_cancel) {
            dismiss();
        } else if (vid == R.id.enable_bg_btn) {
            boolean enable = !v.isSelected();
            v.setSelected(enable);
            enableDrawBg(enable);
        }
    }

    private void onDone() {
        String text = mEditText.getText().toString();  // 获取文本内容
        if (!TextUtils.isEmpty(text) && mTextListener != null) {  //
            mTextListener.onText(new StickerText(text, mCurrentColor, mEnableDrawBg.isSelected()), true);
        }
        dismiss();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mCurrentColor = mColorGroup.getCheckColor();
        enableDrawBg(mEnableDrawBg.isSelected());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        showKeyboard();
        return false;
    }

    public interface ITextChangedListener {
        void onText(StickerText text, boolean enableEdit);
    }
}
