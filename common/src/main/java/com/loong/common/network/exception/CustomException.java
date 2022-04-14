package com.loong.common.network.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.loong.common.event.LoginEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/*******************************************************************
 * CustomException.java  2019/5/5
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/
public class CustomException {

    //常见的HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;//请求的资源并不存在
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    public static final int PARSE_ERROR = 1001;//json解析数据错误
    public static final int NETWORK_ERROR = 1002;//通常的网络错误
    public static final int UNKNOWN = 1003;//其他错误
    public static final int ResultException = 100;//服务器定义错误

    //默认的序偶无信息提示
    public static final String NOT_FOUND_ERRORMSG = "404访问资源不存在，请联系管理员！";
    public static final String INTERNAL_SERVER_ERROR_ERRORMSG = "500系统错误，请稍后重试！";
    public static final String INTERNAL_ERROR_COMMON_ERRORMSG = "网络错误！";
    public static final String DATA_PRASE_ERRORMSG = "数据解析错误！";
    public static final String UNKNOWN_ERRORMSG = "其他错误！";

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //解析错误
            ex = new ApiException(PARSE_ERROR, "数据解析异常");
            return ex;
        } else if (e instanceof ConnectException) {
            //网络错误
            ex = new ApiException(NETWORK_ERROR, "网络连接失败，请稍后重试");
            return ex;
        } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
            //连接错误
            ex = new ApiException(NETWORK_ERROR, "网络连接失败，请稍后重试");
            return ex;
        } else {
            //未知错误
            if (e instanceof HttpException) {
                HttpException exception = (HttpException) e;
                if (UNAUTHORIZED==exception.code()){
                    ex = new ApiException(((HttpException) e).code(),"登录信息已失效");
                    EventBus.getDefault().post(new LoginEvent(3));
                }else {
                    ex=new ApiException(UNKNOWN, e.getMessage());
                }
            }else {

                ex = new ApiException(UNKNOWN, e.getMessage());
            }
            return ex;
        }
    }
}
