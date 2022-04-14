package com.loong.common.network.response;

import com.loong.common.bean.BaseBeanT;
import com.loong.common.event.LoginEvent;
import com.loong.common.network.exception.ApiException;
import com.loong.common.network.exception.CustomException;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/*******************************************************************
 * ResponseTransformer.java  2019/5/5
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/
public class ResponseTransformerT {
    public static <T> ObservableTransformer<BaseBeanT<T>, T> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>());
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends BaseBeanT<T>>> {

        @Override
        public ObservableSource<? extends BaseBeanT<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<BaseBeanT<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(BaseBeanT<T> tResponse) throws Exception {
            int code = tResponse.getStatus();
            String message = tResponse.getMsg();
            if (code == 200) {
                if (tResponse.getData() == null) {
                    return (ObservableSource<T>) Observable.just(tResponse.getMsg());
                }
                return Observable.just(tResponse.getData());
            } else if (code == 401) {
                EventBus.getDefault().post(new LoginEvent(3));
                return Observable.error(new ApiException(code, "登录信息已失效"));
            } else {
                return Observable.error(new ApiException(code, message));
            }
        }
    }
}
