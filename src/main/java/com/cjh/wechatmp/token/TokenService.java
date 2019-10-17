package com.cjh.wechatmp.token;

import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.redis.RedisConstant;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.sign.MpProperty;
import com.cjh.wechatmp.util.JsonUtil;
import com.cjh.wechatmp.util.RestTemplateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 凭证服务
 */
@AllArgsConstructor
@Slf4j
@Component
public class TokenService {

    private MpProperty mpProperty;
    private RedisService redisService;

    /**
     * 获取基础支持token
     */
    public String getBaseToken() {
        String baseToken = redisService.get(RedisConstant.BASE_TOKEN);

        if (baseToken == null) {
            String url = WxApi.BASE_TOKEN
                .replace("APPID", mpProperty.getAppID())
                .replace("APPSECRET", mpProperty.getAppSecret());
            String resp = RestTemplateUtil.doGet(url);
            TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
            baseToken = tokenEntity.getAccessToken();
            if (baseToken == null) {
                log.error("获取{}失败: {}", RedisConstant.BASE_TOKEN, resp);
            } else {
                log.info("更新{}: {}", RedisConstant.BASE_TOKEN, baseToken);
                redisService.set(RedisConstant.BASE_TOKEN, baseToken);
            }
        }
        return baseToken;
    }

    public static void main(String[] args) {
        String resp = "{\"errcode\":40013,\"errmsg\":\"invalid appid\"}";
        TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
        System.out.println(tokenEntity);
    }

}
