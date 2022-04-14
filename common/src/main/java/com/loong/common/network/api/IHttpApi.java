package com.loong.common.network.api;

import com.loong.common.bean.BaseBeanT;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/10 12:11
 * @desc : 网络请求接口
 */
public interface IHttpApi {

    @GET(HttpConstant.getGitHub)
    Observable<BaseBeanT<String>> getGitHubStar();

    @FormUrlEncoded
    @POST(HttpConstant.getLogin)
    Observable<BaseBeanT<String>> getLogin(@Field("username") String username);



}
