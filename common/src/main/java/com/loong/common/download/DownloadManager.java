package com.loong.common.download;

import android.os.Handler;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.loong.common.download.DownloadListener.DownloadInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadManager {
    /*记录下载数据*/
    private Set<DownInfo> downInfos;
//    /*回调sub队列*/
//    private HashMap<String, ProgressDownSubscriber> subMap;
//    /*单利对象*/
//    private volatile static HttpDownManager INSTANCE;
//    /*数据库类*/
//    private DbDwonUtil db;
    /*控制下载进度回掉到主线程*/
    private Handler handler;


    private static DownloadManager mInstance;

    public static DownloadManager getInstance(){
        if (mInstance == null){
            synchronized (DownloadManager.class){
                if (mInstance == null){
                    mInstance = new DownloadManager();
                }
            }
        }
        return mInstance;
    }

    private DownloadManager(){
        downInfos = new HashSet<>();
//        subMap = new HashMap<>();
    }

    /**
     * 开始下载
     * @param info
     */
    public void startDown(final DownInfo info){
//        if(info==null||subMap.get(info.getUrl())!=null){
//            subMap.get(info.getUrl()).setDownInfo(info);
//            return;
//        }

        IDownApi downApi;
        if (downInfos.contains(info)){
            downApi = info.getService();
        }else {
            /*添加回调处理类*/
            ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info, handler);
            /*记录回调sub*/
            DownloadInterceptor interceptor = new DownloadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(getBasUrl(info.getUrl()))
                    .build();

            downApi = retrofit.create(IDownApi.class);
            info.setService(downApi);
            downInfos.add(info);
        }

        downApi.download("bytes=" + info.getReadLength() + "-", info.getUrl())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, DownInfo>() {
                    @Override
                    public DownInfo apply(ResponseBody responseBody) throws Exception {

                        writeCaches(responseBody, new File(info.getSavePath()), info);
                        return info;
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(bean -> {
                    Log.e("----","onNext");
                    if (info.getListener()!=null)
                        info.getListener().onNext(bean);
                }, throwable -> {
                    if (info.getListener()!=null)
                        info.getListener().onError(throwable);
                    remove(info);
                },()->{
                    Log.e("----","下载完成");
                    if (info.getListener()!=null)
                        info.getListener().onComplete();
                    remove(info);
                    info.setState(DownState.FINISH);
                });
    }

    /**
     * 停止下载
     * @param info
     */
    public void stopDown(DownInfo info){
        if (info==null) return;

    }

    /**
     * 暂停下载
     * @param info
     */
    public void pauseDown(DownInfo info){

    }

    /**
     * 移除下载数据
     *
     * @param info
     */
    public void remove(DownInfo info) {
//        subMap.remove(info.getUrl());
        downInfos.remove(info);
    }

    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCaches(ResponseBody responseBody, File file, DownInfo info) {
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;

            Log.e("----","下载写入");
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                long allLength = 0 == info.getCountLength() ? responseBody.contentLength() : info.getReadLength() + responseBody
                        .contentLength();

                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                        info.getReadLength(), allLength - info.getReadLength());
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, len);
                }
            } catch (IOException e) {
                throw new HttpTimeException(HttpTimeException.HTTP_DOWN_WRITE, e.getMessage());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (IOException e) {
            throw new HttpTimeException(HttpTimeException.HTTP_DOWN_WRITE,e.getMessage());
        }
    }


    /**
     * 读取baseurl
     *
     * @param url
     * @return
     */
    public static String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        Log.e("-----",head+"    "+url);
        return head + url;
    }
}
