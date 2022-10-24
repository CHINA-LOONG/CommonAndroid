package com.loong.common.network.bean;

public class BaseTokenResult extends BaseResultT<BaseToken> implements ITokenResult {

    @Override
    public String getToken() {
        return getData().getToken();
    }

    @Override
    public String getRefreshToken() {
        return getData().getRefreshToken();
    }
}
