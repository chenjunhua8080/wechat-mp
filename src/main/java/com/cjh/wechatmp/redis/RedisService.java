package com.cjh.wechatmp.redis;

import com.alibaba.fastjson.JSONObject;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RedisService {

    private RedisTemplate redisTemplate;

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
     * 普通缓存放入
     *
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public <T> boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), EXIST_DAY_1, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, long time) {
        try {
            if (time != EXIST_FOREVER) {
                redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存拿出并刷新时间
     *
     * @param key 键
     * @param clazz 取出对象
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public <T> T get(String key, Class<T> clazz, long time) {
        String value = (String) redisTemplate.opsForValue().get(key);
        if (time != EXIST_FOREVER) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return value == null ? null : JSONObject.parseObject(value, clazz);
    }

    /**
     * 普通缓存拿出并刷新时间
     *
     * @param key 键
     * @param clazz 取出对象
     * @return true成功 false 失败
     */
    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, EXIST_FOREVER);
    }

}
