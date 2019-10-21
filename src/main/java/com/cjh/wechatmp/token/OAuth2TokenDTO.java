package com.cjh.wechatmp.token;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OAuth2TokenDTO {

    private String token;
    private String accessToken;
    private String openId;
}
