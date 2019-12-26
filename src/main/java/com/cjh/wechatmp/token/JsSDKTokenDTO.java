package com.cjh.wechatmp.token;

import lombok.Data;

@Data
public class JsSDKTokenDTO {

    private String appId;
    private long timestamp;
    private String nonceStr;
    private String signature;
    private String url;
}
