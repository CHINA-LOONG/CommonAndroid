package com.loong.common.listener;

public abstract class NoRepeatBaseListener {
    protected static final int MIN_CLICK_DELAY_TIME = 1000;
    protected long lastClickTime = 0;
}
