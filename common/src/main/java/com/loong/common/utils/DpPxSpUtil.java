package com.loong.common.utils;

import android.content.Context;

/**
 * @author : xuelong
 * @e-mail : xuelong9009@qq.com
 * @date : 2020/5/7 22:27
 * @desc : dp/sp与px相互转换
 */
public class DpPxSpUtil {
    /**
     * dp转换px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + +0.5f);
    }

    /**
     * px转换dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,float spValue){
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale+0.5f);
    }

    /**
     * px转换成sp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
