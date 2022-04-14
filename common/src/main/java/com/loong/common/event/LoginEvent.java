package com.loong.common.event;

/*******************************************************************
 * LoginEvent.java  2019/5/6
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/
public class LoginEvent {
    int type; // 1: 登录；2：退出 3: token失效

    public LoginEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
