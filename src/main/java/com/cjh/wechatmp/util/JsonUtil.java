package com.cjh.wechatmp.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    /**
     * java2json
     */
    public static String java2Json(Object object) {
        return JSONObject.toJSONString(object);
    }

    /**
     * java2json
     */
    public static String java2Json2_(Object object) {
        String json = JSONObject.toJSONString(object);
        return UnderlineToCamelUtils.camelToUnderline(json);
    }

    /**
     * json2java
     */
    public static <T> T json2java(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

    /**
     * str2json
     */
    public static JSONObject str2json(String object) {
        return JSONObject.parseObject(object);
    }

}
