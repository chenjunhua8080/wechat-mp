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
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underlineToCamel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0))
                : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

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
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String word = matcher.group();
            str = str.replace(word, "_" + word.toLowerCase());
        }
        str = String.valueOf(str.charAt(0)).toLowerCase().concat(str.substring(1));
        return str;
    }

    public static void main(String[] args) {
        System.out.println(UnderlineToCamelUtils.camelToUnderline(
            "{\"button\":[{\"key\":\"menu_1\",\"name\":\"菜单1呀\",\"type\":\"click\"},{\"name\":\"菜单2呀\",\"subButton\":[{\"key\":\"menu_2_1\",\"name\":\"菜单2_1呀\",\"type\":\"click\"},{\"name\":\"菜单2_2呀\",\"type\":\"view\",\"url\":\"https://developers.weixin.qq.com\"}]}]}"));
    }
}