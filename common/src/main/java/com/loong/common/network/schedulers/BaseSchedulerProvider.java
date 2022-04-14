package com.loong.common.network.schedulers;

import androidx.annotation.NonNull;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

/*******************************************************************
 * BaseSchedulerProvider.java  2019/5/5
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/
public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

    @NonNull
    <T> ObservableTransformer<T, T> applySchedulers();
}
