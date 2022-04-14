package com.loong.template;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/10 11:31
 * @desc : 常数列表
 */
public class Constants {

    public static String SECRET_KEY = "6329318863293188";

    // 服务器的基础域名
    public static String BASE_URL = BuildConfig.BASE_URL;

    // Token过期刷新的接口
    public static final String REFRESH_URL = "/api/mod/user/refreshToken";

    // 用户协议URL
    public static final String USER_AGREEMENT = BuildConfig.BASE_URL + "/api/mod/web/userAgreement";
    // 隐私协议URL
    public static final String PRIVACY_AGREEMENT = BuildConfig.BASE_URL + "/api/mod/web/privacyAgreement";
}
