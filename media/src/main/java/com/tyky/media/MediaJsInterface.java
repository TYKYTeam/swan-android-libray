package com.tyky.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;

import com.annimon.stream.Stream;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.tyky.media.activity.FilePreviewActivity;
import com.tyky.media.activity.QrScanActivity;
import com.tyky.media.bean.DownloadInfo;
import com.tyky.media.bean.MediaRequestModel;
import com.tyky.media.bean.MyContacts;
import com.tyky.media.utils.FileDownloadUtil;
import com.tyky.media.utils.FileUtils;
import com.tyky.webviewBase.annotation.WebViewInterface;
import com.tyky.webviewBase.event.ImagePreviewEvent;
import com.tyky.webviewBase.event.IntentEvent;
import com.tyky.webviewBase.event.JsCallBackEvent;
import com.tyky.webviewBase.model.ResultModel;
import com.tyky.webviewBase.utils.SpeechService;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


@WebViewInterface("android_media")
public class MediaJsInterface {

    Gson gson = GsonUtils.getGson();

    /**
     * 复制文本到剪切板
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String copyTextToClipboard(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        ClipboardUtils.copyText(content);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 获取剪切板文本
     */
    @JavascriptInterface
    public String getTextFromClipboard() {
        String result = ClipboardUtils.getText().toString();
        return gson.toJson(ResultModel.success(result));
    }

    /**
     * 打电话
     *
     * @param paramStr
     */
    @SuppressLint("MissingPermission")
    @JavascriptInterface
    public String callPhone(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String phone = paramModel.getPhone();
        if (StringUtils.isEmpty(phone)) {
            return gson.toJson(ResultModel.errorParam());
        }
        Activity topActivity = ActivityUtils.getTopActivity();
        String callPhonePermission = Permission.CALL_PHONE;
        AndPermission.with(topActivity)
                .runtime()
                .permission(callPhonePermission)
                .onDenied(null)
                .onGranted(data -> {
                    PhoneUtils.call(phone);
                })
                .start();

        //ClipboardUtils.copyText(phone);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 打电话
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String goToCall(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String content = paramModel.getPhone();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        PhoneUtils.dial(content);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 发送短信
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String sendSms(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String content = paramModel.getContent();
        String phone = paramModel.getPhone();
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(phone)) {
            return gson.toJson(ResultModel.errorParam());
        }
        PhoneUtils.sendSms(phone, content);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 预览单个图片
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String previewPicture(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String content = paramModel.getContent();
        int type = paramModel.getType();
        if (type == 0 || StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam(""));
        }
        EventBus.getDefault().post(new ImagePreviewEvent(content, type));
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 二维码扫描
     *
     * @param paramStr
     * @return
     */
    @JavascriptInterface
    public String qrScan(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String callbackMethod = paramModel.getCallBackMethod();
        if (callbackMethod == null) {
            return gson.toJson(ResultModel.errorParam());
        }

        IntentEvent intentEvent = new IntentEvent(QrScanActivity.class, callbackMethod);
        EventBus.getDefault().post(intentEvent);

        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 文字转语音
     *
     * @param paramStr
     */
    @JavascriptInterface
    public String speakText(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String content = paramModel.getContent();
        if (StringUtils.isEmpty(content)) {
            return gson.toJson(ResultModel.errorParam());
        }
        SpeechService.speakText(content);
        return gson.toJson(ResultModel.success(""));
    }

    @JavascriptInterface
    public String testCallBack(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String callbackMethod = paramModel.getCallBackMethod();
        if (callbackMethod == null) {
            return gson.toJson(ResultModel.errorParam());
        }
        EventBus.getDefault().post(new JsCallBackEvent(callbackMethod, "测试。。"));
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 获取手机通讯录
     */
    @JavascriptInterface
    public String getContacts(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String callbackMethod = paramModel.getCallBackMethod();
        if (callbackMethod == null) {
            return gson.toJson(ResultModel.errorParam());
        }
        List<MyContacts> contacts = new ArrayList<>();
        if (PermissionUtils.isGranted(Permission.READ_CONTACTS)) {
            contacts.addAll(getContact());
            //使用回调js代码回传数据，保证为同步
            EventBus.getDefault().post(new JsCallBackEvent(callbackMethod, contacts));
        } else {
            PermissionUtils.permission(Permission.READ_CONTACTS).callback(new PermissionUtils.SingleCallback() {

                @Override
                public void callback(boolean isAllGranted, @NonNull List<String> granted, @NonNull List<String> deniedForever, @NonNull List<String> denied) {
                    if (isAllGranted) {
                        contacts.addAll(getContact());
                        EventBus.getDefault().post(new JsCallBackEvent(callbackMethod, contacts));
                    }
                }
            }).request();
        }
        return gson.toJson(ResultModel.success(""));
    }

    private List<MyContacts> getContact() {
        List<MyContacts> contacts = new ArrayList<>();
        Activity topActivity = ActivityUtils.getTopActivity();
        Cursor cursor = topActivity.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //新建一个联系人实例
            MyContacts temp = new MyContacts();
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            temp.setName(name);
            //temp.setUserId(contactId);

            //获取联系人电话号码
            Cursor phoneCursor = topActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);

            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replace("-", "");
                phone = phone.replace(" ", "");
                temp.setNumber(phone);
            }

            //获取联系人备注信息
            Cursor noteCursor = topActivity.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Nickname.NAME},
                    ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "'",
                    new String[]{contactId}, null);
            if (noteCursor.moveToFirst()) {
                do {
                    String note = noteCursor.getString(noteCursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
//                    temp.note = note;
                    Log.i("note:", note);
                } while (noteCursor.moveToNext());
            }
            contacts.add(temp);
            //记得要把cursor给close掉
            phoneCursor.close();
            noteCursor.close();
        }
        cursor.close();
        return contacts;
    }

    /**
     * 预览文件
     */
    @JavascriptInterface
    public String previewFile(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        DownloadInfo downloadInfo = paramModel.getPreviewFileInfo();
        Bundle bundle = new Bundle();
        String url = "";
        String fileName = "";
        if (downloadInfo == null) {
            url = paramModel.getDownloadUrl();
            fileName = FileUtils.parseUrlFileName(url);
            downloadInfo = new DownloadInfo();
            downloadInfo.setUrl(url);
            downloadInfo.setName(fileName);

        } else  {
            url = downloadInfo.getUrl();
            fileName = downloadInfo.getName();
            if (TextUtils.isEmpty(fileName)) {
                // 从url中截取fileName
                downloadInfo.setName(FileUtils.parseUrlFileName(url));
            }
        }
        bundle.putSerializable(FilePreviewActivity.FILE_INFO, downloadInfo);

        ActivityUtils.startActivity(bundle, FilePreviewActivity.class);
        return gson.toJson(ResultModel.success(""));
    }

    /**
     * 多文件下载
     */
    @JavascriptInterface
    public String downloadFiles(String paramStr) {
        MediaRequestModel paramModel = gson.fromJson(paramStr, MediaRequestModel.class);
        String[] downloadUrls = paramModel.getDownloadUrls();

        DownloadInfo[] downloadInfos = paramModel.getDownloadInfos();
        String callBackMethod = paramModel.getCallBackMethod();

        if (TextUtils.isEmpty(callBackMethod)) {
            return gson.toJson(ResultModel.errorParam("未传回调函数"));
        }

        List<DownloadInfo> infos = new ArrayList<>();

        if (downloadUrls != null && downloadUrls.length > 0) {
            Stream.of(downloadUrls).forEach(url -> {
                DownloadInfo downloadInfo = new DownloadInfo();
                String fileName = FileUtils.parseUrlFileName(url);
                downloadInfo.setName(fileName);
                downloadInfo.setUrl(url);
                infos.add(downloadInfo);
            });
        } else if (downloadInfos != null && downloadInfos.length > 0){
            Stream.of(downloadInfos).forEach(downloadInfo -> {
                if (TextUtils.isEmpty(downloadInfo.getName())) {
                    downloadInfo.setName(FileUtils.parseUrlFileName(downloadInfo.getUrl()));
                }
                infos.add(downloadInfo);
            });
        } else {
            return gson.toJson(ResultModel.errorParam("未传文件url相关信息"));
        }

        // 异步下载
        FileDownloadUtil.getInstance().downloads(infos, callBackMethod);
        return gson.toJson(ResultModel.success(""));
    }
}
