package com.loong.common.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.loong.common.bean.BaseBeanT;
import com.loong.common.bean.TokenBean;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import ikidou.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private static int UNAUTHORIZED = 401;

    private String BASE_URL ="";
    private String REFRESH_URL = "";

    public TokenInterceptor(String baseUrl,String refrshUrl){
        BASE_URL = baseUrl;
        REFRESH_URL = refrshUrl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        //添加Token请求头 这里的token应当是从本地数据库中读取的 **********
        if (chain.request().url().toString().contains("refreshToken")) {
            request.addHeader(TokenCache.HEADER_TOKEN_JAVA, TokenCache.getInstance().getRefreshToken());
            request.addHeader(TokenCache.HEADER_TOKEN_PHP, TokenCache.getInstance().getRefreshToken());
        } else {
            request.addHeader(TokenCache.HEADER_TOKEN_JAVA, TokenCache.getInstance().getToken());
            request.addHeader(TokenCache.HEADER_TOKEN_PHP, TokenCache.getInstance().getToken());
        }
        Response proceed = chain.proceed(request.build());
        //如果token过期 再去重新请求token 然后设置token的请求头 重新发起请求 用户无感
        if (isTokenExpired(proceed)){
            String newHeaderToken = getNewToken();
            if (TextUtils.isEmpty(newHeaderToken)){
                return proceed;
            }else {
                //使用新的Token，创建新的请求
                Request newRequest = chain.request().newBuilder()
                        .addHeader(TokenCache.HEADER_TOKEN_JAVA, newHeaderToken)
                        .addHeader(TokenCache.HEADER_TOKEN_PHP, newHeaderToken)
                        .build();
                proceed.close();
                return chain.proceed(newRequest);
            }
        }
        return proceed;
    }

    /**
     * 检查token是否过期
     * @param response 消息返回体
     * @return 监测结果 true过期
     */
    private boolean isTokenExpired(Response response){
        if (response.code() == UNAUTHORIZED){
            return true;
        }
        return false;
    }

    /**
     * 同步获取token
     * @return 获取的新token
     * @throws IOException
     */
    private synchronized String getNewToken()throws IOException{
        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .header(TokenCache.HEADER_TOKEN_JAVA,TokenCache.getInstance().getRefreshToken())
                .header(TokenCache.HEADER_TOKEN_PHP,TokenCache.getInstance().getRefreshToken())
                .url(BASE_URL+"/psdog/api/system/refreshToken")
                .build();
        Call call =  okHttpClient.newCall(request);
        String string = Objects.requireNonNull(call.execute().body()).string();
        Type type = new TypeToken<BaseBeanT<TokenBean>>(){}.getType();
        BaseBeanT<TokenBean> data = new Gson().fromJson(string,type);
        if (data.getStatus()==200){
            TokenCache.getInstance().setToken(data.getData().getToken());
            TokenCache.getInstance().setRefreshToken(data.getData().getRefreshToken());
            return data.getData().getToken();
        }
        return null;
    }

}
