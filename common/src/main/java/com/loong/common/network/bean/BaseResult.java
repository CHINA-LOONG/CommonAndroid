package com.loong.common.network.bean;

public class BaseResult implements IResult {
    private String code;
    private String message;

    public int getCode() {
        return Integer.parseInt(code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }
}
