package com.loong.common.base.presenter;

//Presenter基类，关联View
public class BasePresenter<T> {

    protected T mView;

    public void attachView(T mvpView) {
        this.mView = mvpView;
    }

    public void detachView() {
        mView = null;
    }
}
