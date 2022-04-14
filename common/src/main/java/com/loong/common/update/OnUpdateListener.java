package com.loong.common.update;

/**
 * Created by  on 2016/12/6.
 */
public interface OnUpdateListener {
    void needForceUpdate(UpdateBean updateBean);
    void needUpdate(UpdateBean updateBean);
    void noNeedUpdate();
}
