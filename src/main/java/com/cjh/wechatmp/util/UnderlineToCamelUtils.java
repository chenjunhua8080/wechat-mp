package com.cjh.wechatmp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 驼峰法-下划线互转
 *
 * @author cshaper
 * @version 1.0.0
 * @since 2015.07.04
 */
public class UnderlineToCamelUtils {

    /**
     * 驼峰法转下划线
     *
     * @param str 源字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        Pattern pattern = Pattern.compile("[A-Z]([a-z]+)?");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String word = matcher.group();
            str = str.replace(word, "_" + word.toLowerCase());
        }
        str = String.valueOf(str.charAt(0)).toLowerCase().concat(str.substring(1));
        return str;
    }

}