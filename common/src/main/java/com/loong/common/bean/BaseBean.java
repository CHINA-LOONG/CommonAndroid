package com.loong.common.bean;

/*******************************************************************
 * BaseBean.java  2019/4/28
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/
public class BaseBean {
    private String code;
    private String message;

    public int getStatus() {
        return Integer.parseInt(code);
    }

    public void setStatus(String code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }
}
