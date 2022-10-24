package com.loong.common.network.token;

import com.loong.common.utils.SPUtils;

public class TokenCache {
    public static final String HEADER_TOKEN_JAVA = "X-Access-Token";
    public static final String HEADER_TOKEN_PHP = "token";

    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String APP_TOKEN = "APP_TOKEN";

    private static TokenCache mInstance;

    public static TokenCache getInstance() {
        if (mInstance == null) {
            synchronized (TokenCache.class) {
                if (mInstance == null) {
                    mInstance = new TokenCache();
                }
            }
        }
        return mInstance;
    }

    private String mToken;
    private String mRefreshToken;

    public void setRefreshToken(String token) {
        mRefreshToken = token;
        if (mRefreshToken == null) {
            SPUtils.getInstance().remove(REFRESH_TOKEN);
        } else {
            SPUtils.getInstance().put(REFRESH_TOKEN, mRefreshToken);
        }
    }

    public String getRefreshToken() {
        if (mRefreshToken == null) {
            mRefreshToken = SPUtils.getInstance().getString(REFRESH_TOKEN);
        }
        return mRefreshToken;
    }


    public void setToken(String token) {
        mToken = token;
        if (mToken == null) {
            SPUtils.getInstance().remove(APP_TOKEN);
        } else {
            SPUtils.getInstance().put(APP_TOKEN, mToken);
        }
    }

    public String getToken() {
        if (mToken == null) {
            mToken = SPUtils.getInstance().getString(APP_TOKEN);
        }
        return mToken;
    }
}
