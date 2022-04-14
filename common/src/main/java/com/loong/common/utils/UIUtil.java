package com.loong.common.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.loong.common.application.BaseApp;

/*******************************************************************
 * UIUtil.java  2019/4/17
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/
public class UIUtil {
    public static void toast(String message) {
        Toast toast = Toast.makeText(Utils.getApp(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
