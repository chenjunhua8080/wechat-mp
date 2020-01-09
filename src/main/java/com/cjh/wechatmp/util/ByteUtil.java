package com.cjh.wechatmp.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ByteUtil {

    /**
     * 限制字符串utf8编码下至多2047个字节
     */
    public static String limit2048byte(String content) {
        if (content.getBytes(StandardCharsets.UTF_8).length >= 2048) {
            if (content.contains("\n")) {
                content = content.substring(0, content.lastIndexOf("\n"));
                if (content.getBytes(StandardCharsets.UTF_8).length >= 2048) {
                    content = limit2048byte(content);
                }
            } else {
                try {
                    content = unicodeSubstring(content, 2047);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    private static String unicodeSubstring(String s, int length) throws UnsupportedEncodingException {
        length = (int) (length / (2300.0 / 1970));

        byte[] bytes = s.getBytes("Unicode");
        int n = 0;
        int i = 2;
        for (; i < bytes.length && n < length; i++) {
            if (i % 2 == 0) {
                n++;
            } else {
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        //将截一半的汉字要保留
        if (i % 2 == 1) {
            i = i + 1;
        }
        return new String(bytes, 0, i, "Unicode");
    }

}
