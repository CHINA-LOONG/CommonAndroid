package com.loong.template.common.activity;

import android.content.Intent;

import com.loong.common.base.IBaseView;
import com.loong.common.base.presenter.BasePresenter;
import com.loong.common.base.presenter.BasePresenterActivity;
import com.loong.common.event.LoginEvent;
import com.loong.common.update.UpdateUtils;
import com.loong.template.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/**
 *    @author : Administrator
 *    @e-mail : xuelong9009@qq.com
 *    @date   : 2022/3/16
 *    desc   : 主界面
 *    version: v1.0
 */
public class MainActivity extends BasePresenterActivity<ActivityMainBinding, BasePresenter> {


    @Override
    protected void initView() {
        UpdateUtils.getInstance().autoUpdate(this);
    }



    // 监听网络的token过期消息--因为main是常驻界面所以在main中侦听
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventListener(LoginEvent event) {
        if (event.getType() == 3){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



}