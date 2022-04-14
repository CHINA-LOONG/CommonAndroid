package com.loong.template.api;

import com.loong.common.bean.BaseBean;
import com.loong.common.bean.BaseBeanT;
import com.loong.template.common.bean.FeedbackTypeBean;
import com.loong.template.common.bean.LoginBean;
import com.loong.template.common.bean.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    /**
     * 获取用户信息
     * @return
     */
    @POST("api/mod/user/getUserInfo")
    Observable<BaseBeanT<UserInfoBean>> getUserInfo();

    /**
     * 获取手机验证码
     * @param phone （加密）手机号
     * @param type  类型：1.未注册用户(注册/修改的新手机) 2.已注册用户(忘记密码/修改的旧手机)
     * @return
     */
    @FormUrlEncoded
    @POST("api/mod/user/getVerificationCode")
    Observable<BaseBeanT<String>> getVCode(@Field("phone") String phone,
                                           @Field("type") int type);


    /**
     * 用户登录
     * @param phone    手机号
     * @param password  密码
     * @return
     */
    @FormUrlEncoded
    @POST("api/mod/user/login")
    Observable<BaseBeanT<LoginBean>> login(@Field("phone") String phone,
                                           @Field("pwd") String password);



    /**
     * 退出登录
     * @return
     */
    @POST("api/mod/user/logout")
    Observable<BaseBean> logout();

    /**
     * 忘记密码
     * @param phone 手机号
     * @param vcode 验证码
     * @param password  密码
     * @return
     */
    @FormUrlEncoded
    @POST("api/mod/user/resetPwd")
    Observable<BaseBean> forgetPwd(@Field("phone") String phone,
                                   @Field("vcode") String vcode,
                                   @Field("pwd") String password);

    /**
     * 修改密码
     * @param password  旧密码
     * @param newPassword   新密码
     * @return
     */
    @FormUrlEncoded
    @POST("api/mod/user/editPwd")
    Observable<BaseBean> modifyPwd(@Field("oldPwd") String password,
                                   @Field("newPwd") String newPassword);

    /**
     * 获取意见反馈类型
     * @return
     */
    @POST("api/mod/my/getFeedbackType")
    Observable<List<FeedbackTypeBean>> getFeedbackType();


    /**
     * 提交意见反馈
     * @param feedbackType  反馈类型
     * @param content   反馈内容
     * @param images    反馈图片
     * @param contact   联系方式
     * @return
     */
    @FormUrlEncoded
    @POST(" /api/mod/my/subFeedback")
    Observable<BaseBean> subFeedback(@Field("feedbackType") String feedbackType,
                                   @Field("content") String content,
                                   @Field("images") String images,
                                   @Field("contact") String contact);
}
