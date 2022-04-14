package com.loong.common.utils;

import com.orhanobut.logger.Logger;

public class LoggerUtils {

    public static void loggerW(String message) {
        if (!StringUtils.isEmpty(message)) {
            Logger.w(message);
        } else {
            Logger.w("打印内容为空");
        }
    }

    public static void loggerW(String tagStr, String message) {
        if (!StringUtils.isEmpty(message)) {
            Logger.t(tagStr).w(message);
        } else {
            Logger.t(tagStr).w("打印内容为空");
        }
    }


    public static void loggerI(String message) {
        if (!StringUtils.isEmpty(message)) {
            Logger.e(message);
        } else {
            Logger.e("打印内容为空");
        }
    }

    public static void loggerI(String tagStr, String message) {
        if (!StringUtils.isEmpty(message)) {
            Logger.t(tagStr).e(message);
        } else {
            Logger.t(tagStr).e("打印内容为空");
        }
    }


    public static void loggerE(String message) {
        if (!StringUtils.isEmpty(message)) {
            Logger.e(message);
        } else {
            Logger.e("打印内容为空");
        }
    }

    public static void loggerE(String tagStr, String message) {
        if (!StringUtils.isEmpty(message)) {
            Logger.t(tagStr).e(message);
        } else {
            Logger.t(tagStr).e("打印内容为空");
        }
    }


    public static void loggerJson(String jsonStr) {
        if (!StringUtils.isEmpty(jsonStr)) {
            Logger.json(jsonStr);
        } else {
            Logger.json("打印Json字符串为空");
        }
    }

    public static void loggerJson(String tagStr, String jsonStr) {
        if (!StringUtils.isEmpty(jsonStr)) {
            Logger.t(tagStr).json(jsonStr);
        } else {
            Logger.t(tagStr).json("打印Json字符串为空");
        }
    }


    public static void loggerHtml(String xmlStr) {
        if (!StringUtils.isEmpty(xmlStr)) {
            Logger.xml(xmlStr);
        } else {
            Logger.xml("打印xml字符串为空");
        }
    }

    public static void loggerHtml(String tagStr, String xmlStr) {
        if (!StringUtils.isEmpty(xmlStr)) {
            Logger.t(tagStr).xml(xmlStr);
        } else {
            Logger.t(tagStr).xml("打印xml字符串为空");
        }
    }

}
