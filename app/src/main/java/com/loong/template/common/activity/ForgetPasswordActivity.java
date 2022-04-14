package com.loong.template.common.activity;

import android.os.CountDownTimer;
import android.view.View;

import com.loong.common.base.presenter.BasePresenterActivity;
import com.loong.common.listener.NoRepeatClickListener;
import com.loong.common.utils.APPUtils;
import com.loong.common.utils.UIUtil;
import com.loong.template.common.persenter.CommonPresenter;
import com.loong.template.databinding.ActivityForgetPasswordBinding;

/**
 *    @author : Administrator
 *    @e-mail : xuelong9009@qq.com
 *    @date   : 2022/3/16
 *    desc   : 忘记密码
 *    version: v1.0
 */
public class ForgetPasswordActivity extends BasePresenterActivity<ActivityForgetPasswordBinding, CommonPresenter> implements CommonPresenter.VCodeInterface, CommonPresenter.ForgetPwdInterface {

    public CountDownTimer countDownTimer;
    @Override
    protected void initView() {
        setTitleWithBack("忘记密码");

        binding.tvVerify.setOnClickListener(new NoRepeatClickListener() {
            @Override
            public void onNoRepeatClick(View view) {
                // 2021/11/24 请求验证码
                if (APPUtils.validatorPhone(binding.etPhone)) {
                    presenter.getVcode(binding.etPhone.getText().toString(), CommonPresenter.OLD_PHONE);
                }
            }
        });
        binding.btnSubmit.setOnClickListener(new NoRepeatClickListener() {
            @Override
            public void onNoRepeatClick(View view) {
                if (APPUtils.validatorForgetPassword(binding.etPhone, binding.etVerify, binding.etPassword, binding.etPassword)) {
                    // 2021/11/24 提交修改
                    presenter.forgetPwd(binding.etPhone.getText().toString(),
                            binding.etVerify.getText().toString(),
                            binding.etPassword.getText().toString());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onVCodeSuccess(int type, String msg) {
        countDownTimer = new CountDownTimer(APPUtils.MAX_SECOND_SMS * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int second = (int) (millisUntilFinished / 1000);
                binding.tvVerify.setText(String.format("获取验证码 (%02ds)", second));
                binding.tvVerify.setEnabled(false);
            }

            @Override
            public void onFinish() {
                binding.tvVerify.setText("获取验证码");
                binding.tvVerify.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onVCodeError(int type, String error) {
        UIUtil.toast(error);
    }

    @Override
    public void onResetSuccess(String msg) {
        UIUtil.toast(msg);
        finishActivity();
    }

    @Override
    public void onResetError(String error) {
        UIUtil.toast(error);
    }

}