package com.cjh.wechatmp.token;

import com.cjh.wechatmp.api.WxApi;
import com.cjh.wechatmp.exception.ServiceException;
import com.cjh.wechatmp.po.UserPO;
import com.cjh.wechatmp.po.WeChatUser;
import com.cjh.wechatmp.redis.RedisConstant;
import com.cjh.wechatmp.redis.RedisService;
import com.cjh.wechatmp.service.UserService;
import com.cjh.wechatmp.sign.MpProperty;
import com.cjh.wechatmp.util.CharsetUtil;
import com.cjh.wechatmp.util.JsonUtil;
import com.cjh.wechatmp.util.RestTemplateUtil;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
            //获取用户微信基本信息
            WeChatUser baseInfo = getBaseInfo(oauth2Token, openid);
            userPO.setName(baseInfo.getNickname());
            userPO.setAvatar(baseInfo.getHeadimgurl());
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

    /**
     * 获取用户微信基本信息
     */
    private WeChatUser getBaseInfo(String accessToken, String openId) {
        log.info("获取用户微信基本信息, openId: {}", openId);
        String url = WxApi.GET_BASE_INFO
            .replace("ACCESS_TOKEN", accessToken)
            .replace("OPENID", openId);
        String resp = RestTemplateUtil.doGet(url);
        //转换一下字符集
        resp = CharsetUtil.toCharset(resp, "utf-8");
        if (!resp.contains("nickname")) {
            return new WeChatUser();
        }
        return JsonUtil.json2java(resp, WeChatUser.class);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String resp = "respBody - {\"openid\":\"o1IsFt8QJMFhVZDE0W3ovHx15hes\",\"nickname\":\"set any bugsð\u009F¤\u0094ð\u009F¤\u0094\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"å¹¿å·\u009E\",\"province\":\"å¹¿ä¸\u009C\",\"country\":\"ä¸\u00ADå\u009B½\",\"headimgurl\":\"http:\\/\\/thirdwx.qlogo.cn\\/mmopen\\/vi_32\\/19ItTFRzOhgZLbWIlYKprVNtaXpya3VbMncXYYnKLlWfm4SnMqwKIS4WDaHd2lnnAlyCnxdDj1OTYUsolbGlHA\\/132\",\"privilege\":[]}";
        byte[] bytes = resp.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

}
