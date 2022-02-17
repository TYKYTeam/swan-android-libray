package com.tyky.webviewBase.utils;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;

import java.util.Locale;

public class SpeechService {
    private static TextToSpeech textToSpeech;

    /**
     * 初始化
     *
     * @param activity
     * @return
     */
    public static void init(Activity activity) {
        if (textToSpeech == null) {
            //初始化tts语音
            textToSpeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    // 如果装载TTS引擎成功
                    if (status == TextToSpeech.SUCCESS) {
                        // 设置使用美式英语朗读
                        int result = textToSpeech.setLanguage(Locale.CHINA);
                        // 如果不支持所设置的语言
                        if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                                && result != TextToSpeech.LANG_AVAILABLE) {
                            ToastUtils.showShort("该tts不支持中文");
                        }
                    } else {
                        textToSpeech = null;
                    }
                }
            });
        }
    }

    /**
     * 朗读语音
     *
     * @param text
     */
    public static void speakText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        } else {
            Log.e("test", "语音还未初始化");
        }
    }

    /**
     * 关闭并释放资源
     */
    public static void release() {
        if (textToSpeech != null) {
            // 不管是否正在朗读TTS都被打断
            textToSpeech.stop();
            // 关闭，释放资源
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }
}
