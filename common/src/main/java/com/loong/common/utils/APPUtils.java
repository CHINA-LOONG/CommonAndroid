package com.loong.common.utils;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

public class APPUtils {
    // 分页请求列表数量
    public static final int PAGE_SIZE = 20;

    // 验证码倒计时时长
    public static final int MAX_SECOND_SMS = 60;

    /**
     * 登录验证器
     *
     * @param etPhone    手机号输入框
     * @param etPassword 密码输入框
     * @return 验证结果
     */
    public static boolean validatorLogin(EditText etPhone, EditText etPassword) {
        return validatorPhone(etPhone) && validatorPassword(etPassword);
    }

    /**
     * 登录验证器
     *
     * @param etPhone    手机号输入框
     * @param etPassword 密码输入框
     * @param cbAgree    协议复选框
     * @return 验证结果
     */
    public static boolean validatorLogin(EditText etPhone, EditText etPassword, CheckBox cbAgree) {
        return validatorLogin(etPhone, etPassword) && validatorAgreement(cbAgree);
    }

    /**
     * 忘记密码验证
     *
     * @param etPhone           手机号
     * @param etVerify          验证码
     * @param etPassword        密码
     * @param etConfirmPassword 确认密码
     * @return 验证结果
     */
    public static boolean validatorForgetPassword(EditText etPhone, EditText etVerify, EditText etPassword, EditText etConfirmPassword) {
        return validatorPhone(etPhone) && validatorVerify(etVerify) && validatorConfirmPassword(etPassword, etConfirmPassword);
    }

    /**
     * 修改手机号验证
     *
     * @param etVerifyOld 原手机验证码
     * @param etPhoneNew  新手机号
     * @param etVerifyNew 新手机验证码
     * @return 验证是否成功
     */
    public static boolean validatorResetPhone(EditText etVerifyOld, EditText etPhoneNew, EditText etVerifyNew) {
        return validatorVerify(etVerifyOld) && validatorPhoneNew(etPhoneNew) && validatorVerifyNew(etVerifyNew);
    }

    /**
     * 修改密码验证
     * @param etPasswordOld 原密码
     * @param etPasswordNew 新密码
     * @param etConfirmPassword 确认新密码
     * @return
     */
    public static boolean validatorResetPassword(EditText etPasswordOld,EditText etPasswordNew,EditText etConfirmPassword){
        return validatorPassword(etPasswordOld)&&validatorConfirmPassword(etPasswordNew,etConfirmPassword);
    }

    /**
     * 手机号验证器
     *
     * @param etPhone 手机号输入框
     * @return 验证结果
     */
    public static boolean validatorPhone(EditText etPhone) {
        String valitatorPhone = APPUtils.validatorPhone(etPhone.getText().toString());
        if (!TextUtils.isEmpty(valitatorPhone)) {
            UIUtil.toast(valitatorPhone);
            return false;
        }
        return true;
    }

    /**
     * 手机号验证
     *
     * @param phone 输入的手机号
     * @return 验证结果
     */
    private static String validatorPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "请输入手机号";
        } else if (phone.length() != 11) {
            return "请输入正确的手机号";
        }
        return null;
    }

    /**
     * 手机号验证器
     *
     * @param etPassword 密码
     * @return 验证结果
     */
    public static boolean validatorPassword(EditText etPassword) {
        String validatorPassword = APPUtils.validatorPassword(etPassword.getText().toString());
        if (!TextUtils.isEmpty(validatorPassword)) {
            UIUtil.toast(validatorPassword);
            return false;
        }
        return true;
    }

    /**
     * 密码验证
     *
     * @param password 输入的密码
     * @return 验证结果
     */
    private static String validatorPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "请输入密码";
        }
        return null;
    }

    /**
     * 协议验证器
     *
     * @param cbAgree 协议复选框
     * @return 验证结果
     */
    private static boolean validatorAgreement(CheckBox cbAgree) {
        String validatorAgreement = APPUtils.validatorAgreement(cbAgree.isChecked());
        if (!TextUtils.isEmpty(validatorAgreement)) {
            UIUtil.toast(validatorAgreement);
            return false;
        }
        return true;
    }

    /**
     * 协议验证
     *
     * @param cbAgree 是否同意
     * @return 验证结果
     */
    private static String validatorAgreement(boolean cbAgree) {
        if (!cbAgree) {
            UIUtil.toast("请先阅读并同意用户协议和隐私协议");
        }
        return cbAgree ? null : "请先阅读并同意用户协议和隐私协议";
    }

    /**
     * 确认密码验证器
     *
     * @param etConfirmPassword 确认密码
     * @return 验证结果
     */
    private static boolean validatorConfirmPassword(EditText etPassword, EditText etConfirmPassword) {
        String validatorConfirmPassword = APPUtils.validatorConfirmPassword(etPassword.getText().toString(), etConfirmPassword.getText().toString());
        if (!TextUtils.isEmpty(validatorConfirmPassword)) {
            UIUtil.toast(validatorConfirmPassword);
            return false;
        }
        return true;
    }

    /**
     * 确认密码验证
     *
     * @param password 输入确认密码
     * @return 验证结果
     */
    private static String validatorConfirmPassword(String password, String confirmPassword) {
        String validatorPassword = validatorPassword(password);
        if (!TextUtils.isEmpty(validatorPassword)) {
            return validatorPassword;
        }

        if (TextUtils.isEmpty(password)) {
            return "请输入确认密码";
        }
        if (!TextUtils.equals(password, confirmPassword)) {
            return "两次密码输入不一致";
        }
        return null;
    }

    /**
     * 验证码验证器
     *
     * @param etVerify 验证码
     * @return 验证结果
     */
    private static Boolean validatorVerify(EditText etVerify) {
        String validatorVerify = APPUtils.validatorVerify(etVerify.getText().toString());
        if (!TextUtils.isEmpty(validatorVerify)) {
            UIUtil.toast(validatorVerify);
            return false;
        }
        return true;
    }

    /**
     * 验证码验证
     *
     * @param verify 输入的验证码
     * @return 验证结果
     */
    private static String validatorVerify(String verify) {
        if (TextUtils.isEmpty(verify)) {
            return "请输入验证码";
        }
        if (verify.length()!=6){
            return "请输入6位手机验证码";
        }
        return null;
    }


    /**
     * 新手机号验证
     *
     * @param etPhone 手机号输入框
     * @return 验证结果
     */
    public static boolean validatorPhoneNew(EditText etPhone) {
        String validatorPhoneNew = APPUtils.validatorPhoneNew(etPhone.getText().toString());
        if (!TextUtils.isEmpty(validatorPhoneNew)) {
            UIUtil.toast(validatorPhoneNew);
            return false;
        }
        return true;
    }

    /**
     * 新手机号验证
     *
     * @param phone 输入的手机号
     * @return 验证结果
     */
    private static String validatorPhoneNew(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "请输入新手机号";
        } else if (phone.length() != 11) {
            return "请输入正确的新手机号";
        }
        return null;
    }

    /**
     * 新手机验证码
     *
     * @param etVerify 验证码
     * @return 验证结果
     */
    private static Boolean validatorVerifyNew(EditText etVerify) {
        String validatorVerifyNew = APPUtils.validatorVerifyNew(etVerify.getText().toString());
        if (!TextUtils.isEmpty(validatorVerifyNew)) {
            UIUtil.toast(validatorVerifyNew);
            return false;
        }
        return true;
    }

    /**
     * 新手机验证码
     *
     * @param verify 输入的验证码
     * @return 验证结果
     */
    private static String validatorVerifyNew(String verify) {
        if (TextUtils.isEmpty(verify)) {
            return "请输入新手机验证码";
        }
        if (verify.length()!=6){
            return "请输入6位新手机号验证码";
        }
        return null;
    }


}
