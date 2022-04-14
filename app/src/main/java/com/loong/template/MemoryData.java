package com.loong.template;

import com.loong.template.common.bean.UserInfoBean;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2021/7/1310:54 上午
 * desc   :
 * version: 1.0
 */
public class MemoryData {

    private static MemoryData mInstance;

    public static MemoryData getInstance(){
        if (mInstance == null){
            synchronized (MemoryData.class){
                if (mInstance == null){
                    mInstance = new MemoryData();
                }
            }
        }
        return mInstance;
    }

    private UserInfoBean userInfo;

    public UserInfoBean getUserInfo() {
        if (userInfo == null) {
            return null;
        }
        return userInfo;
    }
    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

}
