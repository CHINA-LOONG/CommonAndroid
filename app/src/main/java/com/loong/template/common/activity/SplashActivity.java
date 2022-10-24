package com.loong.template.common.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.loong.common.base.presenter.BasePresenterActivity;
import com.loong.common.network.token.TokenCache;
import com.loong.common.utils.UIUtil;
import com.loong.template.common.bean.UserInfoBean;
import com.loong.template.common.persenter.CommonPresenter;
import com.loong.template.common.persenter.CommonPresenter.UserInfoListener;
import com.loong.template.databinding.ActivitySplashBinding;

/**
 * @author : Administrator
 * @e-mail : xuelong9009@qq.com
 * @date : 2022/3/12
 * desc   : APP启动闪屏界面
 * version: v1.0
 */
public class SplashActivity extends BasePresenterActivity<ActivitySplashBinding, CommonPresenter> implements UserInfoListener {

    @Override
    protected void initView() {
        new Handler(getMainLooper()).postDelayed(() -> {
            if (TextUtils.isEmpty(TokenCache.getInstance().getToken())) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finishActivity();
            } else {
                presenter.getUserInfo();
            }
        }, 3000);
    }


    @Override
    protected void setImmersionBar() {
        ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    @Override
    public void onGetInfoSuccess(UserInfoBean bean) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishActivity();
    }

    @Override
    public void onGetInfoError(String error) {
        UIUtil.toast(error);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finishActivity();
    }
}