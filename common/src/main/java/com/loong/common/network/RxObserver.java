package com.loong.common.network;

import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.loong.common.base.IBaseView;
import com.loong.common.network.exception.ApiException;
import com.loong.common.network.response.DisPlayDialogInfo;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static com.loong.common.network.exception.CustomException.DATA_PRASE_ERRORMSG;
import static com.loong.common.network.exception.CustomException.INTERNAL_ERROR_COMMON_ERRORMSG;
import static com.loong.common.network.exception.CustomException.INTERNAL_SERVER_ERROR;
import static com.loong.common.network.exception.CustomException.INTERNAL_SERVER_ERROR_ERRORMSG;
import static com.loong.common.network.exception.CustomException.NETWORK_ERROR;
import static com.loong.common.network.exception.CustomException.NOT_FOUND;
import static com.loong.common.network.exception.CustomException.NOT_FOUND_ERRORMSG;
import static com.loong.common.network.exception.CustomException.PARSE_ERROR;
import static com.loong.common.network.exception.CustomException.ResultException;
import static com.loong.common.network.exception.CustomException.UNKNOWN;
import static com.loong.common.network.exception.CustomException.UNKNOWN_ERRORMSG;
import static com.loong.common.network.response.DisPlayDialogInfo.horizontal_type;
import static com.loong.common.network.response.DisPlayDialogInfo.vertical_type;
import static com.loong.common.network.response.DisPlayDialogInfo.web_type;


/**
 * 重写Subscriber，处理网络请求过程
 */
public abstract class RxObserver<T> implements Observer<T> {


    private IBaseView mView;
    private DisPlayDialogInfo mDisPlayDialogInfo;

    public RxObserver(DisPlayDialogInfo disPlayDialogInfo, IBaseView baseView) {
        mDisPlayDialogInfo = disPlayDialogInfo;
        this.mView = baseView;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mView != null) {
            mView.addDisposable(d);
            showNetLoadingDialog();
        }
    }

    @Override
    public void onNext(T value) {
        if (mView != null) {
            dissNetLoadingDialog();
        }
        onSuccessDate(value);
    }

    @Override
    public void onError(Throwable e) {

        if (mView != null) {
            dissNetLoadingDialog();
        }
        ApiException exception = null;

        if (e instanceof ApiException) {
            exception = (ApiException) e;

            if (((ApiException) e).getCode() == PARSE_ERROR) {
                //解析错误
            } else if (NOT_FOUND == exception.getCode()) { //404错误
                if (mView != null) {
                    mView.showErrorMsg(NOT_FOUND_ERRORMSG);
                }
            } else if (INTERNAL_SERVER_ERROR == exception.getCode()) { // 500错误
                if (mView != null) {
                    mView.showErrorMsg(INTERNAL_SERVER_ERROR_ERRORMSG);
                }
            } else if (NETWORK_ERROR == exception.getCode()) { // 其他网络错误
                if (mView != null) {
                    mView.showErrorMsg(INTERNAL_ERROR_COMMON_ERRORMSG);
                }
            } else if (ResultException == exception.getCode()) { //服务器定义错误
                if (mView != null) {
                    if (!TextUtils.isEmpty(exception.getDisplayMessage())) {
                        mView.showErrorMsg(exception.getDisplayMessage());
                    }
                }
            } else if (UNKNOWN == exception.getCode()) { //未知异常

            } else { // 其他错误

            }


        } else {
            if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {
                //解析错误
                exception = new ApiException(PARSE_ERROR, e.getMessage(), DATA_PRASE_ERRORMSG);

            } else if (e instanceof HttpException) {             //HTTP错误
                HttpException httpException = (HttpException) e;
                if (NOT_FOUND == httpException.code()) {
                    exception = new ApiException(NOT_FOUND, e.getMessage(), NOT_FOUND_ERRORMSG);
                } else if (INTERNAL_SERVER_ERROR == httpException.code()) {
                    exception = new ApiException(INTERNAL_SERVER_ERROR, e.getMessage(), INTERNAL_SERVER_ERROR_ERRORMSG);

                } else {//其他都视为网络错误
                    exception = new ApiException(NETWORK_ERROR, e.getMessage(), INTERNAL_ERROR_COMMON_ERRORMSG);
                }

            } else if (e instanceof ConnectException) {
                //网络错误
                exception = new ApiException(NETWORK_ERROR, e.getMessage(), INTERNAL_ERROR_COMMON_ERRORMSG);

            } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                //连接错误
                exception = new ApiException(NETWORK_ERROR, e.getMessage(), INTERNAL_ERROR_COMMON_ERRORMSG);

            } else {
                //未知错误
                exception = new ApiException(UNKNOWN, e.getMessage(), UNKNOWN_ERRORMSG);
            }

        }


        //将错误通过接口暴露出去，进行当数据获取出错时的操作，如果只需要提示下错误信息，该步骤可省略
        onErrorData(exception);

    }

    @Override
    public void onComplete() {
        if (mView != null) {
            dissNetLoadingDialog();
        }

    }


    protected abstract void onSuccessDate(T t);

    protected abstract void onErrorData(ApiException e);

    private void showNetLoadingDialog(){
        if (mDisPlayDialogInfo != null) {
            if (mDisPlayDialogInfo.getmBaseDialog() != null) {
                mDisPlayDialogInfo.getmBaseDialog().showDialog();
            } else if (vertical_type.equals(mDisPlayDialogInfo.getmDialogType())) { // 竖直的加载框
                if (mDisPlayDialogInfo.isShowDialog()) {
                    mView.showVerticalLoadingDialog(mDisPlayDialogInfo.getmNoticeMessageStr(), mDisPlayDialogInfo.isCanDialogCancel());
                }

            } else if (horizontal_type.equals(mDisPlayDialogInfo.getmDialogType())) { // 水平的加载框
                if (mDisPlayDialogInfo.isShowDialog()) {
                    mView.showHorizontalLoadingDialog(mDisPlayDialogInfo.getmNoticeMessageStr(), mDisPlayDialogInfo.isCanDialogCancel());
                }

            } else if (web_type.equals(mDisPlayDialogInfo.getmDialogType())) { // 圆环进度条的加载框
                if (mDisPlayDialogInfo.isShowDialog()) {
                    mView.showWebLoadingDialog();
                }
            }
        }
    }

    private void dissNetLoadingDialog() {
        if (mDisPlayDialogInfo != null) {
            if (mDisPlayDialogInfo.getmBaseDialog() != null) {
                mDisPlayDialogInfo.getmBaseDialog().dissDialog();
            } else if (vertical_type.equals(mDisPlayDialogInfo.getmDialogType())) { // 竖直的加载框
                if (mDisPlayDialogInfo.isShowDialog()) {
                    mView.dissVerticalLoadingDialog();
                }

            } else if (horizontal_type.equals(mDisPlayDialogInfo.getmDialogType())) { // 水平的加载框
                if (mDisPlayDialogInfo.isShowDialog()) {
                    mView.dissHorizontalLoadingDialog();
                }

            } else if (web_type.equals(mDisPlayDialogInfo.getmDialogType())) { // 圆环进度条的加载框
                if (mDisPlayDialogInfo.isShowDialog()) {
                    mView.dissWebLoadingDialog();
                }
            }
        }
    }

}
