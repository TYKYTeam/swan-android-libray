package com.tyky.media.utils;

import android.text.TextUtils;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.tyky.media.bean.DownloadInfo;
import com.tyky.media.bean.DownloadingInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件下载工具类
 */
public class FileDownloadUtil {
    private static volatile FileDownloadUtil instance;
    private volatile OkHttpClient okHttpClient;

    private FileDownloadUtil() {
    }

    /**
     * 获取实例
     */
    public static FileDownloadUtil getInstance() {
        if (null == instance) {
            synchronized (FileDownloadUtil.class) {
                if (instance == null) {
                    instance = new FileDownloadUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化客户端
     */
    private OkHttpClient getOkClient() {
        if (okHttpClient == null) {
            synchronized (FileDownloadUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder().build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * get请求 断点续传
     */
    private Response getRequest(String url, long position) throws IOException {
        OkHttpClient okHttpClient = getOkClient();
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("Range", "bytes=" + position)
                .get()
                .build();
        return okHttpClient.newCall(request).execute();
    }

    /**
     * get请求
     */
    private Response getRequest(String url) throws IOException {
        OkHttpClient okHttpClient = getOkClient();
        Request request = new Request
                .Builder()
                .url(url)
                .get()
                .build();
        return okHttpClient.newCall(request).execute();
    }

    // 获取本地文件长度
    private long getFileLength(File file) {
        long startSize = 0;
        if (file.exists()) {
            startSize = file.length();
        }
        return startSize;
    }

    /**
     * 下载多个文件
     */
    public void downloads(List<DownloadInfo> downloadUrls) {
        for (DownloadInfo downloadUrl : downloadUrls) {
            download(downloadUrl, new OnDownloadListener() {
                @Override
                public void onProgress(double progress) {

                }

                @Override
                public void onError(Exception e) {

                }

                @Override
                public void onSuccess(File outputFile) {

                }
            });
        }
    }

    /**
     * 下载文件
     */
    public Future<?> download(DownloadInfo downloadInfo, OnDownloadListener listener) {
        // 利用通道完成文件的复制(非直接缓冲区)
        Future<?> feature = ThreadUtils.getIoPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = downloadInfo.getUrl();
                    String fileName = downloadInfo.getFileName();
                    File file = getFile(fileName);
                    //续传开始的进度
                    long lastDownloadPosition = getFileLength(file);
                    // 发起请求
                    Response resp = getRequest(url, lastDownloadPosition);
                    if (!TextUtils.isEmpty(resp.header("Content-Range")) && resp.code() != 416) {
                        // 继续下载
                        downloadContinue(resp, file, listener);
                    } else {
                        // 开始下载
                        startDownload(url, file, listener);
                    }
                    if (listener != null) {
                        listener.onSuccess(file);
                    }
                } catch (IOException e) {
                    if (listener != null) {
                        e.printStackTrace();
                        listener.onError(e);
                    }
                }
            }
        });
        return feature;
    }

    /**
     * 断点继续下载
     */
    private void downloadContinue(Response resp, File file, OnDownloadListener listener) throws IOException {
        double remoteLength = Long.parseLong(resp.header("Content-Length")) * 1.0;
        long downloadedLength = getFileLength(file);
        // 如果是断点续传，响应头的Content-Length不是文件的总长度
        // 远程未下载长度加上本地已下载长度为总长度
        double totalLength = (double) downloadedLength + remoteLength;
        //断点继续下载
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        //从上次未完成的位置开始下载
        randomAccessFile.seek(downloadedLength);
        // 获取文件通道
        FileChannel fileChannel = randomAccessFile.getChannel();
        writeRespStream(resp, fileChannel, totalLength, listener);

        // 关闭流
        randomAccessFile.close();
        fileChannel.close();
    }

    /**
     * 开始下载
     */
    public void startDownload(String url, File file, OnDownloadListener listener) throws IOException {
        Response resp = getRequest(url);
        //文件总大小
        double fileAllLength = Long.parseLong(resp.header("Content-Length")) * 1.0;
        if (getFileLength(file) == fileAllLength) {
            //判断文件已下载完
            if (listener != null) {
                listener.onSuccess(file);
            }
            return;
        }
        // 走重新下载的逻辑
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        // 获取文件通道
        FileChannel fileChannel = fileOutputStream.getChannel();
        writeRespStream(resp, fileChannel, fileAllLength, listener);

        // 关闭流
        fileOutputStream.close();
        fileChannel.close();
    }

    /**
     * 读取文件流
     */
    private void writeRespStream(Response resp, FileChannel fileChannel, double fileAllLength, OnDownloadListener listener) throws IOException {
        InputStream respInputStream = resp.body().byteStream();
        ReadableByteChannel resChannel = Channels.newChannel(respInputStream);
        // 通道没有办法传输数据，需依赖缓冲区
        // 分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (resChannel.read(byteBuffer) != -1) {
            // 切换成读数据模式
            byteBuffer.flip();
            // 将缓冲数据写入通道
            fileChannel.write(byteBuffer);
            // 获取读取进度
            double progress = fileChannel.size() / fileAllLength;

            // 进行精确计算
            BigDecimal bigDecimal = new BigDecimal(progress);
            // 舍弃小数部分
            double progressResult = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (listener != null) {
                listener.onProgress(progressResult);
            }
            // 切换到写模式
            byteBuffer.clear();
        }
        resChannel.close();
    }

    /**
     * 文件下载回调接口
     */
    public interface OnDownloadListener {
        void onProgress(double progress);

        void onError(Exception e);

        void onSuccess(File outputFile);
    }

    /**
     * 从url中获取文件名
     */
    public String parseUrlFileName(String url) {
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
        if (fileName.contains("=")) {
            fileName = fileName.substring(fileName.indexOf("=") + 1);
        }
        return fileName;
    }

    /**
     * 获取文件
     */
    public File getFile(String fileName) {
        return new File(PathUtils.getExternalAppFilesPath(), fileName);
    }

}
