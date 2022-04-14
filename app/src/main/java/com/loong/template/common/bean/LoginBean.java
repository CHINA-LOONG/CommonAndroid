package com.loong.template.common.bean;

/**
 *    @author : Administrator
 *    @e-mail : xuelong9009@qq.com
 *    @date   : 2022/3/12
 *    desc   : 数据类-登录
 *    version: v1.0
 */
public class LoginBean {
    /**
     * userinfo
     */
    private UserInfoBean userinfo;
    /**
     * token
     */
    private String token;
    /**
     * refreshToken
     */
    private String refreshToken;

    public UserInfoBean getUserInfo() {
        return userinfo;
    }

    public void setUserInfo(UserInfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
