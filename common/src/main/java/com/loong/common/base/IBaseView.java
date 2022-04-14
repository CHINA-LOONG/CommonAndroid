package com.loong.common.base;

import android.content.Intent;

import io.reactivex.disposables.Disposable;

/**
 * View层基类：iBaseView，定义了activity与Presenter层交互的基础操作
 */

public interface IBaseView {

    //显示竖向加载框
    void showVerticalLoadingDialog(String noticeString, boolean cancelable);

    void dissVerticalLoadingDialog();

    //显示横向加载框
    void showHorizontalLoadingDialog(String noticeString, boolean cancelable);

    void dissHorizontalLoadingDialog();

    void showPopupLoading(String noticeString);
    void dissPopupLoading();

    //显示webView页面加载框
    void showWebLoadingDialog();

    void dissWebLoadingDialog();

    //显示错误信息Toast
    void showErrorMsg(String errorMsg);

    void showErrorOrDefaultMsg(String errorMsg, String defaultMsg);

    //添加Rxjava 订阅
    void addDisposable(Disposable disposable);

    //启动一个activity
    void startSingleActivity(Class<?> cls);
    void startSingleActivity(Intent intent);

    //结束当前页
    void finishActivity();

    //通过ARoute 启动给一个页面
    void aRouteActivity(String path);

    //发送事件
    void sendEvent(Object o);


}
