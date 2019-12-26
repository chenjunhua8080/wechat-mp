package com.cjh.wechatmp.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharsetUtil {

    /**
     * 转换字符集
     */
    public static String toCharset(String org, String charset) {
        String resp = null;
        try {
            byte[] bytes = org.getBytes(StandardCharsets.ISO_8859_1);
            resp = new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resp;
    }

}
