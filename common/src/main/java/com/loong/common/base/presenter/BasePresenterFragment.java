package com.loong.common.base.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.loong.common.base.BaseFragment;
import com.loong.common.base.IBaseView;
import com.loong.common.widgets.dialog.loading.LoadingHorizontalDialog;
import com.loong.common.widgets.dialog.loading.LoadingVerticalDialog;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2021/7/63:20 下午
 * desc   :
 * version: 1.0
 */
public abstract class BasePresenterFragment<T extends ViewBinding,P extends BasePresenter> extends BaseFragment<T> implements IBaseView {

    public P presenter;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();//Rxjava取消订阅

    private String mLoadingStr = "加载中...";
    private LoadingVerticalDialog mLoadingVerticalDialog;//白色边框，竖向布局的加载中弹窗
    private LoadingHorizontalDialog mLoadingHorizontalDialog;//白色边框，横向布局的加载中弹窗

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        // 创建presenter对象
        Type superclass = getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) superclass).getActualTypeArguments();
        Class<?> aClass = (Class<?>)types[1];
        try {
            presenter = (P) aClass.newInstance();
            presenter.attachView(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();//解除绑定view
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();//取消订阅

        dissPopupLoading();
        dissVerticalLoadingDialog();
        dissHorizontalLoadingDialog();
        dissWebLoadingDialog();

        super.onDestroy();
    }






    //region 实现接口IBaseView
    @Override
    public void showHorizontalLoadingDialog(String noticeString, boolean cancelable) {
        dissHorizontalLoadingDialog();
        mLoadingHorizontalDialog = LoadingHorizontalDialog.Builder(_mActivity)
                .setLoadingText(TextUtils.isEmpty(noticeString) ? noticeString : mLoadingStr)
                .setIsCancelable(cancelable)
                .build();

        if (mLoadingHorizontalDialog != null && !mLoadingHorizontalDialog.isShowing()) {
            mLoadingHorizontalDialog.shown();
        }
    }

    @Override
    public void dissHorizontalLoadingDialog() {
        if (mLoadingHorizontalDialog != null && mLoadingHorizontalDialog.isShowing()) {
            mLoadingHorizontalDialog.dismiss();
        }
    }

    @Override
    public void showVerticalLoadingDialog(String noticeString, boolean cancelable) {
        dissVerticalLoadingDialog();
        mLoadingVerticalDialog = LoadingVerticalDialog.Builder(_mActivity)
                .setLoadingText(TextUtils.isEmpty(noticeString) ? noticeString : mLoadingStr)
                .setIsCancelable(cancelable)
                .build();
        if (mLoadingVerticalDialog != null && !mLoadingVerticalDialog.isShowing()) {
            mLoadingVerticalDialog.shown();
        }
    }

    @Override
    public void dissVerticalLoadingDialog() {
        if (mLoadingVerticalDialog != null && mLoadingVerticalDialog.isShowing()) {
            mLoadingVerticalDialog.dismiss();
        }
    }

    @Override
    public void showPopupLoading(String noticeString) {

    }

    @Override
    public void dissPopupLoading() {

    }

    @Override
    public void showWebLoadingDialog() {

    }

    @Override
    public void dissWebLoadingDialog() {

    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }

    @Override
    public void showErrorOrDefaultMsg(String errorMsg, String defaultMsg) {

    }

    @Override
    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void startSingleActivity(Class<?> cls) {

    }

    @Override
    public void startSingleActivity(Intent intent) {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void aRouteActivity(String path) {

    }

    @Override
    public void sendEvent(Object o) {

    }

    //endregion
}
