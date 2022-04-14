package com.loong.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2020/10/225:27 PM
 * desc   :
 * version: 1.0
 */
public class DownloadUtils {

    private static DownloadUtils mInstance;

    public static DownloadUtils getInstance() {
        if (mInstance == null) {
            synchronized (DownloadUtils.class) {
                if (mInstance == null) {
                    mInstance = new DownloadUtils();
                }
            }
        }
        return mInstance;
    }

    private OkHttpClient okHttpClient;
    private Handler mHandler;//所有监听器在 Handler 中被执行,所以可以保证所有监听器在主线程中被执行

    private DownloadUtils() {
        this.mHandler = new Handler(Looper.getMainLooper());
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = ProgressManager.getInstance().with(builder).build();
    }

    public void download(final String url, final String saveDir, final String saveName, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(saveDir, saveName);
                    if (!file.getParentFile().exists())
                        file.getParentFile().mkdirs();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();//强行输出清空缓冲区数据
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDownloadSuccess();
                        }
                    });
                }catch (Exception e){
                    Log.e("下载异常", e.getMessage());
                    listener.onDownloadFailed();
                }finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        ProgressManager.getInstance().addResponseListener(url, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                listener.onDownloading(progressInfo);
            }

            @Override
            public void onError(long id, Exception e) {
                listener.onDownloadFailed();
            }
        });
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(ProgressInfo progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();

    }
}
