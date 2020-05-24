package com.cjh.wechatmp.redis;

import com.cjh.wechatmp.util.JsonUtil;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RedisService {

    private RedisTemplate redisTemplate;

    /**
     * 记录上一次指令
     */
    public static final String last_instruct = "last_instruct";

    public String getLastInstruct(String user, boolean delKey) {
        int time = 10;
        if (delKey) {
            time = 0;
        }
        return get(last_instruct + "_" + user, String.class, time);
    }

    public void setLastInstruct(String user, Object value) {
        set(last_instruct + "_" + user, value, 60);
    }


    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public <T> boolean set(String key, T value) {
        try {
            String cacheValue;
            if (value instanceof String) {
                cacheValue = (String) value;
            } else {
                cacheValue = JsonUtil.java2Json(value);
            }
            redisTemplate.opsForValue().set(key, cacheValue, RedisConstant.EXIST_DAY_1, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, long time) {
        try {
            String cacheValue;
            if (value instanceof String) {
                cacheValue = (String) value;
            } else {
                cacheValue = JsonUtil.java2Json(value);
            }
            if (time != RedisConstant.EXIST_FOREVER) {
                redisTemplate.opsForValue().set(key, cacheValue, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, cacheValue);
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
     * @param key   键
     * @param clazz 取出对象
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public <T> T get(String key, Class<T> clazz, long time) {
        Object value = redisTemplate.opsForValue().get(key);
        if (time != RedisConstant.EXIST_FOREVER) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        if (value instanceof String) {
            return (T) value;
        }
        return value == null ? null : JsonUtil.json2java(String.valueOf(value), clazz);
    }

    /**
     * 普通缓存拿出并刷新时间
     *
     * @param key   键
     * @param clazz 取出对象
     * @return true成功 false 失败
     */
    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, RedisConstant.EXIST_FOREVER);
    }

    /**
     * 普通缓存拿出
     *
     * @param key 键
     * @return true成功 false 失败
     */
    public String get(String key) {
        return get(key, String.class, RedisConstant.EXIST_FOREVER);
    }

}
