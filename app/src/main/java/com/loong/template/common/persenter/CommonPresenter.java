package com.loong.template.common.persenter;

import com.loong.common.base.IBaseView;
import com.loong.common.base.presenter.BasePresenter;
import com.loong.common.bean.BaseBean;
import com.loong.common.network.RxObserver;
import com.loong.common.network.TokenCache;
import com.loong.common.network.exception.ApiException;
import com.loong.common.network.response.DisPlayDialogInfo;
import com.loong.common.network.response.ResponseTransformer;
import com.loong.common.network.response.ResponseTransformerT;
import com.loong.common.network.schedulers.SchedulerProvider;
import com.loong.common.utils.PasswordUtil;
import com.loong.common.utils.UIUtil;
import com.loong.common.utils.Utils;
import com.loong.template.Constants;
import com.loong.template.MemoryData;
import com.loong.template.api.ApiUtil;
import com.loong.template.common.bean.LoginBean;
import com.loong.template.common.bean.UserInfoBean;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class CommonPresenter extends BasePresenter<IBaseView> {
    //1.未注册用户 2.已注册用户
    public static final int NEW_PHONE = 0b0_0001;
    public static final int OLD_PHONE = 0b0_0010;

    public interface VCodeInterface {
        void onVCodeSuccess(int type, String msg);

        void onVCodeError(int type, String error);
    }

    public void getVcode(String phone, int type) {
        if (mView instanceof VCodeInterface) {
            try {
                // xuelong 2021/12/3 进行加密
                String encryptPhone = PasswordUtil.encrypt2(phone, Constants.SECRET_KEY);
                ApiUtil.getInstance().getApi().getVCode(encryptPhone, type)
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(ResponseTransformerT.handleResult())
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .subscribe(new RxObserver<String>(new DisPlayDialogInfo(), mView) {
                            @Override
                            protected void onSuccessDate(String string) {
                                ((VCodeInterface) mView).onVCodeSuccess(type, string);
                            }

                            @Override
                            protected void onErrorData(ApiException e) {
                                ((VCodeInterface) mView).onVCodeError(type, e.getDisplayMessage());
                            }
                        });
            }catch (Exception e){
                UIUtil.toast("手机号加密失败");
                return;
            }

        }
    }

    public interface UserInfoListener {
        void onGetInfoSuccess(UserInfoBean bean);

        void onGetInfoError(String error);
    }

    public void getUserInfo() {
        if (mView instanceof UserInfoListener) {
            ApiUtil.getInstance().getApi().getUserInfo()
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(ResponseTransformerT.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new RxObserver<UserInfoBean>(new DisPlayDialogInfo(), mView) {
                        @Override
                        protected void onSuccessDate(UserInfoBean userInfoBean) {
                            MemoryData.getInstance().setUserInfo(userInfoBean);
                            ((UserInfoListener) mView).onGetInfoSuccess(userInfoBean);
                        }

                        @Override
                        protected void onErrorData(ApiException e) {
                            ((UserInfoListener) mView).onGetInfoError(e.getDisplayMessage());
                        }
                    });
        }
    }

    public interface LoginInterface {
        void onLoginSuccess();

        void onLoginError(String error);
    }

    public void login(String phone, String password) {
        if (mView instanceof LoginInterface) {
            ApiUtil.getInstance().getApi().login(phone, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(ResponseTransformerT.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new RxObserver<LoginBean>(new DisPlayDialogInfo(), mView) {
                        @Override
                        protected void onSuccessDate(LoginBean loginBean) {
                            TokenCache.getInstance().setToken(loginBean.getToken());
                            TokenCache.getInstance().setRefreshToken(loginBean.getRefreshToken());
                            ((LoginInterface) mView).onLoginSuccess();
                        }

                        @Override
                        protected void onErrorData(ApiException e) {
                            ((LoginInterface) mView).onLoginError(e.getDisplayMessage());
                        }
                    });
        }
    }

    public interface ForgetPwdInterface {
        void onResetSuccess(String msg);

        void onResetError(String error);
    }

    public void forgetPwd(String phone, String vcode, String password) {
        if (mView instanceof ForgetPwdInterface) {
            ApiUtil.getInstance().getApi().forgetPwd(phone, vcode, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new RxObserver<BaseBean>(new DisPlayDialogInfo(), mView) {
                        @Override
                        protected void onSuccessDate(BaseBean baseBean) {
                            ((ForgetPwdInterface) mView).onResetSuccess(baseBean.getMsg());
                        }

                        @Override
                        protected void onErrorData(ApiException e) {
                            ((ForgetPwdInterface) mView).onResetError(e.getDisplayMessage());
                        }
                    });
        }
    }

    public interface ModifyPwdInterface {
        void onModifySuccess(String msg);

        void onModifyError(String error);
    }

    public void modifyPwd(String password, String newPassword) {
        if (mView instanceof ModifyPwdInterface) {
            ApiUtil.getInstance().getApi().modifyPwd(password, newPassword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new RxObserver<BaseBean>(new DisPlayDialogInfo(), mView) {
                        @Override
                        protected void onSuccessDate(BaseBean baseBean) {
                            ((ModifyPwdInterface) mView).onModifySuccess(baseBean.getMsg());
                        }

                        @Override
                        protected void onErrorData(ApiException e) {
                            ((ModifyPwdInterface) mView).onModifyError(e.getDisplayMessage());
                        }
                    });
        }
    }


}
