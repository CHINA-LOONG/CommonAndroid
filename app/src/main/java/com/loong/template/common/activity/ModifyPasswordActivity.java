package com.loong.template.common.activity;

import android.view.View;

import com.loong.common.base.presenter.BasePresenter;
import com.loong.common.base.presenter.BasePresenterActivity;
import com.loong.common.listener.NoRepeatClickListener;
import com.loong.common.utils.APPUtils;
import com.loong.common.utils.UIUtil;
import com.loong.template.common.persenter.CommonPresenter;
import com.loong.template.databinding.ActivityModifyPasswordBinding;

/**
 *    @author : Administrator
 *    @e-mail : xuelong9009@qq.com
 *    @date   : 2022/3/16
 *    desc   : 修改密码
 *    version: v1.0
 */
public class ModifyPasswordActivity extends BasePresenterActivity<ActivityModifyPasswordBinding, CommonPresenter> implements CommonPresenter.ModifyPwdInterface {

    @Override
    protected void initView() {

        setTitleWithBack("修改密码");


        binding.btnSubmit.setOnClickListener(new NoRepeatClickListener() {
            @Override
            public void onNoRepeatClick(View view) {
                if (APPUtils.validatorResetPassword(binding.etPasswordOld,binding.etPasswordNew,binding.etConfirmPassword)){
                    // 2021/11/25 提交修改
                    presenter.modifyPwd(binding.etPasswordOld.getText().toString(),binding.etPasswordNew.getText().toString());
                }
            }
        });
    }

    @Override
    public void onModifySuccess(String msg) {
        UIUtil.toast(msg);
        finishActivity();
    }

    @Override
    public void onModifyError(String error) {
        UIUtil.toast(error);
    }
}