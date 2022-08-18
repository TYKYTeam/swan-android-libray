package com.tyky.update.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.ThreadUtils;

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

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloadUtil {

    public static void download(String url, File file, OnDownloadListener listener) {

        //http://10.232.107.44:9060/swan-business/file/download
        // 利用通道完成文件的复制(非直接缓冲区)
        ThreadUtils.getIoPool().submit(new Runnable() {
            @Override
            public void run() {
                try {

                    //续传开始的进度
                    long startSize = 0;
                    if (file.exists()) {
                        startSize = file.length();
                    }
                    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                    Request request = new Request.Builder().url(url)
                            .addHeader("Range", "bytes=" + startSize)
                            .get().build();
                    Call call = okHttpClient.newCall(request);
                    Response resp = call.execute();

                    double length = Long.parseLong(resp.header("Content-Length")) * 1.0;
                    FileChannel foschannel ;

                    if (!TextUtils.isEmpty(resp.header("Content-Range"))) {
                        //断点继续下载
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                        //从上次未完成的位置开始下载
                        randomAccessFile.seek(startSize);
                        randomAccessFile.getChannel();
                        foschannel = randomAccessFile.getChannel();
                        InputStream fis = resp.body().byteStream();
                        ReadableByteChannel fisChannel = Channels.newChannel(fis);

                        // 通道没有办法传输数据，必须依赖缓冲区
                        // 分配指定大小的缓冲区
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                        // 将通道中的数据存入缓冲区中
                        while (fisChannel.read(byteBuffer) != -1) {  // fisChannel 中的数据读到 byteBuffer 缓冲区中
                            byteBuffer.flip();  // 切换成读数据模式
                            // 将缓冲区中的数据写入通道
                            foschannel.write(byteBuffer);

                            final double progress = (foschannel.size() / length);
                            BigDecimal two = new BigDecimal(progress);
                            double result = two.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                            //计算进度，回调
                            if (listener != null) {
                                listener.onProgress(result);
                            }
                            byteBuffer.clear();  // 清空缓冲区
                        }
                        foschannel.close();
                        fisChannel.close();
                        randomAccessFile.close();

                        if (listener != null) {
                            listener.onSuccess(file);
                        }
                    } else {
                        //走重新下载的逻辑
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        foschannel = fileOutputStream.getChannel();

                        InputStream fis = resp.body().byteStream();
                        ReadableByteChannel fisChannel = Channels.newChannel(fis);

                        // 通道没有办法传输数据，必须依赖缓冲区
                        // 分配指定大小的缓冲区
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                        // 将通道中的数据存入缓冲区中
                        while (fisChannel.read(byteBuffer) != -1) {  // fisChannel 中的数据读到 byteBuffer 缓冲区中
                            byteBuffer.flip();  // 切换成读数据模式
                            // 将缓冲区中的数据写入通道
                            foschannel.write(byteBuffer);

                            final double progress = (foschannel.size() / length);
                            BigDecimal two = new BigDecimal(progress);
                            double result = two.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                            //计算进度，回调
                            if (listener != null) {
                                listener.onProgress(result);
                            }
                            byteBuffer.clear();  // 清空缓冲区
                        }
                        foschannel.close();
                        fisChannel.close();
                        fileOutputStream.close();

                        if (listener != null) {
                            listener.onSuccess(file);
                        }
                    }

                } catch (IOException e) {
                    if (listener != null) {
                        listener.onError(e);
                    }

                }
            }
        });


    }

    public interface OnDownloadListener {
        void onProgress(double progress);

        void onError(Exception e);

        void onSuccess(File outputFile);
    }
}
