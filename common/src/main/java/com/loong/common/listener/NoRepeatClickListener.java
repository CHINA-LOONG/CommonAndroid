package com.loong.common.listener;

import android.view.View;

import java.util.Calendar;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2020/12/149:02 AM
 * desc   :
 * version: 1.0
 */
public abstract class NoRepeatClickListener extends NoRepeatBaseListener implements View.OnClickListener {

    public abstract void onNoRepeatClick(View view);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoRepeatClick(v);
        }
    }
}
