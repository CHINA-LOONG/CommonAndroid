package com.loong.common.network.response;


import androidx.annotation.NonNull;

import com.loong.common.bean.BaseBean;
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
public class ResponseTransformer {
    public static ObservableTransformer handleResult() {
        return new ObservableTransformer() {
            @NonNull
            @Override
            public ObservableSource apply(@NonNull Observable upstream) {
                return upstream.onErrorResumeNext(new ErrorResumeFunction()).flatMap(new ResponseFunction());
            }
        };
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     */
    private static class ErrorResumeFunction implements Function<Throwable, ObservableSource<? extends BaseBean>> {

        @Override
        public ObservableSource<? extends BaseBean> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     */
    private static class ResponseFunction<T> implements Function<T, ObservableSource> {

        @Override
        public ObservableSource apply(T tResponse) throws Exception {
            int code = ((BaseBean) tResponse).getStatus();
            String message = ((BaseBean) tResponse).getMsg();
            if (code == 200) {
                return Observable.just(tResponse);
            } else if (code == 401) {
                EventBus.getDefault().post(new LoginEvent(3));
                return Observable.error(new ApiException(code, message));
            } else {
                return Observable.error(new ApiException(code, message));
            }
        }
    }
}
