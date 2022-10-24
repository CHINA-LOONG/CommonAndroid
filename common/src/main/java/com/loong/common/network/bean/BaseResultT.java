package com.loong.common.network.bean;

import com.loong.common.bean.BaseBean;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/10 14:35
 * @desc :
 */
public class BaseResultT<T> extends BaseResult {
    private T result;
    private String timestamp;

    public boolean isSuccess() {
        return getCode()==1;
    }

    public T getData() {
        return result;
    }

    public void setData(T data) {
        this.result = data;
    }

    public String getTime() {
        return timestamp;
    }
}
