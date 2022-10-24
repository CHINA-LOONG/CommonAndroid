package com.loong.template;

import com.loong.common.application.BaseApp;
import com.loong.common.network.NetworkManager;
import com.loong.common.network.bean.BaseTokenResult;
import com.loong.common.network.token.TokenInterceptorT;
import com.tencent.bugly.crashreport.CrashReport;

public class APP extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();

        // bugly初始化
        CrashReport.initCrashReport(getApplicationContext(),"ba9e4b14b6",false);
        // 网络框架初始化
        NetworkManager.getInstance().init(Constants.BASE_URL,Constants.REFRESH_URL);

//        NetworkManager.getInstance().init(Constants.BASE_URL);
//        NetworkManager.getInstance().init(Constants.BASE_URL,new TokenInterceptorT<BaseTokenResult>(Constants.BASE_URL,Constants.REFRESH_URL));
    }
}
