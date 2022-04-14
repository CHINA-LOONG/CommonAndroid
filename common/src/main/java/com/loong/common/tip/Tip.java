package com.loong.common.tip;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2020/9/152:07 PM
 * desc   :
 * version: 1.0
 */
public class Tip {
    /**
     * desc :停靠的位置
     */
    enum Gravity {
        LEFT, RIGHT, TOP, BOTTOM, CENTER
    }


    private Context context;
    private WindowManager windowManager;


    public Tip(Context context,Builder builder){
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    }

    public void show(View parent, Gravity gravity,boolean fitToScreen){

    }

    public static class Builder{
        private Context context;        // 界面上下文
        private CharSequence text;      // 显示的文案
        private boolean showArrow;      // 显示方向标
        private View anchorView;        // 锚点的View

        public Builder(Context context) {
            this.context = context;
        }

        public Tip create(){
            return new Tip(context,this);
        }
    }

}
