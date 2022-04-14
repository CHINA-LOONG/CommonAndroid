package com.loong.template.api;


import com.loong.common.network.NetworkManager;

public class ApiUtil {

    private static ApiUtil mInstance;
    public static ApiUtil getInstance(){
        if (mInstance == null){
            synchronized (ApiUtil.class){
                if (mInstance == null){
                    mInstance = new ApiUtil();
                }
            }
        }
        return mInstance;
    }

    private Api mApi;

    protected ApiUtil(){
        mApi = NetworkManager.getInstance().getRequest(Api.class);
    }

    public Api getApi() {
        return mApi;
    }


    public static void Clean(){
        mInstance = null;
    }
}
