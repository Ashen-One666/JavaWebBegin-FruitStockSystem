package com.atguigu.myssm.util;

public class StringUtil {
    // 判断字符串是否为null或者为""
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
