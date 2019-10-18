package com.cjh.wechatmp.token;

import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.exception.ServiceException;
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
                redisService.set(RedisConstant.BASE_TOKEN + mpProperty.getAppID(), baseToken,
                    RedisConstant.EXIST_SEC_7200);
            }
        }
        return baseToken;
    }

    /**
     * 获取网页授权token
     */
    public OAuth2TokenDTO getOAuth2Token(String code) {
        log.info("获取网页授权token, code: {}", code);
        String oauth2Token;
        String url = WxApi.OAUTH2_TOKEN
            .replace("APPID", mpProperty.getAppID())
            .replace("APPSECRET", mpProperty.getAppSecret())
            .replace("CODE", code);
        String resp = RestTemplateUtil.doGet(url);
        TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
        oauth2Token = tokenEntity.getAccessToken();
        if (oauth2Token == null) {
            log.error("获取{}失败: {}", RedisConstant.BASE_TOKEN, resp);
            throw new ServiceException(resp);
        } else {
            String key = RedisConstant.OAUTH2_TOKEN + tokenEntity.getOpenid();
            log.info("新增{}: {}", key, oauth2Token);
            redisService.set(key, oauth2Token, RedisConstant.EXIST_SEC_7200);
        }
        oauth2Token = refreshOAuth2Token(tokenEntity.getRefreshToken());
        OAuth2TokenDTO tokenDTO = new OAuth2TokenDTO();
        tokenDTO.setOpenId(tokenEntity.getOpenid());
        tokenDTO.setAccessToken(oauth2Token);
        return tokenDTO;
    }

    /**
     * 刷新网页授权token
     */
    private String refreshOAuth2Token(String refreshToken) {
        log.info("刷新网页授权token, refreshToken: {}", refreshToken);
        String oauth2Token;
        String url = WxApi.REFRESH_OAUTH2_TOKEN
            .replace("APPID", mpProperty.getAppID())
            .replace("REFRESH_TOKEN", refreshToken);
        String resp = RestTemplateUtil.doGet(url);
        TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
        oauth2Token = tokenEntity.getAccessToken();
        if (oauth2Token == null) {
            log.error("刷新{}失败: {}", RedisConstant.OAUTH2_TOKEN, resp);
            throw new ServiceException(resp);
        } else {
            String key = RedisConstant.OAUTH2_TOKEN + tokenEntity.getOpenid();
            log.info("刷新{}: {}", key, oauth2Token);
            redisService.set(key, oauth2Token, RedisConstant.EXIST_DAY_30);
        }
        return oauth2Token;
    }

    public static void main(String[] args) {
        String resp = "{\"errcode\":40013,\"errmsg\":\"invalid appid\"}";
        TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
        System.out.println(tokenEntity);
    }

}
