package com.cjh.wechatmp.sign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mp")
@Component
@Data
public class MpEntity {


    /**
     * 开发者ID是公众号开发识别码，配合开发者密码可调用公众号的接口能力。
     */
    private String appID;
    /**
     * 开发者密码是校验公众号开发者身份的密码，具有极高的安全性。切记勿把密码直接交给第三方开发者或直接存储在代码中。如需第三方代开发公众号，请使用授权方式接入。
     */
    private String appSecret;
    /**
     * token
     */
    private String token;
    /**
     * 消息加密密钥由43位字符组成，可随机修改，字符范围为A-Z，a-z，0-9。
     */
    private String encodingAESKey;

}
