package com.cjh.wechatmp.token;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TokenEntity {

    /**
     * 基础支持
     */
    private String accessToken;
    private int expiresIn;
    /**
     * 网页授权/jsSDK支持
     */
    private String refreshToken;
    private String openid;
    private String scope;
}
