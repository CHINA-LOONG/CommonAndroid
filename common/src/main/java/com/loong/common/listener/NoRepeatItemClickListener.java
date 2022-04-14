package com.loong.common.listener;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.Calendar;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2020/12/149:02 AM
 * desc   :
 * version: 1.0
 */
public abstract class NoRepeatItemClickListener extends NoRepeatBaseListener implements OnItemClickListener {

    public abstract void onNoRepeatClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position);

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoRepeatClick(adapter, view, position);
        }
    }
}
