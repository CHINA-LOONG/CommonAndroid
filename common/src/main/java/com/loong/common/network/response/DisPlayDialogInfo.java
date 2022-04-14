package com.loong.common.network.response;

import com.loong.common.base.IBaseDialog;

public class DisPlayDialogInfo {

    //默认的弹窗类型
    public static final String vertical_type = "vertical";
    public static final String horizontal_type = "horizontal";
    public static final String web_type = "web";
    public static final String custom_type = "custom";


    public String mDialogType = vertical_type; // 加载框类型
    private boolean isShowDialog = true;  //是否显示加载框
    private boolean isCanDialogCancel = false;//加载框是否可点击取消
    private String mNoticeMessageStr = "加载中...";//加载中提示框提示信息


    private IBaseDialog mBaseDialog = null;

    public DisPlayDialogInfo() {
    }

    public DisPlayDialogInfo(IBaseDialog baseDialog) {
        mBaseDialog = baseDialog;
    }

    public DisPlayDialogInfo(String dialogType) {
        mDialogType = dialogType;
        if (custom_type.equals(mDialogType)) {
            mDialogType = vertical_type;
        }
    }

    public DisPlayDialogInfo(String dialogType, String mNoticeMessageStr) {
        mDialogType = dialogType;
        if (custom_type.equals(mDialogType)) {
            mDialogType = vertical_type;
        }
        this.mNoticeMessageStr = mNoticeMessageStr;
    }

    public DisPlayDialogInfo(String dialogType, boolean isShowDialog, boolean isCanDialogCancel, String mNoticeMessageStr) {
        mDialogType = dialogType;
        if (custom_type.equals(mDialogType)) {
            mDialogType = vertical_type;
        }
        this.isShowDialog = isShowDialog;
        this.isCanDialogCancel = isCanDialogCancel;
        this.mNoticeMessageStr = mNoticeMessageStr;
    }



    public String getmDialogType() {
        return mDialogType;
    }

    public void setmDialogType(String mDialogType) {
        this.mDialogType = mDialogType;
    }

    public boolean isShowDialog() {
        return isShowDialog;
    }

    public void setShowDialog(boolean showDialog) {
        isShowDialog = showDialog;
    }

    public boolean isCanDialogCancel() {
        return isCanDialogCancel;
    }

    public void setCanDialogCancel(boolean canDialogCancel) {
        isCanDialogCancel = canDialogCancel;
    }

    public String getmNoticeMessageStr() {
        return mNoticeMessageStr;
    }

    public void setmNoticeMessageStr(String mNoticeMessageStr) {
        this.mNoticeMessageStr = mNoticeMessageStr;
    }

    public IBaseDialog getmBaseDialog() {
        return mBaseDialog;
    }

    public void setmBaseDialog(IBaseDialog mBaseDialog) {
        this.mBaseDialog = mBaseDialog;
    }
}
