package com.loong.common.network.api;

import com.loong.common.network.NetworkManager;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/10 17:29
 * @desc :
 */
public class BaseApiUtil {
    private static BaseApiUtil mInstance;
    public static BaseApiUtil getInstance(){
        if (mInstance == null){
            synchronized (BaseApiUtil.class){
                if (mInstance == null){
                    mInstance = new BaseApiUtil();
                }
            }
        }
        return mInstance;
    }

    private IHttpApi mHttpApi;

    protected BaseApiUtil(){
        mHttpApi = NetworkManager.getInstance().getRequest(IHttpApi.class);
    }

    public IHttpApi getHttpApi() {
        return mHttpApi;
    }
}
