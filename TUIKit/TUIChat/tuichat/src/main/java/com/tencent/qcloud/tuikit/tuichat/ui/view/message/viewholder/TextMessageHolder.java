package com.tencent.qcloud.tuikit.tuichat.ui.view.message.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuicore.TUIThemeManager;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatService;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TextMessageBean;
import com.tencent.qcloud.tuikit.tuichat.component.face.FaceManager;
import com.tencent.qcloud.tuikit.tuichat.ui.view.message.SelectTextHelper;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;

public class TextMessageHolder extends MessageContentHolder {

    private TextView msgBodyText;
    private SelectTextHelper mSelectableTextHelper;
    private String selectedText;

    private final Runnable mShowSelectViewRunnable =
            () -> mSelectableTextHelper.reset();

    public TextMessageHolder(View itemView) {
        super(itemView);
        msgBodyText = itemView.findViewById(R.id.msg_body_tv);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_text;
    }

    public void resetSelectableText() {
        //msgBodyText.callOnClick();
        msgBodyText.postDelayed(mShowSelectViewRunnable, 120);
    }

    @Override
    public void layoutVariableViews(TUIMessageBean msg, int position) {
        if (!(msg instanceof TextMessageBean)) {
            return;
        }
        TextMessageBean textMessageBean = (TextMessageBean) msg;

        if (!textMessageBean.isSelf()) {
            int otherTextColorResId = TUIThemeManager.getAttrResId(msgBodyText.getContext(), R.attr.chat_other_msg_text_color);
            int otherTextColor = msgBodyText.getResources().getColor(otherTextColorResId);
            msgBodyText.setTextColor(otherTextColor);
        } else {
            int selfTextColorResId = TUIThemeManager.getAttrResId(msgBodyText.getContext(), R.attr.chat_self_msg_text_color);
            int selfTextColor = msgBodyText.getResources().getColor(selfTextColorResId);
            msgBodyText.setTextColor(selfTextColor);
        }

        boolean isEmoji = false;
        msgBodyText.setVisibility(View.VISIBLE);
        if (textMessageBean.getText() != null) {
            isEmoji = FaceManager.handlerEmojiText(msgBodyText, textMessageBean.getText(), false);
        } else if (!TextUtils.isEmpty(textMessageBean.getExtra())) {
            isEmoji = FaceManager.handlerEmojiText(msgBodyText, textMessageBean.getExtra(), false);
        } else {
            isEmoji = FaceManager.handlerEmojiText(msgBodyText, TUIChatService.getAppContext().getString(R.string.no_support_msg), false);
        }
        if (properties.getChatContextFontSize() != 0) {
            msgBodyText.setTextSize(properties.getChatContextFontSize());
        }
        if (textMessageBean.isSelf()) {
            if (properties.getRightChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getRightChatContentFontColor());
            }
        } else {
            if (properties.getLeftChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
            }
        }

        msgContentFrame.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //mSelectableTextHelper.selectAll();
                    return true;
                }
            });

        mSelectableTextHelper = new SelectTextHelper
                .Builder(msgBodyText)// ?????????textView???????????????
                .setCursorHandleColor(TUIChatService.getAppContext().getResources().getColor(R.color.font_blue))// ????????????
                .setCursorHandleSizeInDp(22)// ???????????? ??????dp
                .setSelectedColor(TUIChatService.getAppContext().getResources().getColor(R.color.test_blue))// ?????????????????????
                .setSelectAll(true)// ???????????????????????? default true
                .setIsEmoji(isEmoji)
                .setScrollShow(false)// ??????????????????????????? default true
                .setSelectedAllNoPop(true)// ???????????????????????????????????????????????? onSelectAllShowCustomPop ??????
                .setMagnifierShow(false)// ????????? default true
                .build();

        mSelectableTextHelper.setSelectListener(new SelectTextHelper.OnSelectListener() {
            /**
             * ????????????
             */
            @Override
            public void onClick(View v) {
                //clickTextView(textView.getText().toString().trim());
            }

            /**
             * ????????????
             */
            @Override
            public void onLongClick(View v) {
                //postShowCustomPop(SHOW_DELAY);
            }

            /**
             * ??????????????????
             */
            @Override
            public void onTextSelected(CharSequence content) {
                selectedText = content.toString();
                textMessageBean.setSelectText(selectedText);
                TUIChatLog.d("TextMessageHolder", "onTextSelected selectedText = " + selectedText);
                if (onItemClickListener != null) {
                    onItemClickListener.onTextSelected(msgContentFrame, position, msg);
                }
            }

            /**
             * ??????????????????
             */
            @Override
            public void onDismiss() {
            }

            /**
             * ??????TextView??????url??????
             *
             * ??????????????????
             * textView.setMovementMethod(new LinkMovementMethodInterceptor());
             */
            @Override
            public void onClickUrl(String url) {
                //toast("????????????  " + url);
            }

            /**
             * ?????????????????????????????????
             */
            @Override
            public void onSelectAllShowCustomPop() {
                //postShowCustomPop(SHOW_DELAY);
            }

            /**
             * ????????????
             */
            @Override
            public void onReset() {
                //SelectTextEventBus.getDefault().dispatch(new SelectTextEvent("dismissOperatePop"));
            }

            /**
             * ???????????????????????????
             */
            @Override
            public void onDismissCustomPop() {
                //SelectTextEventBus.getDefault().dispatch(new SelectTextEvent("dismissOperatePop"));
            }

            /**
             * ????????????????????????
             */
            @Override
            public void onScrolling() {
                //removeShowSelectView();
            }
        });
    }

}
