package com.loong.common.event;

/*******************************************************************
 * WechatLoginEvent.java  2019-06-10
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/
public class WechatLoginEvent {
    public UserInfo userInfo;

    public WechatLoginEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
