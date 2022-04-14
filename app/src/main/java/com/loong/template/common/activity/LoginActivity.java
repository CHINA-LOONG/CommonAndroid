package com.loong.template.common.activity;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.loong.common.activity.WebViewActivity;
import com.loong.common.base.IBaseView;
import com.loong.common.base.presenter.BasePresenterActivity;
import com.loong.common.listener.NoRepeatClickListener;
import com.loong.common.update.UpdateUtils;
import com.loong.common.utils.APPUtils;
import com.loong.common.utils.UIUtil;
import com.loong.template.Constants;
import com.loong.template.common.bean.UserInfoBean;
import com.loong.template.common.persenter.CommonPresenter;
import com.loong.template.databinding.ActivityLoginBinding;

/**
 * @author : Administrator
 * @e-mail : xuelong9009@qq.com
 * @date : 2022/3/12
 * desc   : 登录界面
 * version: v1.0
 */
public class LoginActivity extends BasePresenterActivity<ActivityLoginBinding, CommonPresenter> implements CommonPresenter.LoginInterface, CommonPresenter.UserInfoListener {

    @Override
    protected void initView() {
        UpdateUtils.getInstance().autoUpdate(this);

        // xuelong 2021/11/8 用户协议
        binding.tvUserAgent.setOnClickListener(new NoRepeatClickListener() {
            @Override
            public void onNoRepeatClick(View view) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("title", "用户协议");
                intent.putExtra("url", Constants.USER_AGREEMENT);
                startActivity(intent);
            }
        });
        // xuelong 2021/11/8 隐私协议
        binding.tvSecretAgent.setOnClickListener(new NoRepeatClickListener() {
            @Override
            public void onNoRepeatClick(View view) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("title", "隐私协议");
                intent.putExtra("url", Constants.PRIVACY_AGREEMENT);
                startActivity(intent);
            }
        });

        // 2021/11/25 忘记密码
        binding.tvForget.setOnClickListener(new NoRepeatClickListener() {
            @Override
            public void onNoRepeatClick(View view) {
                startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
            }
        });

        // 2021/11/25 密码的显示隐藏
        binding.cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        binding.btnSubmit.setOnClickListener(new NoRepeatClickListener() {
            @Override
            public void onNoRepeatClick(View view) {
                // 2021/11/24 是否合法
                if (APPUtils.validatorLogin(binding.etPhone, binding.etPassword, binding.cbAgree)) {
                    presenter.login(binding.etPhone.getText().toString(), binding.etPassword.getText().toString());
                }
            }
        });
    }

    @Override
    protected void setImmersionBar() {
        ImmersionBar.with(this)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    @Override
    public void onGetInfoSuccess(UserInfoBean bean) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        finishActivity();
    }

    @Override
    public void onGetInfoError(String error) {
        UIUtil.toast(error);
    }

    @Override
    public void onLoginSuccess() {
        presenter.getUserInfo();
    }

    @Override
    public void onLoginError(String error) {
        UIUtil.toast(error);
    }
}