package com.loong.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 解决地图在主scrollview中滑动冲突的问题由于MapView被定义成final class，所以只能在容器中操作了
 * Created by 苏七 on 2016/8/3
 */

public class MapContainer extends RelativeLayout {
    private ViewGroup viewGroup;

    public MapContainer(Context context) {
        super(context);
    }

    public MapContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (viewGroup != null) {
                viewGroup.requestDisallowInterceptTouchEvent(false);
            }

        } else {
            if (viewGroup != null) {
                viewGroup.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}