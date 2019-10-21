package com.cjh.wechatmp.token;

import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.exception.ServiceException;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.redis.RedisConstant;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.service.UserService;
import com.cjh.wechatmp.sign.MpProperty;
import com.cjh.wechatmp.util.JsonUtil;
import com.cjh.wechatmp.util.RestTemplateUtil;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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
    private UserService userService;

    /**
     * 获取基础支持token
     */
    public String getBaseToken() {
        String key = RedisConstant.BASE_TOKEN + mpProperty.getAppID();
        String baseToken = redisService.get(key);
        log.info("获取基础支持token: {}", baseToken);
        if (baseToken == null) {
            String url = WxApi.BASE_TOKEN
                .replace("APPID", mpProperty.getAppID())
                .replace("APPSECRET", mpProperty.getAppSecret());
            String resp = RestTemplateUtil.doGet(url);
            TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
            baseToken = tokenEntity.getAccessToken();
            if (baseToken == null) {
                log.error("获取{}失败: {}", key, resp);
            } else {
                log.info("更新{}: {}", key, baseToken);
                redisService.set(key, baseToken, RedisConstant.EXIST_SEC_7200);
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
        String openid = tokenEntity.getOpenid();

        //创建用户
        UserPO userPO = userService.getByOpenId(openid);
        if (userPO == null) {
            userPO = new UserPO();
            userPO.setOpenId(openid);
            userService.save(userPO);
        }

        OAuth2TokenDTO tokenDTO = new OAuth2TokenDTO();
        tokenDTO.setOpenId(openid);
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

    /**
     * 获取jsSDK授权token
     */
    private String getJsSDKToken() {
        String key = RedisConstant.JSSKD_TOKEN + mpProperty.getAppID();
        String jsSDKToken = redisService.get(key);
        log.info("获取jsSDK授权token: {}", jsSDKToken);
        if (jsSDKToken == null) {
            String url = WxApi.JSSDK_TOKEN.replace("ACCESS_TOKEN", getBaseToken());
            String resp = RestTemplateUtil.doGet(url);
            TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
            jsSDKToken = tokenEntity.getTicket();
            if (jsSDKToken == null) {
                log.error("获取{}失败: {}", key, resp);
                throw new ServiceException(resp);
            } else {
                log.info("更新{}: {}", key, jsSDKToken);
                redisService.set(key, jsSDKToken, RedisConstant.EXIST_SEC_7200);
            }
        }
        return jsSDKToken;
    }

    /**
     * 获取jsSDK授权token DTO
     */
    public JsSDKTokenDTO getJsSDKTokenDTO(String url) {
        JsSDKTokenDTO dto;
        String key = RedisConstant.JSSKD_TOKEN + url;
        String jsonDTO = redisService.get(key);
        if (jsonDTO == null) {
            dto = new JsSDKTokenDTO();
            dto.setAppId(mpProperty.getAppID());
            dto.setNonceStr(UUID.randomUUID().toString());
            dto.setTimestamp(System.currentTimeMillis());
            dto.setUrl(url);
            StringBuilder sb = new StringBuilder();
            sb.append("jsapi_ticket=").append(getJsSDKToken())
                .append("&noncestr=").append(dto.getNonceStr())
                .append("&timestamp=").append(dto.getTimestamp())
                .append("&url=").append(url);
            String sign = DigestUtils.sha1Hex(sb.toString());
            dto.setSignature(sign);
            jsonDTO = JsonUtil.java2Json(dto);
            log.info("更新{}: {}", key, jsonDTO);
            redisService.set(key, jsonDTO, RedisConstant.EXIST_MIN_5);
        } else {
            log.info("获取{}: {}", key, jsonDTO);
            dto = JsonUtil.json2java(jsonDTO, JsSDKTokenDTO.class);
        }
        return dto;
    }

    public static void main(String[] args) {
        String resp = "{\"errcode\":40013,\"errmsg\":\"invalid appid\"}";
        TokenEntity tokenEntity = JsonUtil.json2java(resp, TokenEntity.class);
        System.out.println(tokenEntity);
    }

}
