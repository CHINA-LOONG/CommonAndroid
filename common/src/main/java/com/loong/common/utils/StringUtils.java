package com.loong.common.utils;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要功能： 字符串判断工具类
 */
public class StringUtils {
    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || str.trim().length() == 0;
    }

    /**
     * 判断str null,"","null" 均视为空.
     *
     * @param str 字符
     * @return 结果 boolean
     */
    public static boolean isNotEmpty(String str) {
        boolean bool = true;
        if (str == null || "null".equals(str) || "".equals(str)) {
            bool = false;
        } else {
            bool = true;
        }
        return bool;
    }


    /**
     * 将字符串的第一位转为小写
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0)))
            return str;
        else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
    }

    /**
     * 将字符串的第一位转为大写
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0)))
            return str;
        else {
            char[] chars = str.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }
    }

    //获取某个字符在字符串中出现的第一次的位置
    public static int getFirstPosition(String string, String flag) {

        Matcher slashMatcher = Pattern.compile(flag).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == 1) {
                break;
            }
        }
        return slashMatcher.start();
    }

    /**
     * 判断字符串是否是金额
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        Pattern pattern= Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(str);
        if(match.matches()==false){
            return false;
        }
        else{
            return true;
        }
    }
    public static String join(CharSequence delimiter,
                                   Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

}
