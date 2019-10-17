package com.cjh.wechatmp.redis;

import lombok.Data;

/**
 * redis常量类，建议所有key在此定义
 */
@Data
public class RedisConstant {

    /**
     * 过期时间，单位为秒 30天
     */
    public final static long EXIST_DAY_30 = 60 * 60 * 24 * 30;
    /**
     * 过期时间，单位为秒 7天
     */
    public final static long EXIST_DAY_7 = 60 * 60 * 24 * 7;
    /**
     * 默认过期时长，1天，单位：秒
     */
    public final static long EXIST_DAY_1 = 60 * 60 * 24;
    /**
     * 过期时长一小时，单位：秒
     */
    public final static long EXIST_HOUSE_2 = 60 * 60;
    /**
     * 过期时长五分钟，单位：秒
     */
    public final static long EXIST_MIN_5 = 5 * 60;
    /**
     * 不设置过期时长
     */
    public final static long EXIST_FOREVER = -1;

    /**
     * 不设置过期时长
     */
    public final static long EXIST_SEC_7200 = 7200;

    /**
     * base_token
     */
    public final static String BASE_TOKEN = "base_token";

}
