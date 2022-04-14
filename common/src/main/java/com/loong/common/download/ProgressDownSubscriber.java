package com.loong.common.download;

import android.net.wifi.aware.SubscribeConfig;
import android.os.Handler;

import com.loong.common.download.DownloadListener.DownloadProgressListener;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.SubscriberCompletableObserver;
import io.reactivex.internal.subscribers.SubscriberResourceWrapper;
import io.reactivex.subscribers.ResourceSubscriber;

public class ProgressDownSubscriber<T> implements DownloadProgressListener, Observer<T> {

    private SoftReference<DownloadOnNextListener> mSubscriberOnNextListener;

    /*下载数据*/
    private DownInfo downInfo;
    private Handler handler;



    public ProgressDownSubscriber(DownInfo downInfo, Handler handler) {
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo = downInfo;
        this.handler = handler;
    }


    public void setDownInfo(DownInfo downInfo){
        this.mSubscriberOnNextListener = new SoftReference<>(downInfo.getListener());
        this.downInfo = downInfo;
    }

    @Override
    public void update(long read, long count, boolean done) {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
