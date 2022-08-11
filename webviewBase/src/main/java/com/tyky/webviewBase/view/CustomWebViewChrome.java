package com.tyky.webviewBase.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.blankj.utilcode.util.ResourceUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tyky.webviewBase.constants.RequestCodeConstants;
import com.tyky.webviewBase.event.UrlLoadFinishEvent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

public class CustomWebViewChrome extends WebChromeClient {

    private Activity currrentActivity;

    public CustomWebViewChrome(WebView webView) {
        Context context = webView.getContext();
        if (context instanceof Activity) {
            currrentActivity = (Activity) context;
        }

    }

    private ValueCallback<Uri[]> filePathCallback;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        this.filePathCallback = filePathCallback;
        String[] acceptTypes = fileChooserParams.getAcceptTypes();

        String acceptType = "*/*";
        StringBuilder sb = new StringBuilder();
        if (acceptTypes.length > 0) {
            for (String type : acceptTypes) {
                sb.append(type).append(';');
            }
        }
        if (sb.length() > 0) {
            String typeStr = sb.toString();
            acceptType = typeStr.substring(0, typeStr.length() - 1);
        }

        final String tempType = acceptType;
        //权限检查
        if (AndPermission.hasPermissions(currrentActivity, Permission.Group.STORAGE, Permission.Group.STORAGE)) {
            showChooseDialog(tempType);
        } else {
            AndPermission.with(currrentActivity).runtime()
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(permissions -> {
                        showChooseDialog(tempType);
                    })
                    .onDenied(permission -> {
                        Toast.makeText(currrentActivity, "拒绝权限,无法进行上传文件...", Toast.LENGTH_SHORT).show();
                        //拒绝权限也得加个回调
                        filePathCallback.onReceiveValue(null);
                    }).start();
        }
        return true;
    }

    private boolean isClickDialog = false;

    /**
     * 展示选择方式的对话框
     */
    private void showChooseDialog(String acceptType) {
        if (TextUtils.isEmpty(acceptType) || "*/*".equals(acceptType)) {
            String[] items = new String[]{"选择图片/拍照", "选择视频/录制视频", "选择音频/录制音频", "选择文件"};//创建item
            //添加列表
            AlertDialog alertDialog = new AlertDialog.Builder(currrentActivity)
                    .setTitle("选择方式")
                    .setIcon(ResourceUtils.getDrawableIdByName("ic_launcher"))
                    .setItems(items, (dialogInterface, i) -> {
                        isClickDialog = true;
                        chooseFileFromWay(i);
                    })
                    .create();
            alertDialog.setOnCancelListener(dialogInterface -> {
                if (!isClickDialog) {
                    filePathCallback.onReceiveValue(null);
                } else {
                    isClickDialog = false;
                }
            });
            alertDialog.show();

        }
        if (acceptType.contains("image")) {
            chooseFileFromWay(0);
        }
        if (acceptType.contains("video")) {
            chooseFileFromWay(1);
        }
        if (acceptType.contains("audio")) {
            chooseFileFromWay(2);
        }
        if (acceptType.contains("file")) {
            chooseFileFromWay(3);
        }
    }

    /**
     * @param type 0:选择图片/拍照 1:选择视频/录制视频 2:选择音频/录音 3:选择文件
     */
    private void chooseFileFromWay(int type) {
        if (type == 0) {
            PictureSelector.create(currrentActivity)
                    .openGallery(PictureMimeType.ofImage())
                    .isCamera(true)
                    .isPreviewImage(true)
                    .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                    .forResult(RequestCodeConstants.PHOTO);
        }
        if (type == 1) {
            PictureSelector.create(currrentActivity)
                    .openGallery(PictureMimeType.ofVideo())
                    .isPreviewVideo(true)
                    .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                    .forResult(RequestCodeConstants.VIDEO);
        }
        if (type == 2) {
            PictureSelector.create(currrentActivity)
                    .openGallery(PictureMimeType.ofAudio())
                    .isEnablePreviewAudio(true)
                    .imageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                    .forResult(RequestCodeConstants.AUDIO);
        }
        if (type == 3) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            currrentActivity.startActivityForResult(intent, RequestCodeConstants.FILE);
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress >= 100) {
            EventBus.getDefault().post(new UrlLoadFinishEvent());
        }
        super.onProgressChanged(view, newProgress);
    }

    /**
     * 处理返回的数据,触发h5回调,回传文件数据给页面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void handleDataFromIntent(int requestCode, int resultCode, Intent data) {
        isClickDialog = false;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode >= RequestCodeConstants.PHOTO && requestCode <= RequestCodeConstants.AUDIO) {
                List<LocalMedia> result = PictureSelector.obtainMultipleResult(data);
                Uri[] uriArray = new Uri[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    LocalMedia localMedia = result.get(i);
                    String path = localMedia.getPath();
                    Uri uri = Uri.fromFile(new File(path));
                    uriArray[i] = uri;
                }
                if (result.isEmpty()) {
                    filePathCallback.onReceiveValue(new Uri[]{});
                } else {
                    filePathCallback.onReceiveValue(uriArray);
                }
                return;
            }
            if (requestCode == RequestCodeConstants.FILE) {
                Uri data1 = data.getData();
                if (data1 == null) {
                    filePathCallback.onReceiveValue(new Uri[]{});
                } else {
                    Uri[] uriArray = new Uri[]{data1};
                    filePathCallback.onReceiveValue(uriArray);
                }
                return;
            }
        }
        //可能为空，在选择音频文件可能会出现
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(new Uri[]{});
        }
    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        request.grant(request.getResources());
    }
}
